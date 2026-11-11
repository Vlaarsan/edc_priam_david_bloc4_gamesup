# GamesUP - Plateforme de vente de jeux de société

## 📋 Résumé

Application complète de gestion de catalogue de jeux de société avec **API Spring Boot** sécurisée (Spring Security, 2 rôles : client/admin), **API Python FastAPI** intégrant un algorithme de recommandation **KNN (K-Nearest Neighbors)** pour l'analyse de données massives et le Machine Learning.

## Architecture

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   AngularJS   │ ──▶ │  API Spring   │ ──▶ │  MySQL DB     │
│   Frontend    │     │   (Java 21)   │     │  (jeux, users,│
│               │     │   + Spring    │     │   commandes)  │
│               │     │   Security    │     └──────────────┘
└──────────────┘     └──────┬────────┘
                           │
                    ┌──────▼──────┐
                    │  API Python  │
                    │   FastAPI    │
                    │   + KNN      │
                    └──────────────┘
```

### Diagrammes UML

#### Diagramme d'architecture
```
┌─────────────────┐    REST    ┌──────────────────┐
│   Frontend      │──────────▶│  API Spring Boot    │
│   (Angular)     │            │  (Java 21)        │
└─────────────────┘            │  Spring Security  │
                               │  Hibernate/JPA    │
                               │  MySQL DB         │
                               └────────┬───────────┘
                                        │
                                        │ HTTP
                                        ▼
                            ┌──────────────────┐
                            │  API Python       │
                            │  FastAPI + KNN    │
                            │  (ML Recommand.)  │
                            └──────────────────┘
```

#### Diagramme de classes (extraits)
```
┌──────────────┐     1    ┌──────────────┐
│   Game       │──────────▶│    Author    │
│  - id        │   1      │  - id        │
│  - nom       │──────────▶│  - name      │
│  - prix      │          │  - games     │
│  - stock     │          └──────────────┘
│  - author    │◀──────────┤
│  - category  │           │ 1
└──────────────┘           │    ┌──────────────┐
                           │    │  Category     │
                           │    │  - type       │
└─────────────────────────┼───▶│  - games      │
                           │    └──────────────┘
                           │    ┌──────────────┐
                           └────│ Publisher     │
                                │  - name       │
                                └──────────────┘
```

#### Diagramme de séquence - Achat d'un jeu
```
Client          API Spring          ML API          MySQL
  │                    │                   │
  │-- POST /purchases │                   │
  │----(JWT)---------▶│                   │
  │                    │---- GET /recommend│
  │                    │<---- (recommand.) │
  │                    │                   │
  │                    │-- INSERT ... ────▶│
  │                    │<-- OK────────────│
  │<---- 200 OK───────│                   │
```

### Respect des principes SOLID

| Principe | Application |
|----------|-------------|
| **S**ingle Responsibility | Chaque classe a une seule responsabilité (GameService, AuthorService, etc.) |
| **O**pen/Closed | Les services sont extensibles sans modifier le code existant |
| **L**iskov Substitution | Tous les services implémentent des interfaces cohérentes |
| **I**nterface Segregation | Interfaces spécialisées (GameRepository, AuthorRepository, etc.) |
| **D**ependency Inversion | Dépendances inversées via injection de constructeur (@RequiredArgsConstructor) |

## 🌐 API Endpoints

| Méthode | Endpoint | Description | Rôle |
|---------|----------|-------------|--------|
| POST | /api/auth/register | Inscription | Public |
| POST | /api/auth/login | Connexion, retourne JWT | Public |
| GET | /api/games | Liste des jeux | Public |
| GET | /api/games/{id} | Détail du jeu | Public |
| GET | /api/games/search?nom= | Recherche par nom | Public |
| POST | /api/games | Créer un jeu | ADMIN |
| GET | /api/users | Liste des utilisateurs | ADMIN |
| POST | /api/users | Créer un utilisateur | ADMIN |
| GET | /api/purchases | Liste des commandes | ADMIN |
| GET | /api/purchases/me | Commandes de l'utilisateur | CLIENT |
| POST | /api/purchases | Créer une commande | CLIENT |

## 🐳 Docker Compose

```bash
docker compose up --build
```

Services :
- `backend` : API Spring Boot sur le port 8080
- `ml-api` : API Python FastAPI sur le port 8001
- `mysql` : Base de données MySQL sur le port 3306
- `frontend` : Interface Angular sur le port 80

## CI/CD

La pipeline GitHub Actions exécute :
1. Les tests backend (JUnit + JaCoCo)
2. Les tests de l'API Python
3. Build et push des images Docker sur Docker Hub (si secrets configurés)

## 📁 Structure du projet

```
gamesup_clean/
├── backend/                  # API Spring Boot
│   ├── src/main/java/com/gamesup/backend/
│   │   ├── controller/       # Contrôleurs REST
│   │   ├── service/          # Logique métier
│   │   ├── repository/       # Spring Data JPA
│   │   ├── entity/           # Entités JPA (Hibernate)
│   │   ├── dto/              # DTO (skiff publ-Op)
│   │   ├── security/         # Spring Security + JWT
│   │   └── ...
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/resources/
├── ml-api/                   # API Python FastAPI + KNN
│   ├── main.py               # Entrée principale
│   ├── requirements.txt
│   └── Dockerfile
├── docker-compose.yml
├── .github/workflows/ci.yml
└── README.md
```
