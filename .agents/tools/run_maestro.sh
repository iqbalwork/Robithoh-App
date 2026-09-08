#!/usr/bin/env bash
# ==============================================================================
# run_maestro.sh - Convenient runner for Maestro E2E UI tests
# ==============================================================================
# Usage:
#   bash .agents/tools/run_maestro.sh                     # Runs full suite
#   bash .agents/tools/run_maestro.sh <path-to-flow.yaml> # Runs single flow
# ==============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
cd "$PROJECT_ROOT"

TARGET_FLOW="${1:-.maestro/full_suite.yaml}"

echo "========================================================"
echo "🎭 Robithoh App - Maestro E2E Runner"
echo "Target Flow: $TARGET_FLOW"
echo "========================================================"

if ! command -v maestro &> /dev/null; then
    echo "❌ Maestro CLI not found in PATH."
    echo "   Install via: curl -fsSL 'https://get.maestro.mobile.dev' | bash"
    exit 1
fi

# Verify adb device connection
if command -v adb &> /dev/null; then
    DEVICE_COUNT=$(adb devices | grep -v "List" | grep "device$" | wc -l || true)
    if [ "$DEVICE_COUNT" -eq 0 ]; then
        echo "⚠️ No connected Android devices or emulators found via ADB."
        echo "   Please start an emulator or connect a device before running E2E tests."
        exit 1
    fi
    echo "📱 Connected Android device detected."
fi

echo "🚀 Launching Maestro test..."
maestro test "$TARGET_FLOW"
