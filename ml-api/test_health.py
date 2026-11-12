import uvicorn
import requests
import subprocess
import time
import sys

# Lancer l'API en arrière-plan
proc = subprocess.Popen(['uvicorn', 'main:app', '--port', '8001'], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

try:
    # Attendre que l'API soit prête (max 30 secondes)
    max_retries = 30
    for i in range(max_retries):
        try:
            r = requests.get('http://localhost:8001/health', timeout=2)
            if r.status_code == 200:
                break
        except requests.exceptions.ConnectionError:
            time.sleep(1)
    else:
        print("Erreur : l'API n'a pas démarré après 30 secondes")
        proc.terminate()
        proc.wait()
        sys.exit(1)

    assert r.status_code == 200, f"Expected 200, got {r.status_code}"
    print("ML API OK")
finally:
    proc.terminate()
    proc.wait()