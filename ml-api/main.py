"""
GAMESUP - API de recommandation par Machine Learning (KNN)

Cette API FastAPI implémente un système de recommandation de jeux de société
basé sur l'algorithme K-Nearest Neighbors (KNN).
"""

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Dict, Optional
import numpy as np
import pandas as pd
from sklearn.neighbors import NearestNeighbors
from sklearn.preprocessing import StandardScaler
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = FastAPI(
    title="GAMESUP ML API",
    description="API de recommandation de jeux de société par Machine Learning (KNN)",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


class PurchaseInput(BaseModel):
    user_id: int
    game_id: int
    rating: Optional[float] = 5.0


class RecommendationRequest(BaseModel):
    user_id: int
    n_recommendations: Optional[int] = 5


class RecommendationResponse(BaseModel):
    user_id: int
    recommended_game_ids: List[int]
    scores: List[float]


class TrainResponse(BaseModel):
    message: str
    n_users: int
    n_games: int
    n_interactions: int


class GamePopularity(BaseModel):
    game_id: int
    purchase_count: int
    avg_rating: float


# ---------------------------------------------------------------------------
# État global du modèle (en mémoire, à remplacer par une BDD en production)
# ---------------------------------------------------------------------------

_model: Optional[NearestNeighbors] = None
_scaler: Optional[StandardScaler] = None
_user_item_matrix: Optional[pd.DataFrame] = None
_game_id_to_index: Dict[int, int] = {}
_index_to_game_id: Dict[int, int] = {}
_user_id_to_index: Dict[int, int] = {}
_index_to_user_id: Dict[int, int] = {}
_is_trained: bool = False

# Données d'interactions utilisateur-jeu (user_id, game_id, rating)
_interactions: List[PurchaseInput] = []


# ---------------------------------------------------------------------------
# Endpoints
# ---------------------------------------------------------------------------


@app.get("/")
async def root():
    return {
        "service": "GAMESUP ML API",
        "status": "running",
        "model_trained": _is_trained,
        "algorithm": "KNN (K-Nearest Neighbors)"
    }


@app.get("/health")
async def health():
    return {"status": "healthy"}


@app.post("/interactions", status_code=201)
async def add_interaction(interaction: PurchaseInput):
    _interactions.append(interaction)
    logger.info(f"Interaction ajoutée: user={interaction.user_id}, game={interaction.game_id}")
    return {"message": "Interaction enregistrée"}


@app.post("/train", response_model=TrainResponse)
async def train_model():
    global _model, _scaler, _user_item_matrix, _game_id_to_index, _index_to_game_id
    global _user_id_to_index, _index_to_user_id, _is_trained

    if not _interactions:
        raise HTTPException(status_code=400, detail="Aucune interaction d'entraînement disponible")

    df = pd.DataFrame([{
        "user_id": i.user_id,
        "game_id": i.game_id,
        "rating": i.rating
    } for i in _interactions])

    n_users = df["user_id"].nunique()
    n_games = df["game_id"].nunique()
    n_interactions = len(df)

    _user_item_matrix = df.pivot_table(
        index="user_id",
        columns="game_id",
        values="rating",
        fill_value=0
    )

    _user_id_to_index = {uid: idx for idx, uid in enumerate(_user_item_matrix.index)}
    _index_to_user_id = {idx: uid for uid, idx in _user_id_to_index.items()}
    _game_id_to_index = {gid: idx for idx, gid in enumerate(_user_item_matrix.columns)}
    _index_to_game_id = {idx: gid for gid, idx in _game_id_to_index.items()}

    _scaler = StandardScaler()
    matrix_scaled = _scaler.fit_transform(_user_item_matrix.values)

    n_neighbors = min(5, n_users)
    _model = NearestNeighbors(n_neighbors=n_neighbors, metric="cosine")
    _model.fit(matrix_scaled)

    _is_trained = True

    return TrainResponse(
        message="Modèle KNN entraîné avec succès",
        n_users=n_users,
        n_games=n_games,
        n_interactions=n_interactions
    )


@app.post("/recommend", response_model=RecommendationResponse)
async def recommend(request: RecommendationRequest):
    if not _is_trained:
        raise HTTPException(status_code=400, detail="Le modèle n'est pas encore entraîné. Appelez /train d'abord.")

    if request.user_id not in _user_id_to_index:
        raise HTTPException(status_code=404, detail=f"Utilisateur {request.user_id} non trouvé dans le modèle")

    user_idx = _user_id_to_index[request.user_id]

    user_vector = _user_item_matrix.iloc[user_idx].values.reshape(1, -1)
    user_vector_scaled = _scaler.transform(user_vector)

    distances, indices = _model.kneighbors(user_vector_scaled, n_neighbors=min(6, len(_user_id_to_index)))

    recommended_scores: Dict[int, float] = {}
    user_games = set(_user_item_matrix.columns[
        _user_item_matrix.iloc[user_idx] > 0
    ])

    for neighbor_idx in indices[0]:
        neighbor_user_id = _index_to_user_id[int(neighbor_idx)]
        neighbor_games = _user_item_matrix.iloc[neighbor_idx]

        for game_id in neighbor_games.index:
            if neighbor_games[game_id] > 0 and game_id not in user_games:
                gid = int(game_id)
                score = float(neighbor_games[game_id])
                recommended_scores[gid] = recommended_scores.get(gid, 0) + score

    sorted_recommendations = sorted(
        recommended_scores.items(),
        key=lambda x: x[1],
        reverse=True
    )[:request.n_recommendations]

    return RecommendationResponse(
        user_id=request.user_id,
        recommended_game_ids=[gid for gid, _ in sorted_recommendations],
        scores=[score for _, score in sorted_recommendations]
    )


@app.get("/games/popular", response_model=List[GamePopularity])
async def popular_games():
    if not _interactions:
        return []

    df = pd.DataFrame([{
        "game_id": i.game_id,
        "rating": i.rating
    } for i in _interactions])

    popularity = df.groupby("game_id").agg(
        purchase_count=("rating", "count"),
        avg_rating=("rating", "mean")
    ).reset_index()

    popularity = popularity.sort_values("purchase_count", ascending=False)

    return [
        GamePopularity(
            game_id=int(row["game_id"]),
            purchase_count=int(row["purchase_count"]),
            avg_rating=round(float(row["avg_rating"]), 2)
        )
        for _, row in popularity.iterrows()
    ]


@app.post("/reset")
async def reset_model():
    global _model, _scaler, _user_item_matrix, _game_id_to_index, _index_to_game_id
    global _user_id_to_index, _index_to_user_id, _is_trained, _interactions

    _model = None
    _scaler = None
    _user_item_matrix = None
    _game_id_to_index = {}
    _index_to_game_id = {}
    _user_id_to_index = {}
    _index_to_user_id = {}
    _is_trained = False
    _interactions = []

    return {"message": "Modèle et données réinitialisés"}


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8001)