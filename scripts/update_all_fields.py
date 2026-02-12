#!/usr/bin/env python3
"""
Combined script to update ALL 5 fields of 330 board games in the GGB database via REST API.
Merges data from update_juegos.py (minJugadores, maxJugadores, duracionMediaMin, idioma)
and update_generos.py (genero - multi-genre comma-separated strings).

Updates per game: minJugadores, maxJugadores, duracionMediaMin, genero, idioma
Preserves existing fields: id, nombre, activo, complejidad, descripcion,
                           recomendadoDosJugadores, ubicacion

Usage:
    python update_all_fields.py

Requires: requests library (pip install requests)
"""

import json
import sys
import time
import requests

# ============================================================================
# CONFIGURATION
# ============================================================================
BASE_URL = "http://localhost:8080"
LOGIN_URL = f"{BASE_URL}/api/auth/login"
JUEGOS_URL = f"{BASE_URL}/api/juegos"
DUMP_FILE = r"C:\Users\di_os\Music\Proyect\juegos_dump.json"

LOGIN_CREDENTIALS = {
    "email": "admin@ggbproyect.com",
    "password": "admin123"
}

# IDs to skip
SKIP_IDS = {2, 90001}

# ============================================================================
# COMBINED GAME DATA DICTIONARY
# Maps game ID -> {minJugadores, maxJugadores, duracionMediaMin, genero, idioma}
#
# - minJugadores, maxJugadores, duracionMediaMin, idioma: from update_juegos.py
# - genero: from update_generos.py (multi-genre comma-separated strings)
# ============================================================================
GAME_DATA = {
    1: {"minJugadores": 3, "maxJugadores": 8, "duracionMediaMin": 20, "genero": "PARTY,CARTAS", "idioma": "ESPANOL"},
    # 2: SKIP
    3: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 15, "genero": "FAMILIAR,CARTAS", "idioma": "ESPANOL"},
    4: {"minJugadores": 2, "maxJugadores": 10, "duracionMediaMin": 25, "genero": "CARTAS,ESTRATEGIA", "idioma": "INDEPENDIENTE"},
    5: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 30, "genero": "CARRERAS,FAMILIAR", "idioma": "ESPANOL"},
    6: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 5, "genero": "COOPERATIVO,CARTAS,ACCION", "idioma": "ESPANOL"},
    7: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 25, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    8: {"minJugadores": 2, "maxJugadores": 7, "duracionMediaMin": 25, "genero": "ESTRATEGIA,FAMILIAR", "idioma": "ESPANOL"},
    9: {"minJugadores": 2, "maxJugadores": 99, "duracionMediaMin": 30, "genero": "DADOS,ESTRATEGIA", "idioma": "INDEPENDIENTE"},
    10: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 30, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    11: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "ESTRATEGIA,PUZZLE", "idioma": "INDEPENDIENTE"},
    12: {"minJugadores": 1, "maxJugadores": 5, "duracionMediaMin": 120, "genero": "ESTRATEGIA,FAMILIAR", "idioma": "ESPANOL"},
    13: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 20, "genero": "CARTAS,FAMILIAR", "idioma": "ESPANOL"},
    14: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "FAMILIAR,COOPERATIVO", "idioma": "INDEPENDIENTE"},
    15: {"minJugadores": 3, "maxJugadores": 8, "duracionMediaMin": 30, "genero": "PARTY,CARTAS", "idioma": "ESPANOL"},
    16: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 90, "genero": "ESTRATEGIA,MINIATURAS", "idioma": "ESPANOL"},
    17: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "AVENTURA,CARTAS", "idioma": "INGLES"},
    18: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 120, "genero": "ESTRATEGIA,AVENTURA", "idioma": "INGLES"},
    19: {"minJugadores": 1, "maxJugadores": 1, "duracionMediaMin": 30, "genero": "MISTERIO,SOLITARIO,CARTAS", "idioma": "ESPANOL"},
    20: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "PARTY,CARTAS", "idioma": "ESPANOL"},
    21: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "MAZOS,CARTAS", "idioma": "ESPANOL"},
    22: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 20, "genero": "FAMILIAR,ESTRATEGIA", "idioma": "ESPANOL"},
    23: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 45, "genero": "ESTRATEGIA,FAMILIAR", "idioma": "ESPANOL"},
    24: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 15, "genero": "FAMILIAR,ESTRATEGIA", "idioma": "ESPANOL"},
    25: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "ESTRATEGIA,CARTAS", "idioma": "INDEPENDIENTE"},
    26: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "PUZZLE,ESTRATEGIA,FAMILIAR", "idioma": "INDEPENDIENTE"},
    27: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 30, "genero": "PUZZLE,ESTRATEGIA", "idioma": "INDEPENDIENTE"},
    28: {"minJugadores": 4, "maxJugadores": 7, "duracionMediaMin": 30, "genero": "CARTAS,ROLESOCULTOS", "idioma": "ESPANOL"},
    29: {"minJugadores": 3, "maxJugadores": 8, "duracionMediaMin": 15, "genero": "DADOS,ROLESOCULTOS", "idioma": "ESPANOL"},
    30: {"minJugadores": 2, "maxJugadores": 10, "duracionMediaMin": 30, "genero": "CARTAS", "idioma": "INDEPENDIENTE"},
    31: {"minJugadores": 2, "maxJugadores": 10, "duracionMediaMin": 30, "genero": "CARTAS", "idioma": "INDEPENDIENTE"},
    32: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "DADOS,PARTY", "idioma": "INDEPENDIENTE"},
    33: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 20, "genero": "PARTY,CARTAS", "idioma": "INGLES"},
    34: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 20, "genero": "CARTAS", "idioma": "INDEPENDIENTE"},
    35: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 30, "genero": "CARRERAS,FAMILIAR", "idioma": "INDEPENDIENTE"},
    36: {"minJugadores": 3, "maxJugadores": 12, "duracionMediaMin": 60, "genero": "MISTERIO,PARTY", "idioma": "ESPANOL"},
    37: {"minJugadores": 3, "maxJugadores": 12, "duracionMediaMin": 60, "genero": "MISTERIO,PARTY", "idioma": "ESPANOL"},
    38: {"minJugadores": 3, "maxJugadores": 12, "duracionMediaMin": 60, "genero": "MISTERIO,PARTY", "idioma": "ESPANOL"},
    39: {"minJugadores": 3, "maxJugadores": 6, "duracionMediaMin": 20, "genero": "PARTY,CARTAS", "idioma": "ESPANOL"},
    40: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 20, "genero": "ESTRATEGIA,PUZZLE,FAMILIAR", "idioma": "INDEPENDIENTE"},
    41: {"minJugadores": 5, "maxJugadores": 20, "duracionMediaMin": 60, "genero": "ROLESOCULTOS,PARTY", "idioma": "ESPANOL"},
    42: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 30, "genero": "CARTAS,FAMILIAR", "idioma": "ESPANOL"},
    43: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 30, "genero": "CARTAS,ESTRATEGIA", "idioma": "ESPANOL"},
    44: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 20, "genero": "FAMILIAR,CARTAS", "idioma": "ESPANOL"},
    45: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 35, "genero": "ESTRATEGIA,FAMILIAR", "idioma": "ESPANOL"},
    46: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 35, "genero": "ESTRATEGIA,FAMILIAR", "idioma": "ESPANOL"},
    47: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 45, "genero": "FAMILIAR,PUZZLE,ESTRATEGIA", "idioma": "ESPANOL"},
    48: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 90, "genero": "ESTRATEGIA,DADOS", "idioma": "ESPANOL"},
    49: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 25, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    50: {"minJugadores": 3, "maxJugadores": 4, "duracionMediaMin": 75, "genero": "ESTRATEGIA,FAMILIAR", "idioma": "ESPANOL"},
    51: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 75, "genero": "ESTRATEGIA,FAMILIAR", "idioma": "ESPANOL"},
    52: {"minJugadores": 1, "maxJugadores": 7, "duracionMediaMin": 120, "genero": "ESTRATEGIA", "idioma": "ESPANOL"},
    53: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 45, "genero": "ESTRATEGIA,CARTAS", "idioma": "INDEPENDIENTE"},
    54: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 60, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    55: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 20, "genero": "FAMILIAR,PUZZLE", "idioma": "INDEPENDIENTE"},
    56: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 30, "genero": "ESTRATEGIA,CARTAS,ROLESOCULTOS", "idioma": "ESPANOL"},
    57: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 30, "genero": "ESTRATEGIA,CARTAS,ROLESOCULTOS", "idioma": "ESPANOL"},
    58: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 60, "genero": "AVENTURA,MAZOS", "idioma": "ESPANOL"},
    59: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "CARTAS,FAMILIAR", "idioma": "INDEPENDIENTE"},
    60: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 90, "genero": "ESTRATEGIA,AVENTURA", "idioma": "ESPANOL"},
    61: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 25, "genero": "CARTAS,ESTRATEGIA", "idioma": "ESPANOL"},
    62: {"minJugadores": 4, "maxJugadores": 8, "duracionMediaMin": 15, "genero": "PARTY,COOPERATIVO", "idioma": "ESPANOL"},
    63: {"minJugadores": 4, "maxJugadores": 8, "duracionMediaMin": 15, "genero": "PARTY,COOPERATIVO", "idioma": "ESPANOL"},
    64: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 15, "genero": "PARTY,COOPERATIVO", "idioma": "ESPANOL"},
    65: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 40, "genero": "ACCION,ESTRATEGIA,FAMILIAR", "idioma": "ESPANOL"},
    66: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 15, "genero": "FAMILIAR,CARTAS", "idioma": "INDEPENDIENTE"},
    67: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 20, "genero": "PARTY,CARTAS", "idioma": "ESPANOL"},
    68: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "PARTY,ACCION", "idioma": "ESPANOL"},
    69: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "ROLESOCULTOS,CARTAS", "idioma": "ESPANOL"},
    70: {"minJugadores": 1, "maxJugadores": 5, "duracionMediaMin": 120, "genero": "ESTRATEGIA,COOPERATIVO", "idioma": "ESPANOL"},
    71: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 60, "genero": "ESTRATEGIA", "idioma": "ESPANOL"},
    72: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 20, "genero": "CARTAS,FAMILIAR", "idioma": "INDEPENDIENTE"},
    73: {"minJugadores": 3, "maxJugadores": 6, "duracionMediaMin": 20, "genero": "PARTY,CARTAS", "idioma": "ESPANOL"},
    74: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 90, "genero": "ESTRATEGIA,MINIATURAS", "idioma": "ESPANOL"},
    75: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "DADOS", "idioma": "INDEPENDIENTE"},
    76: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 20, "genero": "ESTRATEGIA", "idioma": "INDEPENDIENTE"},
    77: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 20, "genero": "PARTY,ACCION", "idioma": "ESPANOL"},
    78: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 120, "genero": "ESTRATEGIA", "idioma": "ESPANOL"},
    79: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "CARTAS", "idioma": "INDEPENDIENTE"},
    80: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "FAMILIAR,COOPERATIVO", "idioma": "ESPANOL"},
    81: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 60, "genero": "ESTRATEGIA,DADOS", "idioma": "ESPANOL"},
    82: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 30, "genero": "DADOS,ESTRATEGIA", "idioma": "ESPANOL"},
    83: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 30, "genero": "DADOS,ESTRATEGIA", "idioma": "ESPANOL"},
    84: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 30, "genero": "DADOS,ESTRATEGIA", "idioma": "ESPANOL"},
    85: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 30, "genero": "DADOS,ESTRATEGIA", "idioma": "ESPANOL"},
    86: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 15, "genero": "INFANTIL,FAMILIAR", "idioma": "ESPANOL"},
    87: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 60, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    88: {"minJugadores": 3, "maxJugadores": 8, "duracionMediaMin": 30, "genero": "PARTY,FAMILIAR", "idioma": "ESPANOL"},
    89: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 10, "genero": "PARTY,ACCION", "idioma": "INDEPENDIENTE"},
    90: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 10, "genero": "PARTY,ACCION", "idioma": "INDEPENDIENTE"},
    91: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 10, "genero": "PARTY,ACCION", "idioma": "INDEPENDIENTE"},
    92: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "MAZOS,ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    93: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 20, "genero": "FAMILIAR", "idioma": "INDEPENDIENTE"},
    94: {"minJugadores": 1, "maxJugadores": 6, "duracionMediaMin": 30, "genero": "PUZZLE,COOPERATIVO", "idioma": "INDEPENDIENTE"},
    95: {"minJugadores": 2, "maxJugadores": 10, "duracionMediaMin": 15, "genero": "CARTAS,FAMILIAR", "idioma": "INDEPENDIENTE"},
    96: {"minJugadores": 1, "maxJugadores": 6, "duracionMediaMin": 45, "genero": "COOPERATIVO,ACCION,DADOS", "idioma": "ESPANOL"},
    97: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 20, "genero": "CARTAS,AVENTURA", "idioma": "ESPANOL"},
    98: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 15, "genero": "DADOS,AVENTURA", "idioma": "ESPANOL"},
    99: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 60, "genero": "ROL,AVENTURA", "idioma": "ESPANOL"},
    100: {"minJugadores": 3, "maxJugadores": 8, "duracionMediaMin": 20, "genero": "PARTY,ROLESOCULTOS", "idioma": "ESPANOL"},
    101: {"minJugadores": 3, "maxJugadores": 8, "duracionMediaMin": 15, "genero": "PARTY,ROLESOCULTOS", "idioma": "ESPANOL"},
    102: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "CARTAS,COOPERATIVO", "idioma": "ESPANOL"},
    103: {"minJugadores": 1, "maxJugadores": 2, "duracionMediaMin": 30, "genero": "MISTERIO,COOPERATIVO", "idioma": "ESPANOL"},
    104: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "FAMILIAR,ESTRATEGIA", "idioma": "ESPANOL"},
    105: {"minJugadores": 2, "maxJugadores": 14, "duracionMediaMin": 30, "genero": "PARTY", "idioma": "ESPANOL"},
    106: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "CARTAS,FAMILIAR", "idioma": "INDEPENDIENTE"},
    107: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 20, "genero": "PARTY", "idioma": "ESPANOL"},
    108: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 15, "genero": "CARTAS,PARTY", "idioma": "ESPANOL"},
    109: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 25, "genero": "CARTAS,ESTRATEGIA", "idioma": "ESPANOL"},
    110: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 25, "genero": "CARTAS,ESTRATEGIA", "idioma": "ESPANOL"},
    111: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 45, "genero": "ESTRATEGIA", "idioma": "ESPANOL"},
    112: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 20, "genero": "PARTY,CARTAS", "idioma": "ESPANOL"},
    113: {"minJugadores": 3, "maxJugadores": 8, "duracionMediaMin": 30, "genero": "PARTY,CARTAS", "idioma": "INGLES"},
    114: {"minJugadores": 1, "maxJugadores": 5, "duracionMediaMin": 60, "genero": "ESTRATEGIA,FAMILIAR", "idioma": "ESPANOL"},
    115: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    116: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "PARTY", "idioma": "ESPANOL"},
    117: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 60, "genero": "AVENTURA,ACCION", "idioma": "ESPANOL"},
    118: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 20, "genero": "PARTY,CARTAS", "idioma": "ESPANOL"},
    119: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 60, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    120: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 90, "genero": "ESTRATEGIA", "idioma": "ESPANOL"},
    121: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 25, "genero": "COOPERATIVO,CARTAS", "idioma": "INDEPENDIENTE"},
    122: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 15, "genero": "FAMILIAR,CARTAS", "idioma": "INDEPENDIENTE"},
    123: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "PUZZLE,ESTRATEGIA", "idioma": "INDEPENDIENTE"},
    124: {"minJugadores": 1, "maxJugadores": 6, "duracionMediaMin": 60, "genero": "CARRERAS,ESTRATEGIA", "idioma": "ESPANOL"},
    125: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "COOPERATIVO,MAZOS", "idioma": "ESPANOL"},
    126: {"minJugadores": 3, "maxJugadores": 5, "duracionMediaMin": 15, "genero": "CARTAS,ESTRATEGIA", "idioma": "ESPANOL"},
    127: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "CARTAS,FAMILIAR", "idioma": "INDEPENDIENTE"},
    128: {"minJugadores": 2, "maxJugadores": 10, "duracionMediaMin": 30, "genero": "PARTY,FAMILIAR", "idioma": "ESPANOL"},
    129: {"minJugadores": 1, "maxJugadores": 10, "duracionMediaMin": 30, "genero": "PARTY,FAMILIAR", "idioma": "ESPANOL"},
    130: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "FAMILIAR", "idioma": "ESPANOL"},
    131: {"minJugadores": 8, "maxJugadores": 18, "duracionMediaMin": 30, "genero": "ROLESOCULTOS,PARTY", "idioma": "ESPANOL"},
    132: {"minJugadores": 3, "maxJugadores": 10, "duracionMediaMin": 15, "genero": "PARTY,CARTAS", "idioma": "ESPANOL"},
    133: {"minJugadores": 3, "maxJugadores": 6, "duracionMediaMin": 30, "genero": "PARTY", "idioma": "ESPANOL"},
    134: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "FAMILIAR", "idioma": "ESPANOL"},
    135: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 20, "genero": "DADOS,PARTY", "idioma": "ESPANOL"},
    136: {"minJugadores": 1, "maxJugadores": 5, "duracionMediaMin": 45, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    137: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 15, "genero": "PARTY,CARTAS", "idioma": "ESPANOL"},
    138: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 60, "genero": "AVENTURA,CARRERAS", "idioma": "ESPANOL"},
    139: {"minJugadores": 1, "maxJugadores": 8, "duracionMediaMin": 15, "genero": "ACCION,FAMILIAR", "idioma": "INDEPENDIENTE"},
    140: {"minJugadores": 1, "maxJugadores": 8, "duracionMediaMin": 15, "genero": "ACCION,FAMILIAR", "idioma": "INDEPENDIENTE"},
    141: {"minJugadores": 1, "maxJugadores": 8, "duracionMediaMin": 15, "genero": "ACCION,FAMILIAR", "idioma": "INDEPENDIENTE"},
    142: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 15, "genero": "CARTAS,FAMILIAR", "idioma": "INDEPENDIENTE"},
    143: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 20, "genero": "ACCION,PARTY", "idioma": "ESPANOL"},
    144: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "CARTAS,ESTRATEGIA", "idioma": "ESPANOL"},
    145: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 30, "genero": "FAMILIAR,DADOS,ACCION", "idioma": "ESPANOL"},
    146: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "ESTRATEGIA,COOPERATIVO", "idioma": "ESPANOL"},
    147: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "COOPERATIVO,AVENTURA", "idioma": "ESPANOL"},
    148: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 20, "genero": "COOPERATIVO,CARTAS", "idioma": "ESPANOL"},
    149: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 20, "genero": "COOPERATIVO,CARTAS", "idioma": "ESPANOL"},
    150: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "FAMILIAR,ESTRATEGIA", "idioma": "INDEPENDIENTE"},
    151: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 20, "genero": "DADOS,PARTY", "idioma": "INDEPENDIENTE"},
    152: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    153: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 20, "genero": "PARTY", "idioma": "ESPANOL"},
    154: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 20, "genero": "PARTY", "idioma": "ESPANOL"},
    155: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 20, "genero": "INFANTIL,PARTY", "idioma": "ESPANOL"},
    156: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 20, "genero": "PARTY", "idioma": "ESPANOL"},
    157: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 20, "genero": "PARTY,CARTAS", "idioma": "ESPANOL"},
    158: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 20, "genero": "PARTY,CARTAS", "idioma": "ESPANOL"},
    159: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "FAMILIAR,DADOS", "idioma": "ESPANOL"},
    160: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "CARTAS,ROLESOCULTOS", "idioma": "ESPANOL"},
    161: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "CARTAS,ROLESOCULTOS", "idioma": "ESPANOL"},
    162: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 20, "genero": "COOPERATIVO,CARTAS", "idioma": "ESPANOL"},
    163: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 45, "genero": "COOPERATIVO,ESTRATEGIA", "idioma": "ESPANOL"},
    164: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 45, "genero": "COOPERATIVO,ESTRATEGIA", "idioma": "ESPANOL"},
    165: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 30, "genero": "MAZOS,ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    166: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 30, "genero": "MAZOS,ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    167: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 20, "genero": "PARTY", "idioma": "ESPANOL"},
    168: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "FAMILIAR,ESTRATEGIA", "idioma": "ESPANOL"},
    169: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    170: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 15, "genero": "CARTAS,FAMILIAR", "idioma": "ESPANOL"},
    171: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 40, "genero": "COOPERATIVO,MINIATURAS", "idioma": "ESPANOL"},
    172: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 50, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    173: {"minJugadores": 1, "maxJugadores": 6, "duracionMediaMin": 60, "genero": "MINIATURAS,COOPERATIVO", "idioma": "ESPANOL"},
    174: {"minJugadores": 1, "maxJugadores": 1, "duracionMediaMin": 10, "genero": "PUZZLE,SOLITARIO", "idioma": "INDEPENDIENTE"},
    175: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 20, "genero": "PARTY,ACCION", "idioma": "ESPANOL"},
    176: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 20, "genero": "FAMILIAR,PUZZLE", "idioma": "INDEPENDIENTE"},
    177: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 25, "genero": "CARTAS,ESTRATEGIA", "idioma": "ESPANOL"},
    178: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 20, "genero": "CARTAS,ESTRATEGIA", "idioma": "INDEPENDIENTE"},
    179: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 45, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    180: {"minJugadores": 1, "maxJugadores": 2, "duracionMediaMin": 30, "genero": "AVENTURA,SOLITARIO,DADOS", "idioma": "ESPANOL"},
    181: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "DADOS,PARTY", "idioma": "INDEPENDIENTE"},
    182: {"minJugadores": 3, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "PARTY,ROLESOCULTOS", "idioma": "ESPANOL"},
    183: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 15, "genero": "PARTY", "idioma": "ESPANOL"},
    184: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 30, "genero": "FAMILIAR,DADOS", "idioma": "ESPANOL"},
    185: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    186: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 20, "genero": "CARTAS,ESTRATEGIA", "idioma": "INDEPENDIENTE"},
    187: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "ESTRATEGIA,MINIATURAS", "idioma": "ESPANOL"},
    188: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 90, "genero": "FAMILIAR", "idioma": "ESPANOL"},
    189: {"minJugadores": 2, "maxJugadores": 7, "duracionMediaMin": 60, "genero": "FAMILIAR", "idioma": "ESPANOL"},
    190: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 20, "genero": "FAMILIAR", "idioma": "ESPANOL"},
    191: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 90, "genero": "AVENTURA,ESTRATEGIA", "idioma": "ESPANOL"},
    192: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 15, "genero": "FAMILIAR,CARTAS", "idioma": "ESPANOL"},
    193: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 90, "genero": "AVENTURA,ESTRATEGIA,DADOS", "idioma": "ESPANOL"},
    194: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 20, "genero": "COOPERATIVO,TERROR", "idioma": "ESPANOL"},
    195: {"minJugadores": 2, "maxJugadores": 7, "duracionMediaMin": 45, "genero": "COOPERATIVO,ROLESOCULTOS", "idioma": "ESPANOL"},
    196: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 90, "genero": "ESTRATEGIA", "idioma": "ESPANOL"},
    197: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "FAMILIAR,CARTAS", "idioma": "ESPANOL"},
    198: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "FAMILIAR,CARTAS", "idioma": "ESPANOL"},
    199: {"minJugadores": 3, "maxJugadores": 6, "duracionMediaMin": 30, "genero": "ROLESOCULTOS,CARTAS", "idioma": "ESPANOL"},
    200: {"minJugadores": 1, "maxJugadores": 2, "duracionMediaMin": 45, "genero": "COOPERATIVO,DADOS,AVENTURA", "idioma": "ESPANOL"},
    201: {"minJugadores": 1, "maxJugadores": 2, "duracionMediaMin": 45, "genero": "COOPERATIVO,DADOS,AVENTURA", "idioma": "ESPANOL"},
    202: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "ESTRATEGIA", "idioma": "INDEPENDIENTE"},
    203: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 15, "genero": "CARTAS,FAMILIAR", "idioma": "INDEPENDIENTE"},
    204: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 45, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    205: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 60, "genero": "ESTRATEGIA", "idioma": "ESPANOL"},
    206: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 45, "genero": "COOPERATIVO,ESTRATEGIA", "idioma": "ESPANOL"},
    207: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "FAMILIAR", "idioma": "INDEPENDIENTE"},
    208: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 30, "genero": "ESTRATEGIA,PUZZLE", "idioma": "INDEPENDIENTE"},
    209: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 20, "genero": "PUZZLE,ESTRATEGIA", "idioma": "INDEPENDIENTE"},
    210: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "INFANTIL,PARTY", "idioma": "ESPANOL"},
    211: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 15, "genero": "CARTAS,FAMILIAR", "idioma": "ESPANOL"},
    212: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 15, "genero": "CARTAS,FAMILIAR", "idioma": "ESPANOL"},
    213: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 15, "genero": "CARTAS,FAMILIAR", "idioma": "ESPANOL"},
    214: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 20, "genero": "DADOS,FAMILIAR", "idioma": "INDEPENDIENTE"},
    215: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 15, "genero": "DADOS,PARTY", "idioma": "ESPANOL"},
    216: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 30, "genero": "CARTAS,ESTRATEGIA", "idioma": "ESPANOL"},
    217: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 30, "genero": "CARTAS,FAMILIAR", "idioma": "ESPANOL"},
    218: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 30, "genero": "CARTAS,FAMILIAR", "idioma": "ESPANOL"},
    219: {"minJugadores": 3, "maxJugadores": 5, "duracionMediaMin": 20, "genero": "CARTAS,MISTERIO", "idioma": "ESPANOL"},
    220: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 20, "genero": "CARTAS,ESTRATEGIA", "idioma": "ESPANOL"},
    221: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    222: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 20, "genero": "MAZOS,CARTAS", "idioma": "ESPANOL"},
    223: {"minJugadores": 3, "maxJugadores": 8, "duracionMediaMin": 30, "genero": "PARTY,CARTAS", "idioma": "ESPANOL"},
    224: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 20, "genero": "CARTAS", "idioma": "ESPANOL"},
    225: {"minJugadores": 2, "maxJugadores": 10, "duracionMediaMin": 30, "genero": "CARTAS", "idioma": "INDEPENDIENTE"},
    226: {"minJugadores": 3, "maxJugadores": 5, "duracionMediaMin": 20, "genero": "CARTAS,PARTY", "idioma": "ESPANOL"},
    227: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 20, "genero": "PARTY,FAMILIAR", "idioma": "ESPANOL"},
    228: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 30, "genero": "CARTAS,ESTRATEGIA", "idioma": "ESPANOL"},
    229: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 30, "genero": "ESTRATEGIA,CARTAS", "idioma": "INGLES"},
    230: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 20, "genero": "CARTAS,PARTY", "idioma": "ESPANOL"},
    231: {"minJugadores": 1, "maxJugadores": 1, "duracionMediaMin": 20, "genero": "SOLITARIO,CARTAS", "idioma": "INGLES"},
    232: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 30, "genero": "CARTAS,FAMILIAR", "idioma": "ESPANOL"},
    233: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 10, "genero": "CARTAS,ESTRATEGIA", "idioma": "ESPANOL"},
    234: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 60, "genero": "ESTRATEGIA", "idioma": "ESPANOL"},
    235: {"minJugadores": 1, "maxJugadores": 6, "duracionMediaMin": 30, "genero": "FAMILIAR,DADOS", "idioma": "ESPANOL"},
    236: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 60, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    237: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 10, "genero": "INFANTIL,ACCION", "idioma": "INDEPENDIENTE"},
    238: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 30, "genero": "FAMILIAR,ESTRATEGIA", "idioma": "ESPANOL"},
    239: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 90, "genero": "ESTRATEGIA", "idioma": "ESPANOL"},
    240: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 60, "genero": "MINIATURAS,ESTRATEGIA", "idioma": "ESPANOL"},
    241: {"minJugadores": 1, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "DADOS,FAMILIAR", "idioma": "INDEPENDIENTE"},
    242: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "CARTAS", "idioma": "INDEPENDIENTE"},
    243: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 30, "genero": "FAMILIAR,CARRERAS", "idioma": "INGLES"},
    244: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 30, "genero": "FAMILIAR,ESTRATEGIA", "idioma": "ESPANOL"},
    245: {"minJugadores": 3, "maxJugadores": 10, "duracionMediaMin": 30, "genero": "ROLESOCULTOS,CARTAS", "idioma": "ESPANOL"},
    246: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "PUZZLE,DADOS", "idioma": "INDEPENDIENTE"},
    247: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 20, "genero": "ESTRATEGIA", "idioma": "ESPANOL"},
    248: {"minJugadores": 3, "maxJugadores": 6, "duracionMediaMin": 45, "genero": "COOPERATIVO,ESTRATEGIA,MISTERIO", "idioma": "ESPANOL"},
    249: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 20, "genero": "PARTY", "idioma": "ESPANOL"},
    250: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 15, "genero": "INFANTIL,FAMILIAR", "idioma": "ESPANOL"},
    251: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 15, "genero": "FAMILIAR,ESTRATEGIA", "idioma": "ESPANOL"},
    252: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 30, "genero": "COOPERATIVO,MISTERIO", "idioma": "ESPANOL"},
    253: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 15, "genero": "CARTAS,PARTY", "idioma": "ESPANOL"},
    254: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 30, "genero": "CARTAS,ESTRATEGIA", "idioma": "ESPANOL"},
    255: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 15, "genero": "ACCION,COOPERATIVO", "idioma": "INDEPENDIENTE"},
    256: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 20, "genero": "PARTY", "idioma": "ESPANOL"},
    257: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 20, "genero": "PARTY", "idioma": "ESPANOL"},
    258: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 20, "genero": "PARTY", "idioma": "ESPANOL"},
    259: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 20, "genero": "PARTY", "idioma": "ESPANOL"},
    260: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 5, "genero": "ACCION,FAMILIAR", "idioma": "INDEPENDIENTE"},
    261: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 30, "genero": "ESTRATEGIA", "idioma": "INDEPENDIENTE"},
    262: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "ESTRATEGIA,FAMILIAR", "idioma": "ESPANOL"},
    263: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 15, "genero": "PARTY,CARTAS", "idioma": "ESPANOL"},
    264: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "CARTAS,AVENTURA", "idioma": "ESPANOL"},
    265: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 30, "genero": "MAZOS,ESTRATEGIA", "idioma": "ESPANOL"},
    266: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 25, "genero": "FAMILIAR,COOPERATIVO", "idioma": "ESPANOL"},
    267: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 10, "genero": "DADOS,FAMILIAR", "idioma": "ESPANOL"},
    268: {"minJugadores": 3, "maxJugadores": 6, "duracionMediaMin": 30, "genero": "CARTAS,ESTRATEGIA", "idioma": "INGLES"},
    269: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 25, "genero": "FAMILIAR,PUZZLE", "idioma": "INDEPENDIENTE"},
    270: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 15, "genero": "INFANTIL", "idioma": "ESPANOL"},
    271: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "FAMILIAR,ESTRATEGIA", "idioma": "ESPANOL"},
    272: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 30, "genero": "CARTAS,FAMILIAR,PARTY", "idioma": "ESPANOL"},
    273: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 10, "genero": "PARTY,ACCION", "idioma": "ESPANOL"},
    274: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 10, "genero": "PARTY,ACCION", "idioma": "ESPANOL"},
    275: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 90, "genero": "AVENTURA,ROL", "idioma": "ESPANOL"},
    276: {"minJugadores": 1, "maxJugadores": 5, "duracionMediaMin": 20, "genero": "CARTAS,FAMILIAR", "idioma": "ESPANOL"},
    277: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "DADOS,ESTRATEGIA", "idioma": "ESPANOL"},
    278: {"minJugadores": 1, "maxJugadores": 1, "duracionMediaMin": 20, "genero": "SOLITARIO,ESTRATEGIA", "idioma": "ESPANOL"},
    279: {"minJugadores": 3, "maxJugadores": 8, "duracionMediaMin": 15, "genero": "PARTY,ROLESOCULTOS", "idioma": "ESPANOL"},
    280: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 15, "genero": "COOPERATIVO,CARTAS", "idioma": "INDEPENDIENTE"},
    281: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 80, "genero": "ESTRATEGIA", "idioma": "ESPANOL"},
    282: {"minJugadores": 1, "maxJugadores": 6, "duracionMediaMin": 120, "genero": "COOPERATIVO,AVENTURA,ESTRATEGIA", "idioma": "ESPANOL"},
    283: {"minJugadores": 3, "maxJugadores": 10, "duracionMediaMin": 60, "genero": "CARTAS,PARTY", "idioma": "INDEPENDIENTE"},
    284: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 20, "genero": "PARTY", "idioma": "ESPANOL"},
    285: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 15, "genero": "PARTY,FAMILIAR", "idioma": "ESPANOL"},
    286: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 15, "genero": "PARTY", "idioma": "ESPANOL"},
    287: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 15, "genero": "ACCION,PARTY", "idioma": "ESPANOL"},
    288: {"minJugadores": 2, "maxJugadores": 8, "duracionMediaMin": 15, "genero": "PARTY,CARTAS", "idioma": "ESPANOL"},
    289: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 20, "genero": "ACCION,ESTRATEGIA", "idioma": "ESPANOL"},
    290: {"minJugadores": 3, "maxJugadores": 6, "duracionMediaMin": 30, "genero": "MISTERIO,PARTY", "idioma": "ESPANOL"},
    291: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "CARTAS,FAMILIAR", "idioma": "INDEPENDIENTE"},
    292: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 20, "genero": "FAMILIAR,ESTRATEGIA", "idioma": "ESPANOL"},
    293: {"minJugadores": 3, "maxJugadores": 8, "duracionMediaMin": 15, "genero": "PARTY", "idioma": "ESPANOL"},
    294: {"minJugadores": 3, "maxJugadores": 8, "duracionMediaMin": 15, "genero": "PARTY", "idioma": "ESPANOL"},
    295: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    296: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "MINIATURAS,ESTRATEGIA", "idioma": "ESPANOL"},
    297: {"minJugadores": 2, "maxJugadores": 10, "duracionMediaMin": 15, "genero": "CARTAS,FAMILIAR", "idioma": "INDEPENDIENTE"},
    298: {"minJugadores": 2, "maxJugadores": 10, "duracionMediaMin": 15, "genero": "CARTAS,FAMILIAR", "idioma": "INDEPENDIENTE"},
    299: {"minJugadores": 2, "maxJugadores": 10, "duracionMediaMin": 15, "genero": "CARTAS,FAMILIAR", "idioma": "INDEPENDIENTE"},
    300: {"minJugadores": 2, "maxJugadores": 10, "duracionMediaMin": 15, "genero": "CARTAS,FAMILIAR", "idioma": "INDEPENDIENTE"},
    301: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "ESTRATEGIA", "idioma": "INGLES"},
    302: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "PARTY", "idioma": "INDEPENDIENTE"},
    303: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "CARTAS,ESTRATEGIA", "idioma": "ESPANOL"},
    304: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 30, "genero": "CARTAS", "idioma": "INDEPENDIENTE"},
    305: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 30, "genero": "PARTY", "idioma": "ESPANOL"},
    306: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 20, "genero": "CARTAS,PARTY", "idioma": "ESPANOL"},
    307: {"minJugadores": 1, "maxJugadores": 4, "duracionMediaMin": 20, "genero": "DADOS,FAMILIAR", "idioma": "ESPANOL"},
    308: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 20, "genero": "CARTAS,PARTY", "idioma": "ESPANOL"},
    309: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 45, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    310: {"minJugadores": 1, "maxJugadores": 100, "duracionMediaMin": 25, "genero": "FAMILIAR,ESTRATEGIA", "idioma": "ESPANOL"},
    311: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "ROLESOCULTOS,PARTY", "idioma": "ESPANOL"},
    312: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 30, "genero": "FAMILIAR,ESTRATEGIA", "idioma": "ESPANOL"},
    313: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 90, "genero": "ESTRATEGIA", "idioma": "INGLES"},
    314: {"minJugadores": 1, "maxJugadores": 5, "duracionMediaMin": 30, "genero": "CARTAS,ESTRATEGIA", "idioma": "ESPANOL"},
    315: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 20, "genero": "FAMILIAR,PUZZLE", "idioma": "INDEPENDIENTE"},
    316: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 15, "genero": "ACCION,PARTY", "idioma": "INDEPENDIENTE"},
    317: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 30, "genero": "FAMILIAR,CARTAS", "idioma": "ESPANOL"},
    318: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 20, "genero": "FAMILIAR,COOPERATIVO", "idioma": "INDEPENDIENTE"},
    319: {"minJugadores": 3, "maxJugadores": 10, "duracionMediaMin": 15, "genero": "PARTY,FAMILIAR", "idioma": "INDEPENDIENTE"},
    320: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 15, "genero": "CARTAS,PARTY", "idioma": "ESPANOL"},
    321: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 30, "genero": "DADOS,ESTRATEGIA", "idioma": "ESPANOL"},
    322: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "CARTAS,FAMILIAR", "idioma": "ESPANOL"},
    323: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 15, "genero": "FAMILIAR,INFANTIL", "idioma": "INDEPENDIENTE"},
    324: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 25, "genero": "FAMILIAR,ESTRATEGIA", "idioma": "ESPANOL"},
    325: {"minJugadores": 3, "maxJugadores": 6, "duracionMediaMin": 60, "genero": "AVENTURA,CARTAS,PARTY", "idioma": "ESPANOL"},
    326: {"minJugadores": 2, "maxJugadores": 2, "duracionMediaMin": 30, "genero": "ESTRATEGIA,CARTAS", "idioma": "ESPANOL"},
    327: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 30, "genero": "DADOS,ESTRATEGIA", "idioma": "INDEPENDIENTE"},
    328: {"minJugadores": 2, "maxJugadores": 5, "duracionMediaMin": 30, "genero": "FAMILIAR,ESTRATEGIA", "idioma": "ESPANOL"},
    329: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 30, "genero": "MISTERIO,COOPERATIVO", "idioma": "ESPANOL"},
    330: {"minJugadores": 2, "maxJugadores": 4, "duracionMediaMin": 20, "genero": "FAMILIAR,ACCION", "idioma": "INDEPENDIENTE"},
    331: {"minJugadores": 2, "maxJugadores": 6, "duracionMediaMin": 15, "genero": "INFANTIL,ACCION", "idioma": "ESPANOL"},
}

# ============================================================================
# MAIN SCRIPT
# ============================================================================

def main():
    # Load the existing game data from the dump
    print(f"Loading game data from {DUMP_FILE}...")
    try:
        with open(DUMP_FILE, "r", encoding="utf-8") as f:
            games = json.load(f)
    except FileNotFoundError:
        print(f"ERROR: File not found: {DUMP_FILE}")
        sys.exit(1)
    except json.JSONDecodeError as e:
        print(f"ERROR: Invalid JSON in {DUMP_FILE}: {e}")
        sys.exit(1)

    print(f"Loaded {len(games)} games from dump file.")

    # Build lookup by ID
    games_by_id = {g["id"]: g for g in games}

    # ---- Step 1: Login to get JWT token ----
    print(f"\nLogging in to {LOGIN_URL}...")
    try:
        resp = requests.post(LOGIN_URL, json=LOGIN_CREDENTIALS, timeout=10)
        resp.raise_for_status()
    except requests.exceptions.ConnectionError:
        print("ERROR: Cannot connect to the server. Is it running on localhost:8080?")
        sys.exit(1)
    except requests.exceptions.HTTPError as e:
        print(f"ERROR: Login failed with status {resp.status_code}: {resp.text}")
        sys.exit(1)

    login_data = resp.json()
    token = login_data.get("token") or login_data.get("accessToken") or login_data.get("jwt")
    if not token:
        print(f"WARNING: Could not find token key. Full login response: {login_data}")
        for k, v in login_data.items():
            if isinstance(v, str) and len(v) > 20:
                token = v
                print(f"  Using key '{k}' as token.")
                break
        if not token:
            print("ERROR: No token found in login response.")
            sys.exit(1)

    print(f"Login successful! Token: {token[:30]}...")

    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }

    # ---- Step 2: Update all 5 fields for each game ----
    success_count = 0
    error_count = 0
    skip_count = 0
    not_found_count = 0
    total = len(GAME_DATA)

    print(f"\nUpdating all 5 fields for {total} games...\n")

    for i, (game_id, new_data) in enumerate(GAME_DATA.items(), 1):
        if game_id in SKIP_IDS:
            skip_count += 1
            print(f"  [{i:3d}/{total}] ID {game_id:5d} - SKIPPED (in skip list)")
            continue

        # Get existing game data from dump
        existing = games_by_id.get(game_id)
        if not existing:
            print(f"  [{i:3d}/{total}] ID {game_id:5d} - NOT FOUND in dump, skipping")
            not_found_count += 1
            continue

        # Build the PUT payload: preserve existing fields, update the 5 new ones
        payload = {
            "id": existing["id"],
            "nombre": existing["nombre"],
            "activo": existing.get("activo", True),
            "complejidad": existing.get("complejidad", "VERDE"),
            "descripcion": existing.get("descripcion"),
            "recomendadoDosJugadores": existing.get("recomendadoDosJugadores", False),
            "ubicacion": existing.get("ubicacion", "ENTRADA"),
            # Updated fields (all 5)
            "minJugadores": new_data["minJugadores"],
            "maxJugadores": new_data["maxJugadores"],
            "duracionMediaMin": new_data["duracionMediaMin"],
            "genero": new_data["genero"],
            "idioma": new_data["idioma"],
        }

        url = f"{JUEGOS_URL}/{game_id}"
        try:
            resp = requests.put(url, json=payload, headers=headers, timeout=10)
            if resp.status_code in (200, 204):
                success_count += 1
                name = existing.get("nombre", "?")
                print(f"  [{i:3d}/{total}] ID {game_id:5d} - OK - {name} "
                      f"({new_data['minJugadores']}-{new_data['maxJugadores']}p, "
                      f"{new_data['duracionMediaMin']}min, {new_data['genero']}, "
                      f"{new_data['idioma']})")
            else:
                error_count += 1
                print(f"  [{i:3d}/{total}] ID {game_id:5d} - ERROR {resp.status_code}: {resp.text[:200]}")
        except requests.exceptions.RequestException as e:
            error_count += 1
            print(f"  [{i:3d}/{total}] ID {game_id:5d} - EXCEPTION: {e}")

        # Small delay to not overload the server
        time.sleep(0.05)

    # ---- Summary ----
    print(f"\n{'='*60}")
    print(f"ALL FIELDS UPDATE COMPLETE")
    print(f"{'='*60}")
    print(f"  Total games in data dict: {total}")
    print(f"  Successful updates:       {success_count}")
    print(f"  Errors:                   {error_count}")
    print(f"  Skipped (skip list):      {skip_count}")
    print(f"  Not found in dump:        {not_found_count}")
    print(f"{'='*60}")


if __name__ == "__main__":
    main()
