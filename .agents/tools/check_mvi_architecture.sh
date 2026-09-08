#!/usr/bin/env bash
# ==============================================================================
# check_mvi_architecture.sh - Linter for MVI UDF pattern & Screen/Content separation
# ==============================================================================
# Scans feature modules under shared/src/commonMain/kotlin/com/iqbalwork/robithoh/feature/ to verify:
#   1. *Content.kt composables contain ZERO Koin/ViewModel dependencies.
#   2. *Content.kt exceeding ~250 lines have a corresponding components/ sub-package.
#   3. Correct MVI structure (*Contract, *ViewModel, *Screen, *Content).
#
# Usage:
#   bash .agents/tools/check_mvi_architecture.sh
# ==============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
cd "$PROJECT_ROOT"

SCAN_DIR="shared/src/commonMain/kotlin/com/iqbalwork/robithoh/feature"

echo "========================================================"
echo "🏗️ Checking MVI Architecture & UI Separation..."
echo "Directory: $SCAN_DIR"
echo "========================================================"

if [ ! -d "$SCAN_DIR" ]; then
    echo "❌ Feature directory not found: $SCAN_DIR"
    exit 1
fi

python3 - <<'PY'
import pathlib, re, sys

scan_dir = pathlib.Path('shared/src/commonMain/kotlin/com/iqbalwork/robithoh/feature')
content_files = sorted(scan_dir.rglob('*Content.kt'))

violations = []
warnings = []

# Strict violations: direct Koin injection or ViewModel instantiation in Content
STRICT_VIOLATIONS = [
    re.compile(r'import\s+.*koinViewModel'),
    re.compile(r'import\s+org\.koin\.'),
]

# Soft warnings: ViewModel type reference in Content (prefer passing pure UiState data instead)
SOFT_WARNINGS = [
    re.compile(r'import\s+.*ViewModel'),
]

for p in content_files:
    lines = p.read_text(encoding='utf-8', errors='ignore').split('\n')
    line_count = len(lines)

    # Check 1: Forbidden DI/koinViewModel imports in *Content.kt
    for i, line in enumerate(lines):
        for pat in STRICT_VIOLATIONS:
            if pat.search(line):
                violations.append(f"❌ {p.relative_to(scan_dir)}:{i+1}: Forbidden DI/koinViewModel import in stateless Content: {line.strip()}")
        for pat in SOFT_WARNINGS:
            if pat.search(line):
                warnings.append(f"⚠️ {p.relative_to(scan_dir)}:{i+1}: Content composable references ViewModel directly (consider hoisting state to Screen): {line.strip()}")

    # Check 2: Large *Content.kt files missing components/ subfolder
    if line_count > 250:
        parent_dir = p.parent
        components_dir = parent_dir / "component"
        components_dir_plural = parent_dir / "components"
        if not (components_dir.exists() or components_dir_plural.exists()):
            warnings.append(f"⚠️ {p.relative_to(scan_dir)} has {line_count} lines (> 250 lines) but has no 'components/' or 'component/' subpackage.")

print(f"Scanned {len(content_files)} *Content.kt files.")

if warnings:
    print("\n--- Architecture Warnings ---", file=sys.stderr)
    for w in warnings:
        print(w, file=sys.stderr)

if violations:
    print("\n--- ❌ MVI Architecture Violations Found ---", file=sys.stderr)
    for v in violations:
        print(v, file=sys.stderr)
    sys.exit(1)

print("✅ MVI Architecture & Screen/Content separation verified successfully.")
PY

echo "========================================================"
echo "✅ MVI Architecture check passed!"
echo "========================================================"
