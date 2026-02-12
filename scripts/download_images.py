#!/usr/bin/env python3
"""
Download board game images, ratings and descriptions from BoardGameGeek.
For each game: searches BGG, extracts box art + rating + description,
downloads image to Angular assets, and saves metadata as JSON.

Usage:
    python download_images.py

Requires: requests (pip install requests)
"""

import json
import os
import re
import sys
import time
import html
import requests

# ============================================================================
# CONFIGURATION
# ============================================================================
DUMP_FILE = r"C:\Users\di_os\Music\Proyect\juegos_dump.json"
OUTPUT_DIR = r"C:\Users\di_os\Music\Proyect\GGBProyectAngular\src\assets\games"
METADATA_FILE = os.path.join(OUTPUT_DIR, "bgg-metadata.json")

BGG_USERNAME = "GiberGamesBar"
BGG_PASSWORD = "bgX@wQQjGGh4F!i"

REQUEST_DELAY = 1.5  # seconds between requests
SKIP_IDS = {2, 90001}
IMAGE_SIZE = "400x400"

# ============================================================================
# FUNCTIONS
# ============================================================================

def create_session():
    """Create an authenticated BGG session."""
    session = requests.Session()
    session.headers['User-Agent'] = (
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) '
        'AppleWebKit/537.36 Chrome/120.0.0.0 Safari/537.36'
    )
    print("Logging in to BoardGameGeek...")
    r = session.post(
        'https://boardgamegeek.com/login/api/v1',
        json={'credentials': {'username': BGG_USERNAME, 'password': BGG_PASSWORD}},
        timeout=15
    )
    if r.status_code in (200, 204):
        print("Login OK!")
    else:
        print(f"Login failed: {r.status_code} (continuing anyway)")
    return session


def search_bgg(session, game_name):
    """Search BGG for a board game, return first result's BGG ID."""
    clean = game_name.strip()
    clean = re.sub(r'\s*\+\s*[Ee]xpansi[oó]n.*$', '', clean)
    clean = re.sub(r'\s*\([^)]*\)\s*$', '', clean)
    clean = re.sub(r'\s*"[^"]*"\s*', ' ', clean)
    clean = re.sub(r'\s*:\s*', ' ', clean)
    clean = clean.strip()

    r = session.get(
        'https://boardgamegeek.com/geeksearch.php',
        params={'action': 'search', 'objecttype': 'boardgame', 'q': clean},
        timeout=15
    )
    if r.status_code == 200:
        matches = re.findall(r'/boardgame/(\d+)/', r.text)
        if matches:
            return int(matches[0])

    # Retry with shorter name
    words = clean.split()
    if len(words) > 2:
        short = ' '.join(words[:2])
        time.sleep(REQUEST_DELAY * 0.4)
        r2 = session.get(
            'https://boardgamegeek.com/geeksearch.php',
            params={'action': 'search', 'objecttype': 'boardgame', 'q': short},
            timeout=15
        )
        if r2.status_code == 200:
            m2 = re.findall(r'/boardgame/(\d+)/', r2.text)
            if m2:
                return int(m2[0])
    return None


def get_game_data(session, bgg_id):
    """
    Get image URL, rating and description from BGG for a given BGG ID.
    Uses geekitems API for description + image, HTML page for rating.
    Returns dict with keys: image_url, rating, num_ratings, description
    """
    result = {
        'image_url': None,
        'rating': None,
        'num_ratings': None,
        'description': None,
    }

    # 1) geekitems API -> description + image
    try:
        r = session.get(
            'https://boardgamegeek.com/api/geekitems',
            params={'objectid': bgg_id, 'objecttype': 'thing', 'subtype': 'boardgame'},
            timeout=15
        )
        if r.status_code == 200:
            data = r.json()
            item = data.get('item', {})

            # Description: strip HTML tags, decode entities
            raw_desc = item.get('description', '') or item.get('short_description', '') or ''
            clean_desc = re.sub(r'<[^>]+>', '', raw_desc)
            clean_desc = html.unescape(clean_desc).strip()
            # Truncate to reasonable length
            if len(clean_desc) > 1000:
                clean_desc = clean_desc[:997] + '...'
            result['description'] = clean_desc

            # Image URL - use previewthumb (300x320) for good quality cards
            images = item.get('images', {})
            img_url = (
                images.get('previewthumb')    # 300x320 - ideal for cards
                or item.get('imageurl@2x')    # 492x600 - fallback
                or item.get('imageurl')       # 246x300 - smaller fallback
                or images.get('original')     # full size - last resort
            )
            if img_url:
                result['image_url'] = img_url
    except Exception as e:
        print(f"    geekitems error: {e}")

    time.sleep(REQUEST_DELAY * 0.5)

    # 2) HTML page -> rating + fallback image
    try:
        r2 = session.get(
            f'https://boardgamegeek.com/boardgame/{bgg_id}',
            timeout=15
        )
        if r2.status_code == 200:
            text = r2.text

            # Rating
            avg = re.search(r'"average":"([0-9.]+)"', text)
            if avg:
                result['rating'] = round(float(avg.group(1)), 2)

            # Number of ratings
            rated = re.search(r'"usersrated":"?(\d+)', text)
            if rated:
                result['num_ratings'] = int(rated.group(1))

            # Fallback image if API didn't give one
            if not result['image_url']:
                imgs = re.findall(
                    r'(https://cf\.geekdo-images\.com/[^"\s>]+__itemrep[^"\s>]+)',
                    text
                )
                if imgs:
                    url = imgs[0]
                    url = re.sub(r'fit-in/\d+x\d+', f'fit-in/{IMAGE_SIZE}', url)
                    result['image_url'] = url

            # Fallback description from og:description
            if not result['description']:
                og = re.search(
                    r'<meta\s+property="og:description"\s+content="([^"]+)"',
                    text
                )
                if og:
                    result['description'] = html.unescape(og.group(1))
    except Exception as e:
        print(f"    page error: {e}")

    return result


def download_image(session, url, filepath):
    """Download an image to a local file."""
    try:
        r = session.get(url, timeout=30)
        if r.status_code == 200 and len(r.content) > 1000:
            with open(filepath, 'wb') as f:
                f.write(r.content)
            return True
    except Exception as e:
        print(f"    download error: {e}")
    return False


# ============================================================================
# MAIN
# ============================================================================

def main():
    print(f"Loading game data from {DUMP_FILE}...")
    try:
        with open(DUMP_FILE, "r", encoding="utf-8") as f:
            games = json.load(f)
    except FileNotFoundError:
        print(f"ERROR: {DUMP_FILE} not found")
        sys.exit(1)

    games = [g for g in games if g["id"] not in SKIP_IDS]
    games.sort(key=lambda g: g["id"])
    total = len(games)
    print(f"Loaded {total} games.")

    os.makedirs(OUTPUT_DIR, exist_ok=True)

    # Load existing metadata if resuming
    metadata = {}
    if os.path.exists(METADATA_FILE):
        with open(METADATA_FILE, "r", encoding="utf-8") as f:
            metadata = json.load(f)
        print(f"Loaded existing metadata for {len(metadata)} games.")

    session = create_session()

    success = 0
    skipped = 0
    not_found = 0
    errors = 0
    not_found_names = []

    print(f"\nProcessing {total} games...\n")

    for i, game in enumerate(games, 1):
        game_id = game["id"]
        game_name = game["nombre"]
        str_id = str(game_id)
        filepath = os.path.join(OUTPUT_DIR, f"{game_id}.jpg")

        # Skip if already fully processed (has image + metadata)
        has_image = os.path.exists(filepath) and os.path.getsize(filepath) > 1000
        has_meta = str_id in metadata and metadata[str_id].get('rating') is not None
        if has_image and has_meta:
            skipped += 1
            print(f"  [{i:3d}/{total}] ID {game_id:5d} - SKIP (complete) - {game_name}")
            continue

        try:
            # Search BGG
            bgg_id = search_bgg(session, game_name)
            time.sleep(REQUEST_DELAY * 0.4)

            if not bgg_id:
                not_found += 1
                not_found_names.append(f"ID {game_id}: {game_name}")
                print(f"  [{i:3d}/{total}] ID {game_id:5d} - NOT FOUND - {game_name}")
                time.sleep(REQUEST_DELAY * 0.3)
                continue

            # Get data from BGG
            data = get_game_data(session, bgg_id)

            # Download image
            img_ok = has_image
            if not has_image and data['image_url']:
                img_ok = download_image(session, data['image_url'], filepath)

            # Save metadata
            meta_entry = {
                'bgg_id': bgg_id,
                'rating': data['rating'],
                'num_ratings': data['num_ratings'],
                'description': data['description'],
                'has_image': img_ok,
            }
            metadata[str_id] = meta_entry

            if img_ok or data['rating'] or data['description']:
                success += 1
                size_kb = os.path.getsize(filepath) / 1024 if img_ok else 0
                rating_str = f"{data['rating']:.1f}" if data['rating'] else '?'
                desc_preview = (data['description'] or '')[:50]
                print(
                    f"  [{i:3d}/{total}] ID {game_id:5d} - OK "
                    f"(img:{size_kb:.0f}KB, rating:{rating_str}, "
                    f"desc:{len(data['description'] or '')}ch) "
                    f"BGG#{bgg_id} - {game_name}"
                )
            else:
                errors += 1
                print(f"  [{i:3d}/{total}] ID {game_id:5d} - NO DATA BGG#{bgg_id} - {game_name}")

            # Save metadata periodically (every 10 games)
            if i % 10 == 0:
                with open(METADATA_FILE, "w", encoding="utf-8") as f:
                    json.dump(metadata, f, indent=2, ensure_ascii=False)

            time.sleep(REQUEST_DELAY)

        except Exception as e:
            errors += 1
            print(f"  [{i:3d}/{total}] ID {game_id:5d} - ERROR: {e} - {game_name}")
            time.sleep(REQUEST_DELAY)

    # Final save
    with open(METADATA_FILE, "w", encoding="utf-8") as f:
        json.dump(metadata, f, indent=2, ensure_ascii=False)

    # Summary
    print(f"\n{'='*60}")
    print(f"DOWNLOAD COMPLETE")
    print(f"{'='*60}")
    print(f"  Total games:    {total}")
    print(f"  Processed OK:   {success}")
    print(f"  Already done:   {skipped}")
    print(f"  Not found:      {not_found}")
    print(f"  Errors:         {errors}")
    print(f"  Metadata file:  {METADATA_FILE}")
    print(f"{'='*60}")

    if not_found_names:
        print(f"\nNot found on BGG ({len(not_found_names)}):")
        for n in not_found_names:
            print(f"  - {n}")


if __name__ == "__main__":
    main()
