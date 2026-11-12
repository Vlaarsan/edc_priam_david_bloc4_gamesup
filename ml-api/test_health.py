import uvicorn
import requests
import subprocess
import time

# Lancer l'API en arrière-plan
proc = subprocess.Popen(['uvicorn', 'main:app', '--port', '8001'], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
time.sleep(3)

try:
    r = requests.get('http://localhost:8001/health', timeout=5)
    assert r.status_code == 200, f"Expected 200, got {r.status_code}"
    print("ML API OK")
finally:
    proc.terminate()
    proc.wait()