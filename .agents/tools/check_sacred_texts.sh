#!/usr/bin/env bash
# ==============================================================================
# check_sacred_texts.sh - SHA-256 integrity guard for sacred Arabic liturgy texts
# ==============================================================================
# Computes and verifies SHA-256 hashes for all liturgical Markdown files in
# shared/src/commonMain/composeResources/files/ and .sq database schemas.
#
# Usage:
#   bash .agents/tools/check_sacred_texts.sh [--check]  # Verifies against baseline
#   bash .agents/tools/check_sacred_texts.sh --update   # Updates baseline hashes
# ==============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
cd "$PROJECT_ROOT"

HASH_STORE=".agents/rules/sacred_texts_hashes.json"
UPDATE_MODE=false

for arg in "$@"; do
    case $arg in
        --update|--generate)
            UPDATE_MODE=true
            ;;
    esac
done

echo "========================================================"
echo "🛡️ Sacred Liturgy Text Integrity Guard"
echo "========================================================"

python3 - <<PY
import hashlib, json, pathlib, sys

files_dir = pathlib.Path('shared/src/commonMain/composeResources/files')
sq_dir = pathlib.Path('shared/src/commonMain/sqldelight')
hash_store_path = pathlib.Path('$HASH_STORE')
update_mode = '$UPDATE_MODE'.lower() == 'true'

targets = sorted(list(files_dir.glob('*.md')) + list(sq_dir.rglob('*.sq')))

if not targets:
    print("❌ No sacred text targets found!", file=sys.stderr)
    sys.exit(1)

current_hashes = {}
for p in targets:
    h = hashlib.sha256(p.read_bytes()).hexdigest()
    rel_path = str(p.relative_to(pathlib.Path('.')))
    current_hashes[rel_path] = h

if update_mode or not hash_store_path.exists():
    hash_store_path.parent.mkdir(parents=True, exist_ok=True)
    hash_store_path.write_text(json.dumps(current_hashes, indent=2, sort_keys=True) + '\n', encoding='utf-8')
    print(f"✅ Sacred text baseline updated successfully ({len(current_hashes)} files hashed) -> {hash_store_path}")
    sys.exit(0)

# Check mode
baseline_hashes = json.loads(hash_store_path.read_text(encoding='utf-8'))

mismatches = []
missing = []

for rel_path, baseline_hash in baseline_hashes.items():
    p = pathlib.Path(rel_path)
    if not p.exists():
        missing.append(rel_path)
        continue
    current_hash = hashlib.sha256(p.read_bytes()).hexdigest()
    if current_hash != baseline_hash:
        mismatches.append(rel_path)

if missing or mismatches:
    print("❌ SECURITY/INTEGRITY VIOLATION: Sacred text files were modified or deleted!", file=sys.stderr)
    if missing:
        print(f"   Missing files ({len(missing)}):", file=sys.stderr)
        for m in missing:
            print(f"     - {m}", file=sys.stderr)
    if mismatches:
        print(f"   Modified files ({len(mismatches)}):", file=sys.stderr)
        for m in mismatches:
            print(f"     - {m}", file=sys.stderr)
    print("\n⚠️ If this text change was explicitly instructed and verified, run:\n   bash .agents/tools/check_sacred_texts.sh --update", file=sys.stderr)
    sys.exit(1)

print(f"✅ Verified {len(current_hashes)} sacred text files. All SHA-256 hashes match baseline.")
PY

echo "========================================================"
echo "✅ Sacred texts integrity intact."
echo "========================================================"
