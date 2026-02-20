"""
Test de carga - Simula 30 usuarios concurrentes haciendo login + reservas
Ejecutar: pip install requests && python load-test.py
"""

import requests
import time
import threading
from concurrent.futures import ThreadPoolExecutor, as_completed

# ============ CONFIGURACION ============
BASE_URL = "http://localhost:8080"
NUM_USUARIOS = 30

# Credenciales de un usuario real que exista en tu BD
# (se reutiliza para simular la carga concurrente)
TEST_EMAIL = "davidmorenov10@gmail.com"
TEST_PASSWORD = "Atletico8"

# Datos para la reserva de mesa
RESERVA_TEMPLATE = {
    "idCliente": 210003,
    "idMesa": 1,
    "fechaHoraInicio": "2026-03-01T19:00:00Z",
    "fechaHoraFin": "2026-03-01T21:00:00Z",
    "numPersonas": 2,
    "notas": "Test de carga"
}
# ========================================

resultados_login = {"ok": 0, "fail": 0, "tiempos": []}
resultados_reserva = {"ok": 0, "fail": 0, "tiempos": []}
lock = threading.Lock()
tokens = []
tokens_lock = threading.Lock()


def hacer_login(usuario_num):
    inicio = time.time()
    try:
        resp = requests.post(
            f"{BASE_URL}/api/auth/login",
            json={"email": TEST_EMAIL, "password": TEST_PASSWORD},
            timeout=30
        )
        duracion = (time.time() - inicio) * 1000

        with lock:
            resultados_login["tiempos"].append(duracion)
            if resp.status_code == 200:
                resultados_login["ok"] += 1
                token = resp.json().get("accessToken")
                if token:
                    with tokens_lock:
                        tokens.append(token)
                return f"  Usuario {usuario_num:02d}: OK ({duracion:.0f}ms)"
            else:
                resultados_login["fail"] += 1
                return f"  Usuario {usuario_num:02d}: FALLO {resp.status_code} ({duracion:.0f}ms)"
    except Exception as e:
        duracion = (time.time() - inicio) * 1000
        with lock:
            resultados_login["fail"] += 1
            resultados_login["tiempos"].append(duracion)
        return f"  Usuario {usuario_num:02d}: ERROR {str(e)[:50]} ({duracion:.0f}ms)"


def hacer_reserva(usuario_num, token):
    inicio = time.time()
    try:
        headers = {"Authorization": f"Bearer {token}"}
        resp = requests.post(
            f"{BASE_URL}/api/reservas-mesa",
            json=RESERVA_TEMPLATE,
            headers=headers,
            timeout=30
        )
        duracion = (time.time() - inicio) * 1000

        with lock:
            resultados_reserva["tiempos"].append(duracion)
            if resp.status_code in (200, 201):
                resultados_reserva["ok"] += 1
                return f"  Reserva {usuario_num:02d}: OK ({duracion:.0f}ms)"
            else:
                resultados_reserva["fail"] += 1
                return f"  Reserva {usuario_num:02d}: FALLO {resp.status_code} ({duracion:.0f}ms)"
    except Exception as e:
        duracion = (time.time() - inicio) * 1000
        with lock:
            resultados_reserva["fail"] += 1
            resultados_reserva["tiempos"].append(duracion)
        return f"  Reserva {usuario_num:02d}: ERROR {str(e)[:50]} ({duracion:.0f}ms)"


def imprimir_resumen(nombre, resultados):
    tiempos = resultados["tiempos"]
    if not tiempos:
        print(f"  Sin resultados")
        return
    tiempos.sort()
    print(f"  Exitosas:   {resultados['ok']}/{resultados['ok'] + resultados['fail']}")
    print(f"  Fallidas:   {resultados['fail']}")
    print(f"  Tiempo min: {tiempos[0]:.0f}ms")
    print(f"  Tiempo max: {tiempos[-1]:.0f}ms")
    print(f"  Promedio:   {sum(tiempos) / len(tiempos):.0f}ms")
    p95 = tiempos[int(len(tiempos) * 0.95)] if len(tiempos) > 1 else tiempos[0]
    print(f"  P95:        {p95:.0f}ms")


if __name__ == "__main__":
    print("=" * 50)
    print(f"  TEST DE CARGA - {NUM_USUARIOS} usuarios concurrentes")
    print(f"  Servidor: {BASE_URL}")
    print("=" * 50)

    # FASE 1: Login concurrente
    print(f"\n--- FASE 1: {NUM_USUARIOS} logins simultaneos ---\n")
    inicio_total = time.time()

    with ThreadPoolExecutor(max_workers=NUM_USUARIOS) as executor:
        futuros = {executor.submit(hacer_login, i + 1): i for i in range(NUM_USUARIOS)}
        for futuro in as_completed(futuros):
            print(futuro.result())

    tiempo_login = time.time() - inicio_total
    print(f"\n  Tiempo total fase login: {tiempo_login:.2f}s")
    imprimir_resumen("Login", resultados_login)

    # FASE 2: Reservas concurrentes
    if tokens:
        print(f"\n--- FASE 2: {len(tokens)} reservas simultaneas ---\n")
        inicio_total = time.time()

        # Reutilizar tokens ciclicamente si hay menos de NUM_USUARIOS
        tokens_ciclicos = [tokens[i % len(tokens)] for i in range(NUM_USUARIOS)]

        with ThreadPoolExecutor(max_workers=NUM_USUARIOS) as executor:
            futuros = {
                executor.submit(hacer_reserva, i + 1, tokens_ciclicos[i]): i
                for i in range(NUM_USUARIOS)
            }
            for futuro in as_completed(futuros):
                print(futuro.result())

        tiempo_reserva = time.time() - inicio_total
        print(f"\n  Tiempo total fase reservas: {tiempo_reserva:.2f}s")
        imprimir_resumen("Reservas", resultados_reserva)
    else:
        print("\n  No se obtuvieron tokens, saltando fase de reservas.")

    # RESUMEN FINAL
    print("\n" + "=" * 50)
    print("  RESUMEN FINAL")
    print("=" * 50)
    print("\n  LOGIN:")
    imprimir_resumen("Login", resultados_login)
    print("\n  RESERVAS:")
    imprimir_resumen("Reservas", resultados_reserva)
    print()
