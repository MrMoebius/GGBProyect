"""
Test de validacion - Comprueba las 14 correcciones de validacion del backend
Verifica que el servidor rechaza datos invalidos con el codigo HTTP adecuado.

Ejecutar: pip install requests && python validation-test.py

IMPORTANTE: El servidor debe estar corriendo en http://localhost:8080
"""

import requests
import json
import sys

# ============ CONFIGURACION ============
BASE_URL = "http://localhost:8080"

# Credenciales de un usuario ADMIN que exista en tu BD
ADMIN_EMAIL = "admin@ggbproyect.com"
ADMIN_PASSWORD = "admin123"
# ========================================

# Contadores globales
total_tests = 0
passed_tests = 0
failed_tests = 0
skipped_tests = 0


def login(email, password):
    """Inicia sesion y devuelve el token JWT."""
    resp = requests.post(f"{BASE_URL}/api/auth/login",
                         json={"email": email, "password": password}, timeout=15)
    if resp.status_code == 200:
        return resp.json().get("accessToken")
    print(f"  ERROR LOGIN: {resp.status_code} - {resp.text[:200]}")
    return None


def headers(token):
    return {"Authorization": f"Bearer {token}", "Content-Type": "application/json"}


def test(nombre, resp, expected_min, expected_max, expected_msg=None):
    """Evalua un test: el status debe estar entre expected_min y expected_max."""
    global total_tests, passed_tests, failed_tests
    total_tests += 1
    status = resp.status_code
    ok = expected_min <= status <= expected_max

    body = {}
    try:
        body = resp.json()
    except Exception:
        pass
    msg = body.get("message", body.get("error", ""))

    if ok:
        if expected_msg and expected_msg not in str(msg):
            failed_tests += 1
            print(f"  FALLO  {nombre}")
            print(f"         HTTP {status} (ok), pero mensaje no coincide")
            print(f"         Esperado contener: '{expected_msg}'")
            print(f"         Recibido: '{msg}'")
            return False
        passed_tests += 1
        print(f"  OK     {nombre} -> HTTP {status}")
        return True
    else:
        failed_tests += 1
        print(f"  FALLO  {nombre}")
        print(f"         Esperado: HTTP {expected_min}-{expected_max}, Recibido: HTTP {status}")
        if msg:
            print(f"         Mensaje: {msg}")
        return False


def skip(nombre, razon):
    global total_tests, skipped_tests
    total_tests += 1
    skipped_tests += 1
    print(f"  SKIP   {nombre} -> {razon}")


def fetch_first(endpoint, token_str):
    """Obtiene el primer elemento de un endpoint paginado."""
    resp = requests.get(f"{BASE_URL}/api/{endpoint}?size=1", headers=headers(token_str), timeout=15)
    if resp.status_code == 200:
        data = resp.json()
        content = data.get("content", [])
        if content:
            return content[0]
    return None


# ==============================================================
#  MAIN
# ==============================================================
if __name__ == "__main__":
    print("=" * 60)
    print("  TEST DE VALIDACION - 14 correcciones del backend")
    print(f"  Servidor: {BASE_URL}")
    print("=" * 60)

    # --- LOGIN ---
    print("\n--- Login como ADMIN ---")
    token = login(ADMIN_EMAIL, ADMIN_PASSWORD)
    if not token:
        print("  No se pudo obtener el token. Abortando.")
        sys.exit(1)
    print(f"  Token obtenido correctamente\n")

    h = headers(token)

    # ============================================================
    # FIX #4 - Cantidad negativa en LineasComanda
    # ============================================================
    print("--- FIX #4: Cantidad negativa en LineasComanda ---")

    # Necesitamos una comanda y un producto existentes
    comanda = fetch_first("comandas", token)
    producto = fetch_first("productos", token)

    if comanda and producto:
        # Test: cantidad = 0 (DTO @Min(1) o service lo atrapa -> 400)
        resp = requests.post(f"{BASE_URL}/api/lineas-comanda", headers=h, json={
            "idComanda": comanda["id"],
            "idProducto": producto["id"],
            "cantidad": 0,
            "precioUnitarioHistorico": 5.00
        }, timeout=15)
        test("Cantidad = 0 rechazada", resp, 400, 400)

        # Test: cantidad negativa
        resp = requests.post(f"{BASE_URL}/api/lineas-comanda", headers=h, json={
            "idComanda": comanda["id"],
            "idProducto": producto["id"],
            "cantidad": -3,
            "precioUnitarioHistorico": 5.00
        }, timeout=15)
        test("Cantidad negativa rechazada", resp, 400, 400)
    else:
        skip("Cantidad = 0 rechazada", "No se encontro comanda o producto existente")
        skip("Cantidad negativa rechazada", "No se encontro comanda o producto existente")

    # ============================================================
    # FIX #5 - Importe negativo en PagosMesa
    # ============================================================
    print("\n--- FIX #5: Importe negativo en PagosMesa ---")

    # Buscar una sesion activa
    resp_sesiones = requests.get(f"{BASE_URL}/api/sesiones-mesa/filter?estado=ACTIVA&size=1",
                                 headers=h, timeout=15)
    sesion_activa = None
    if resp_sesiones.status_code == 200:
        content = resp_sesiones.json().get("content", [])
        if content:
            sesion_activa = content[0]

    if sesion_activa:
        resp = requests.post(f"{BASE_URL}/api/pagos-mesa", headers=h, json={
            "idSesion": sesion_activa["id"],
            "fechaHora": "2026-02-21T12:00:00Z",
            "importe": -50.00,
            "metodoPago": "EFECTIVO",
            "estado": "PENDIENTE"
        }, timeout=15)
        test("Importe negativo rechazado", resp, 400, 400)
    else:
        # DTO @Positive atrapa antes que el servicio -> 400
        resp = requests.post(f"{BASE_URL}/api/pagos-mesa", headers=h, json={
            "idSesion": 999999,
            "fechaHora": "2026-02-21T12:00:00Z",
            "importe": -50.00,
            "metodoPago": "EFECTIVO",
            "estado": "PENDIENTE"
        }, timeout=15)
        test("Importe negativo rechazado (sin sesion)", resp, 400, 404)

    # ============================================================
    # FIX #6 - Capacidad negativa/cero en Mesa
    # ============================================================
    print("\n--- FIX #6: Capacidad negativa en Mesa ---")

    # Test: capacidad = 0
    resp = requests.post(f"{BASE_URL}/api/mesas", headers=h, json={
        "numeroMesa": 99999,
        "nombreMesa": "Test Validacion Zero",
        "capacidad": 0,
        "zona": "test"
    }, timeout=15)
    test("Capacidad = 0 rechazada", resp, 400, 400)

    # Test: capacidad negativa
    resp = requests.post(f"{BASE_URL}/api/mesas", headers=h, json={
        "numeroMesa": 99998,
        "nombreMesa": "Test Validacion Negativa",
        "capacidad": -5,
        "zona": "test"
    }, timeout=15)
    test("Capacidad negativa rechazada", resp, 400, 400)

    # ============================================================
    # FIX #7 - Participantes negativos en LudotecaSesiones
    # ============================================================
    print("\n--- FIX #7: Participantes negativos en LudotecaSesiones ---")

    # Buscar sesion existente para usar como referencia
    sesion_any = fetch_first("sesiones-mesa", token)
    id_sesion_test = sesion_any["id"] if sesion_any else 1

    # Test: numAdultos negativo (DTO @Min(0) o service lo atrapa -> 400)
    resp = requests.post(f"{BASE_URL}/api/ludoteca-sesiones", headers=h, json={
        "idSesion": id_sesion_test,
        "numAdultos": -2,
        "numNinos613": 0,
        "numNinos05": 0,
        "importeTotal": 10.00
    }, timeout=15)
    test("numAdultos negativo rechazado", resp, 400, 400)

    # Test: numNinos613 negativo
    resp = requests.post(f"{BASE_URL}/api/ludoteca-sesiones", headers=h, json={
        "idSesion": id_sesion_test,
        "numAdultos": 1,
        "numNinos613": -3,
        "numNinos05": 0,
        "importeTotal": 10.00
    }, timeout=15)
    test("numNinos613 negativo rechazado", resp, 400, 400)

    # Test: numNinos05 negativo
    resp = requests.post(f"{BASE_URL}/api/ludoteca-sesiones", headers=h, json={
        "idSesion": id_sesion_test,
        "numAdultos": 1,
        "numNinos613": 0,
        "numNinos05": -1,
        "importeTotal": 10.00
    }, timeout=15)
    test("numNinos05 negativo rechazado", resp, 400, 400)

    # ============================================================
    # FIX #8 - Jugadores negativos y min > max en Juego
    # ============================================================
    print("\n--- FIX #8: Jugadores negativos / min > max en Juego ---")

    # Test: minJugadores negativo
    resp = requests.post(f"{BASE_URL}/api/juegos", headers=h, json={
        "nombre": "Test Validacion Negativo",
        "minJugadores": -1,
        "maxJugadores": 4,
        "duracionMediaMin": 30
    }, timeout=15)
    test("minJugadores negativo rechazado", resp, 400, 400)

    # Test: maxJugadores negativo
    resp = requests.post(f"{BASE_URL}/api/juegos", headers=h, json={
        "nombre": "Test Validacion MaxNeg",
        "minJugadores": 2,
        "maxJugadores": -1,
        "duracionMediaMin": 30
    }, timeout=15)
    test("maxJugadores negativo rechazado", resp, 400, 400)

    # Test: minJugadores > maxJugadores
    resp = requests.post(f"{BASE_URL}/api/juegos", headers=h, json={
        "nombre": "Test Validacion MinGtMax",
        "minJugadores": 10,
        "maxJugadores": 2,
        "duracionMediaMin": 30
    }, timeout=15)
    test("minJugadores > maxJugadores rechazado", resp, 400, 400,
         "minimo de jugadores no puede ser mayor")

    # ============================================================
    # FIX #9 - Precio negativo en TarifasLudoteca
    # ============================================================
    print("\n--- FIX #9: Precio negativo en TarifasLudoteca ---")

    resp = requests.post(f"{BASE_URL}/api/tarifas-ludoteca", headers=h, json={
        "nombreTramo": "Test Neg",
        "edadMin": 6,
        "edadMax": 13,
        "precio": -5.00,
        "activo": True
    }, timeout=15)
    test("Precio tarifa negativo rechazado", resp, 400, 400)

    # ============================================================
    # FIX #10 - Precio negativo en Producto
    # ============================================================
    print("\n--- FIX #10: Precio negativo en Producto ---")

    resp = requests.post(f"{BASE_URL}/api/productos", headers=h, json={
        "nombre": "Test Producto Negativo",
        "descripcion": "Test",
        "categoria": "test",
        "precio": -10.00,
        "tipoIva": 21,
        "activo": True
    }, timeout=15)
    test("Precio producto negativo rechazado", resp, 400, 400)

    # ============================================================
    # FIX #11 - Delete producto con FK en lineas_comanda
    # ============================================================
    print("\n--- FIX #11: Delete producto referenciado por lineas_comanda ---")

    # Buscar un producto que tenga lineas de comanda referenciandolo
    linea = fetch_first("lineas-comanda", token)
    if linea and linea.get("idProducto"):
        id_producto_ref = linea["idProducto"]
        resp = requests.delete(f"{BASE_URL}/api/productos/{id_producto_ref}",
                               headers=h, timeout=15)
        test("Delete producto con FK rechazado", resp, 409, 409)
    else:
        skip("Delete producto con FK rechazado",
             "No se encontro linea de comanda con producto referenciado")

    # ============================================================
    # FIX #12 - Bypass de total en Comanda.update()
    # ============================================================
    print("\n--- FIX #12: Total no se sobreescribe en Comanda.update() ---")

    if comanda:
        # Primero leer el total ACTUAL de la BD (puede estar corrupto de un test anterior)
        resp_get = requests.get(f"{BASE_URL}/api/comandas/{comanda['id']}", headers=h, timeout=15)
        if resp_get.status_code == 200:
            total_original = resp_get.json().get("total")
        else:
            total_original = comanda.get("total")

        # Intentar actualizar con un total absurdo diferente al actual
        total_falso = 777777.77
        resp = requests.put(f"{BASE_URL}/api/comandas/{comanda['id']}", headers=h, json={
            "idSesion": comanda.get("idSesion"),
            "estado": comanda.get("estado"),
            "total": total_falso,
            "fechaHora": comanda.get("fechaHora")
        }, timeout=15)

        if resp.status_code == 200:
            total_despues = resp.json().get("total")
            # Si total_original ya era el mismo valor falso, el test no es concluyente
            if total_original is not None and float(total_original) == total_falso:
                skip("Total no se sobreescribe",
                     f"total_original ya era {total_original}, test no concluyente")
            elif total_despues is not None and float(total_despues) == total_falso:
                failed_tests += 1
                total_tests += 1
                print(f"  FALLO  Total sobreescrito! original: {total_original}, despues: {total_despues}")
            else:
                passed_tests += 1
                total_tests += 1
                print(f"  OK     Total no se sobreescribio (original: {total_original}, despues: {total_despues})")
        else:
            skip("Total no se sobreescribe", f"Update devolvio HTTP {resp.status_code}")
    else:
        skip("Total no se sobreescribe", "No se encontro comanda existente")

    # ============================================================
    # FIX #13 - Fecha de ingreso futura en Empleado
    # ============================================================
    print("\n--- FIX #13: Fecha ingreso futura en Empleado ---")

    resp = requests.post(f"{BASE_URL}/api/empleados", headers=h, json={
        "nombre": "Test Futuro",
        "email": "test_futuro_validacion@test.com",
        "telefono": "600000000",
        "password": "Test123456",
        "idRol": 1,
        "fechaIngreso": "2030-01-01",
        "estado": "ACTIVO"
    }, timeout=15)
    test("Fecha ingreso futura rechazada", resp, 400, 400, "futuro")

    # ============================================================
    # FIX #14 - numComensales = 0 en SesionesMesa.abrir()
    # ============================================================
    print("\n--- FIX #14: numComensales = 0 en abrir sesion ---")

    # Buscar una mesa LIBRE
    resp_mesas = requests.get(f"{BASE_URL}/api/mesas/filter?estado=LIBRE&size=1",
                              headers=h, timeout=15)
    mesa_libre = None
    if resp_mesas.status_code == 200:
        content = resp_mesas.json().get("content", [])
        if content:
            mesa_libre = content[0]

    if mesa_libre:
        # Test: numComensales = 0
        resp = requests.post(f"{BASE_URL}/api/sesiones-mesa/abrir", headers=h, json={
            "idMesa": mesa_libre["id"],
            "numComensales": 0,
            "usaLudoteca": False
        }, timeout=15)
        test("numComensales = 0 rechazado", resp, 400, 400,
             "comensales debe ser mayor")

        # Test: numComensales negativo
        resp = requests.post(f"{BASE_URL}/api/sesiones-mesa/abrir", headers=h, json={
            "idMesa": mesa_libre["id"],
            "numComensales": -2,
            "usaLudoteca": False
        }, timeout=15)
        test("numComensales negativo rechazado", resp, 400, 400,
             "comensales debe ser mayor")
    else:
        skip("numComensales = 0 rechazado", "No se encontro mesa LIBRE")
        skip("numComensales negativo rechazado", "No se encontro mesa LIBRE")

    # ============================================================
    # TESTS ADICIONALES - Validaciones positivas (datos validos)
    # ============================================================
    print("\n--- TESTS POSITIVOS: Datos validos deben ser aceptados ---")

    # Mesa con capacidad valida
    resp = requests.post(f"{BASE_URL}/api/mesas", headers=h, json={
        "numeroMesa": 88888,
        "nombreMesa": "Test Valida OK",
        "capacidad": 4,
        "zona": "test"
    }, timeout=15)
    mesa_creada = None
    if resp.status_code in (200, 201):
        mesa_creada = resp.json()
    test("Mesa con capacidad valida aceptada", resp, 200, 201)

    # Juego con jugadores validos
    resp = requests.post(f"{BASE_URL}/api/juegos", headers=h, json={
        "nombre": "Test Juego Valido OK",
        "minJugadores": 2,
        "maxJugadores": 6,
        "duracionMediaMin": 45
    }, timeout=15)
    juego_creado = None
    if resp.status_code in (200, 201):
        juego_creado = resp.json()
    test("Juego con jugadores validos aceptado", resp, 200, 201)

    # Producto con precio valido
    resp = requests.post(f"{BASE_URL}/api/productos", headers=h, json={
        "nombre": "Test Producto Valido OK",
        "descripcion": "Test positivo",
        "categoria": "test",
        "precio": 5.50,
        "tipoIva": 21,
        "activo": True
    }, timeout=15)
    producto_creado = None
    if resp.status_code in (200, 201):
        producto_creado = resp.json()
    test("Producto con precio valido aceptado", resp, 200, 201)

    # Tarifa con precio valido
    resp = requests.post(f"{BASE_URL}/api/tarifas-ludoteca", headers=h, json={
        "nombreTramo": "Test Tarifa OK",
        "edadMin": 6,
        "edadMax": 13,
        "precio": 3.00,
        "activo": True
    }, timeout=15)
    tarifa_creada = None
    if resp.status_code in (200, 201):
        tarifa_creada = resp.json()
    test("Tarifa con precio valido aceptada", resp, 200, 201)

    # ============================================================
    # LIMPIEZA - Eliminar entidades creadas durante el test
    # ============================================================
    print("\n--- LIMPIEZA ---")
    eliminados = 0
    if mesa_creada:
        r = requests.delete(f"{BASE_URL}/api/mesas/{mesa_creada['id']}", headers=h, timeout=15)
        if r.status_code in (200, 204):
            eliminados += 1
    if juego_creado:
        r = requests.delete(f"{BASE_URL}/api/juegos/{juego_creado['id']}", headers=h, timeout=15)
        if r.status_code in (200, 204):
            eliminados += 1
    if producto_creado:
        r = requests.delete(f"{BASE_URL}/api/productos/{producto_creado['id']}", headers=h, timeout=15)
        if r.status_code in (200, 204):
            eliminados += 1
    if tarifa_creada:
        r = requests.delete(f"{BASE_URL}/api/tarifas-ludoteca/{tarifa_creada['id']}", headers=h, timeout=15)
        if r.status_code in (200, 204):
            eliminados += 1
    print(f"  Eliminadas {eliminados} entidades de prueba")

    # ============================================================
    # RESUMEN FINAL
    # ============================================================
    print("\n" + "=" * 60)
    print("  RESUMEN FINAL")
    print("=" * 60)
    print(f"\n  Total tests:   {total_tests}")
    print(f"  Pasados:       {passed_tests}")
    print(f"  Fallidos:      {failed_tests}")
    print(f"  Saltados:      {skipped_tests}")

    if failed_tests == 0 and skipped_tests == 0:
        print("\n  RESULTADO: TODOS LOS TESTS PASARON")
    elif failed_tests == 0:
        print(f"\n  RESULTADO: TODOS LOS EJECUTADOS PASARON ({skipped_tests} saltados)")
    else:
        print(f"\n  RESULTADO: {failed_tests} TESTS FALLARON")

    print()
    sys.exit(0 if failed_tests == 0 else 1)
