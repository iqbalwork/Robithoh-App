#!/usr/bin/env bash
# ==============================================================================
# check_hardcoded_strings.sh - Linter for hardcoded UI text in Compose screens
# ==============================================================================
# Scans Kotlin files in shared/src/commonMain for raw UI string literals
# assigned to parameters like text, title, label, hint, placeholder, etc.
#
# Usage:
#   bash .agents/tools/check_hardcoded_strings.sh [baseline] [--ratchet]
# ==============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
cd "$PROJECT_ROOT"

BASELINE="725"
RATCHET=false

for arg in "$@"; do
    case $arg in
        --ratchet|--update-baseline)
            RATCHET=true
            ;;
        [0-9]*)
            BASELINE="$arg"
            ;;
    esac
done

echo "========================================================"
echo "🔍 Scanning for Hardcoded UI Strings in Compose..."
echo "Project Root: $PROJECT_ROOT"
echo "========================================================"

COUNT=$(python3 - <<'PY'
import pathlib, re, sys

PAT = re.compile(r'^\s*(text|title|label|hint|placeholder|description|subtitle|message)\s*=\s*"[^"]{3,}"')
ANIM = re.compile(r'animate[A-Za-z]*AsState|updateTransition|rememberInfiniteTransition')

hits = []
scan_dir = pathlib.Path('shared/src/commonMain/kotlin')

if scan_dir.exists():
    for p in sorted(scan_dir.rglob('*.kt')):
        src = p.read_text(encoding='utf-8', errors='ignore').split('\n')

        # Detect line ranges inside @Preview functions (sample data, not production copy)
        in_preview = [False] * len(src)
        for i, line in enumerate(src):
            if '@Preview' not in line:
                continue
            j = i
            while j < len(src) and not re.match(r'^\s*(private |internal )?fun \w+', src[j]):
                j += 1
            if j >= len(src):
                continue
            depth, started, k = 0, False, j
            while k < len(src):
                depth += src[k].count('{') - src[k].count('}')
                if '{' in src[k]:
                    started = True
                in_preview[k] = True
                if started and depth <= 0:
                    break
                k += 1

        for i, line in enumerate(src):
            if in_preview[i] or not PAT.match(line):
                continue
            # Skip debug tags or animation labels
            if line.lstrip().startswith('label') and ANIM.search('\n'.join(src[max(0, i - 8):i + 2])):
                continue
            # Skip test tags
            if 'testTag' in line or 'Modifier' in line:
                continue
            hits.append(f'{p}:{i + 1}: {line.strip()[:100]}')

for h in hits:
    print(h, file=sys.stderr)
print(len(hits))
PY
)

echo "Total raw string occurrences found: $COUNT (Baseline: $BASELINE)"

if [ "$COUNT" -gt "$BASELINE" ]; then
    echo "⚠️ Warning: Hardcoded string count ($COUNT) exceeds baseline ($BASELINE)."
    echo "   Consider moving new copy into composeResources/values/strings.xml or Markdown files."
    exit 1
fi

if [ "$RATCHET" = true ] && [ "$COUNT" -lt "$BASELINE" ]; then
    echo "📉 Ratcheting baseline down from $BASELINE to $COUNT..."
    sed -i "s/BASELINE=\"$BASELINE\"/BASELINE=\"$COUNT\"/" "$SCRIPT_DIR/check_hardcoded_strings.sh"
    echo "✅ Baseline updated to $COUNT in check_hardcoded_strings.sh"
fi

echo "✅ Hardcoded strings within acceptable baseline threshold."
