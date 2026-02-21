"""
Test de carga - Simula 30 usuarios concurrentes haciendo login + reservas
Incluye pruebas con fechas pasadas para validar que el backend las rechaza (400+)
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

# Datos para la reserva de mesa (fecha FUTURA - deberian funcionar)
RESERVA_TEMPLATE = {
    "idCliente": 210003,
    "idMesa": 1,
    "fechaHoraInicio": "2026-03-01T19:00:00Z",
    "fechaHoraFin": "2026-03-01T21:00:00Z",
    "numPersonas": 2,
    "notas": "Test de carga"
}

# Datos para reservas con fecha PASADA (deberian ser rechazadas con 400)
RESERVA_PASADA_TEMPLATE = {
    "idCliente": 210003,
    "idMesa": 1,
    "fechaHoraInicio": "2024-01-15T10:00:00Z",
    "fechaHoraFin": "2024-01-15T12:00:00Z",
    "numPersonas": 2,
    "notas": "Test fecha pasada - debe fallar"
}

# Reserva con fecha inicio posterior a fecha fin (deberia ser rechazada con 400)
RESERVA_INVERTIDA_TEMPLATE = {
    "idCliente": 210003,
    "idMesa": 1,
    "fechaHoraInicio": "2026-06-15T20:00:00Z",
    "fechaHoraFin": "2026-06-15T18:00:00Z",
    "numPersonas": 2,
    "notas": "Test fechas invertidas - debe fallar"
}
# ========================================

resultados_login = {"ok": 0, "fail": 0, "tiempos": []}
resultados_reserva = {"ok": 0, "fail": 0, "tiempos": []}
resultados_pasadas = {"ok": 0, "fail": 0, "tiempos": [], "codigos": []}
resultados_invertidas = {"ok": 0, "fail": 0, "tiempos": [], "codigos": []}
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


def hacer_reserva_invalida(usuario_num, token, template, resultados, etiqueta):
    """Envia una reserva que DEBE ser rechazada (fecha pasada o invertida)."""
    inicio = time.time()
    try:
        headers = {"Authorization": f"Bearer {token}"}
        resp = requests.post(
            f"{BASE_URL}/api/reservas-mesa",
            json=template,
            headers=headers,
            timeout=30
        )
        duracion = (time.time() - inicio) * 1000

        with lock:
            resultados["tiempos"].append(duracion)
            resultados["codigos"].append(resp.status_code)
            if resp.status_code >= 400:
                resultados["ok"] += 1
                return f"  {etiqueta} {usuario_num:02d}: RECHAZADA {resp.status_code} ({duracion:.0f}ms)"
            else:
                resultados["fail"] += 1
                return f"  {etiqueta} {usuario_num:02d}: ATENCION! Aceptada {resp.status_code} ({duracion:.0f}ms)"
    except Exception as e:
        duracion = (time.time() - inicio) * 1000
        with lock:
            resultados["fail"] += 1
            resultados["tiempos"].append(duracion)
        return f"  {etiqueta} {usuario_num:02d}: ERROR {str(e)[:50]} ({duracion:.0f}ms)"


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

    # FASE 2: Reservas con fechas PASADAS (DEBEN ser rechazadas)
    if tokens:
        print(f"\n--- FASE 2: {NUM_USUARIOS} reservas con FECHA PASADA (deben fallar con 400) ---\n")
        inicio_total = time.time()

        tokens_ciclicos = [tokens[i % len(tokens)] for i in range(NUM_USUARIOS)]

        with ThreadPoolExecutor(max_workers=NUM_USUARIOS) as executor:
            futuros = {
                executor.submit(hacer_reserva_invalida, i + 1, tokens_ciclicos[i],
                                RESERVA_PASADA_TEMPLATE, resultados_pasadas, "Pasada"): i
                for i in range(NUM_USUARIOS)
            }
            for futuro in as_completed(futuros):
                print(futuro.result())

        tiempo_pasadas = time.time() - inicio_total
        print(f"\n  Tiempo total fase fechas pasadas: {tiempo_pasadas:.2f}s")
        rechazadas = resultados_pasadas["ok"]
        total = rechazadas + resultados_pasadas["fail"]
        print(f"  Rechazadas correctamente: {rechazadas}/{total}")
        if resultados_pasadas["fail"] > 0:
            print(f"  !! ATENCION: {resultados_pasadas['fail']} reservas pasadas fueron ACEPTADAS (bug!)")
        else:
            print(f"  VALIDACION OK: Todas las reservas con fecha pasada fueron rechazadas")
    else:
        print("\n  No se obtuvieron tokens, saltando fase de fechas pasadas.")

    # FASE 3: Reservas con fechas INVERTIDAS (inicio > fin, DEBEN ser rechazadas)
    if tokens:
        print(f"\n--- FASE 3: {NUM_USUARIOS} reservas con FECHAS INVERTIDAS (deben fallar con 400) ---\n")
        inicio_total = time.time()

        tokens_ciclicos = [tokens[i % len(tokens)] for i in range(NUM_USUARIOS)]

        with ThreadPoolExecutor(max_workers=NUM_USUARIOS) as executor:
            futuros = {
                executor.submit(hacer_reserva_invalida, i + 1, tokens_ciclicos[i],
                                RESERVA_INVERTIDA_TEMPLATE, resultados_invertidas, "Invertida"): i
                for i in range(NUM_USUARIOS)
            }
            for futuro in as_completed(futuros):
                print(futuro.result())

        tiempo_invertidas = time.time() - inicio_total
        print(f"\n  Tiempo total fase fechas invertidas: {tiempo_invertidas:.2f}s")
        rechazadas = resultados_invertidas["ok"]
        total = rechazadas + resultados_invertidas["fail"]
        print(f"  Rechazadas correctamente: {rechazadas}/{total}")
        if resultados_invertidas["fail"] > 0:
            print(f"  !! ATENCION: {resultados_invertidas['fail']} reservas invertidas fueron ACEPTADAS (bug!)")
        else:
            print(f"  VALIDACION OK: Todas las reservas con fechas invertidas fueron rechazadas")
    else:
        print("\n  No se obtuvieron tokens, saltando fase de fechas invertidas.")

    # FASE 4: Reservas con fechas FUTURAS validas (deben funcionar)
    if tokens:
        print(f"\n--- FASE 4: {NUM_USUARIOS} reservas con FECHA FUTURA valida ---\n")
        inicio_total = time.time()

        tokens_ciclicos = [tokens[i % len(tokens)] for i in range(NUM_USUARIOS)]

        with ThreadPoolExecutor(max_workers=NUM_USUARIOS) as executor:
            futuros = {
                executor.submit(hacer_reserva, i + 1, tokens_ciclicos[i]): i
                for i in range(NUM_USUARIOS)
            }
            for futuro in as_completed(futuros):
                print(futuro.result())

        tiempo_reserva = time.time() - inicio_total
        print(f"\n  Tiempo total fase reservas validas: {tiempo_reserva:.2f}s")
        imprimir_resumen("Reservas validas", resultados_reserva)
    else:
        print("\n  No se obtuvieron tokens, saltando fase de reservas.")

    # RESUMEN FINAL
    print("\n" + "=" * 50)
    print("  RESUMEN FINAL")
    print("=" * 50)
    print("\n  LOGIN:")
    imprimir_resumen("Login", resultados_login)
    print("\n  RESERVAS FECHA PASADA (esperado: todas rechazadas):")
    rechazadas = resultados_pasadas["ok"]
    total_p = rechazadas + resultados_pasadas["fail"]
    print(f"  Rechazadas: {rechazadas}/{total_p}")
    if resultados_pasadas["codigos"]:
        codigos_set = set(resultados_pasadas["codigos"])
        print(f"  Codigos HTTP recibidos: {codigos_set}")
    print("\n  RESERVAS FECHAS INVERTIDAS (esperado: todas rechazadas):")
    rechazadas_i = resultados_invertidas["ok"]
    total_i = rechazadas_i + resultados_invertidas["fail"]
    print(f"  Rechazadas: {rechazadas_i}/{total_i}")
    if resultados_invertidas["codigos"]:
        codigos_set_i = set(resultados_invertidas["codigos"])
        print(f"  Codigos HTTP recibidos: {codigos_set_i}")
    print("\n  RESERVAS VALIDAS:")
    imprimir_resumen("Reservas", resultados_reserva)
    print()
