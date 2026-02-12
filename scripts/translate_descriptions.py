#!/usr/bin/env python3
"""
Translate BGG descriptions from English to Spanish and update the database.
For each game:
  1. GET current data from API
  2. Move old 'descripcion' to 'observaciones' (first run only)
  3. Translate BGG English description to Spanish
  4. PUT updated game with Spanish description

Usage:
    python translate_descriptions.py

Requires: requests, deep-translator (pip install requests deep-translator)
"""

import json
import os
import sys
import time
import requests
from deep_translator import GoogleTranslator

# ============================================================================
# CONFIGURATION
# ============================================================================
BASE_URL = "http://localhost:8080"
LOGIN_URL = f"{BASE_URL}/api/auth/login"
JUEGOS_URL = f"{BASE_URL}/api/juegos"

METADATA_FILE = r"C:\Users\di_os\Music\Proyect\GGBProyectAngular\src\assets\games\bgg-metadata.json"
PROGRESS_FILE = r"C:\Users\di_os\Music\Proyect\GGBProyect\scripts\translate_progress.json"

LOGIN_CREDENTIALS = {
    "email": "admin@ggbproyect.com",
    "password": "admin123"
}

SKIP_IDS = {2, 90001}
REQUEST_DELAY = 1.0  # seconds between API calls
TRANSLATE_DELAY = 0.5  # seconds between Google Translate calls

# ============================================================================
# FUNCTIONS
# ============================================================================

def login():
    """Authenticate and return JWT token."""
    print("Logging in...")
    r = requests.post(LOGIN_URL, json=LOGIN_CREDENTIALS, timeout=10)
    if r.status_code != 200:
        print(f"ERROR: Login failed ({r.status_code}): {r.text}")
        sys.exit(1)
    token = r.json().get("accessToken")
    print("Login OK!")
    return token


def translate_text(text):
    """Translate English text to Spanish using Google Translate."""
    if not text or len(text.strip()) == 0:
        return None

    # Google Translate has a ~5000 char limit per request
    # Truncate if needed
    clean = text.strip()
    if len(clean) > 4500:
        clean = clean[:4500] + "..."

    try:
        translator = GoogleTranslator(source='en', target='es')
        translated = translator.translate(clean)
        return translated
    except Exception as e:
        print(f"    Translation error: {e}")
        return None


def get_game(token, game_id):
    """GET game data from API."""
    headers = {"Authorization": f"Bearer {token}"}
    r = requests.get(f"{JUEGOS_URL}/{game_id}", headers=headers, timeout=10)
    if r.status_code == 200:
        return r.json()
    return None


def update_game(token, game_id, game_data):
    """PUT game data to API with explicit UTF-8 encoding."""
    import json as _json
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json; charset=utf-8"
    }
    body = _json.dumps(game_data, ensure_ascii=False).encode('utf-8')
    r = requests.put(
        f"{JUEGOS_URL}/{game_id}",
        data=body,
        headers=headers,
        timeout=10
    )
    if r.status_code != 200:
        print(f"    PUT error {r.status_code}: {r.text[:200]}")
    return r.status_code == 200


def load_progress():
    """Load set of already-processed game IDs."""
    if os.path.exists(PROGRESS_FILE):
        with open(PROGRESS_FILE, "r") as f:
            return set(json.load(f))
    return set()


def save_progress(done_ids):
    """Save processed game IDs for resume."""
    with open(PROGRESS_FILE, "w") as f:
        json.dump(sorted(done_ids), f)


# ============================================================================
# MAIN
# ============================================================================

def main():
    # Load BGG metadata
    print(f"Loading metadata from {METADATA_FILE}...")
    with open(METADATA_FILE, "r", encoding="utf-8") as f:
        metadata = json.load(f)

    # Filter games with descriptions
    games_with_desc = {
        gid: data for gid, data in metadata.items()
        if data.get("description") and int(gid) not in SKIP_IDS
    }
    total = len(games_with_desc)
    print(f"Found {total} games with BGG descriptions to translate.")

    # Load progress
    done_ids = load_progress()
    if done_ids:
        print(f"Resuming: {len(done_ids)} already done, {total - len(done_ids)} remaining.")

    # Login
    token = login()

    # Process
    success = 0
    skipped = 0
    errors = 0

    print(f"\nTranslating and updating...\n")

    for i, (game_id, data) in enumerate(sorted(games_with_desc.items(), key=lambda x: int(x[0])), 1):
        if game_id in done_ids:
            skipped += 1
            continue

        en_desc = data["description"]

        try:
            # 1. Translate
            es_desc = translate_text(en_desc)
            if not es_desc:
                print(f"  [{i:3d}/{total}] ID {game_id:>5s} - SKIP (translation failed)")
                errors += 1
                time.sleep(TRANSLATE_DELAY)
                continue

            time.sleep(TRANSLATE_DELAY)

            # 2. GET current game from API
            game = get_game(token, game_id)
            if not game:
                print(f"  [{i:3d}/{total}] ID {game_id:>5s} - SKIP (not found in API)")
                errors += 1
                continue

            # 3. Move old descripcion to observaciones (only if observaciones is empty)
            old_desc = game.get("descripcion")
            if old_desc and not game.get("observaciones"):
                game["observaciones"] = old_desc

            # 4. Set new Spanish description
            game["descripcion"] = es_desc

            # 5. PUT updated game
            ok = update_game(token, game_id, game)

            if ok:
                success += 1
                done_ids.add(game_id)
                preview = es_desc[:60].replace('\n', ' ')
                print(f"  [{i:3d}/{total}] ID {game_id:>5s} - OK ({len(es_desc)}ch) - {preview}...")
            else:
                errors += 1
                print(f"  [{i:3d}/{total}] ID {game_id:>5s} - PUT FAILED")

            # Save progress every 10 games
            if success % 10 == 0:
                save_progress(done_ids)

            time.sleep(REQUEST_DELAY)

        except Exception as e:
            errors += 1
            print(f"  [{i:3d}/{total}] ID {game_id:>5s} - ERROR: {e}")
            time.sleep(REQUEST_DELAY)

    # Final save
    save_progress(done_ids)

    # Summary
    print(f"\n{'='*60}")
    print(f"TRANSLATION COMPLETE")
    print(f"{'='*60}")
    print(f"  Total games:     {total}")
    print(f"  Translated OK:   {success}")
    print(f"  Already done:    {skipped}")
    print(f"  Errors:          {errors}")
    print(f"{'='*60}")


if __name__ == "__main__":
    main()
