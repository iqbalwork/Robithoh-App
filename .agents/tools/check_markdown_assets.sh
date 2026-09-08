#!/usr/bin/env bash
# ==============================================================================
# check_markdown_assets.sh - Validator for pre-bundled liturgical Markdown docs
# ==============================================================================
# Scans all .md files in shared/src/commonMain/composeResources/files/ to ensure:
#   - Non-empty content (> 0 bytes)
#   - Valid UTF-8 encoding
#   - Absence of null/binary byte corruption
#
# Usage:
#   bash .agents/tools/check_markdown_assets.sh
# ==============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
cd "$PROJECT_ROOT"

FILES_DIR="shared/src/commonMain/composeResources/files"

echo "========================================================"
echo "📖 Validating Liturgical Markdown Asset Bundle..."
echo "Directory: $FILES_DIR"
echo "========================================================"

if [ ! -d "$FILES_DIR" ]; then
    echo "❌ Error: Markdown files directory not found: $FILES_DIR"
    exit 1
fi

python3 - <<'PY'
import pathlib, sys

files_dir = pathlib.Path('shared/src/commonMain/composeResources/files')
md_files = sorted(files_dir.glob('*.md'))

if not md_files:
    print("❌ Error: No .md files found in " + str(files_dir), file=sys.stderr)
    sys.exit(1)

total_bytes = 0
corrupted = []

for p in md_files:
    size = p.stat().st_size
    if size == 0:
        print(f"❌ Empty file detected: {p.name}", file=sys.stderr)
        corrupted.append(p.name)
        continue

    try:
        content = p.read_bytes()
        total_bytes += size
        # Check UTF-8 decoding
        decoded = content.decode('utf-8')
        # Check for null bytes
        if '\x00' in decoded:
            print(f"❌ Null byte corruption detected: {p.name}", file=sys.stderr)
            corrupted.append(p.name)
    except UnicodeDecodeError as e:
        print(f"❌ Invalid UTF-8 encoding in {p.name}: {e}", file=sys.stderr)
        corrupted.append(p.name)

if corrupted:
    print(f"\n❌ Total corrupted/invalid files: {len(corrupted)}", file=sys.stderr)
    sys.exit(1)

print(f"✅ Verified {len(md_files)} Markdown liturgical files successfully.")
print(f"   Total bundle size: {total_bytes / 1024:.2f} KB ({total_bytes} bytes)")
PY

echo "========================================================"
echo "✅ All liturgical Markdown assets are valid and intact."
echo "========================================================"
