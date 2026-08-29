#!/usr/bin/env bash
set -e

# ==============================================================================
# Script Menjalankan Maestro Automation Test & Capture Screenshots
# ==============================================================================

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
MAESTRO_DIR="$PROJECT_DIR/.maestro"
SCREENSHOTS_DIR="$MAESTRO_DIR/screenshots"

echo "========================================================"
echo "  🚀 Menjalankan Automation Test Robithoh App"
echo "========================================================"

# Pastikan direktori screenshots tersedia
mkdir -p "$SCREENSHOTS_DIR"

# Cek apakah device/emulator terhubung
echo "📱 Memeriksa koneksi perangkat Android..."
if command -v adb >/dev/null 2>&1; then
    DEVICES=$(adb devices | grep -v "List of devices" | grep "device" || true)
    if [ -z "$DEVICES" ]; then
        echo "⚠️  Tidak ada perangkat Android yang terdeteksi via ADB."
        echo "   Pastikan Emulator menyala atau HP Android terhubung via USB Debugging."
        exit 1
    else
        echo "✅ Perangkat terdeteksi:"
        echo "$DEVICES"
    fi
else
    echo "⚠️  ADB tidak ditemukan di PATH, melanjutkan eksekusi maestro..."
fi

# Jalankan Maestro Test Suite
echo ""
echo "▶️  Mengeksekusi test flow dan capture screenshots..."
cd "$PROJECT_DIR"
maestro test "$MAESTRO_DIR/full_suite.yaml"

echo ""
echo "========================================================"
echo "  ✅ Automation Test Selesai!"
echo "  📸 Screenshot tersimpan di: $SCREENSHOTS_DIR"
echo "========================================================"
ls -lh "$SCREENSHOTS_DIR" 2>/dev/null || true
