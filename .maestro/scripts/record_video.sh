#!/usr/bin/env bash
set -e

# ==============================================================================
# Script Merekam Video Demo & Automation Test Maestro (.mp4)
# ==============================================================================

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
MAESTRO_DIR="$PROJECT_DIR/.maestro"
VIDEO_DIR="$PROJECT_DIR/.maestro/output_video"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
OUTPUT_FILE="$VIDEO_DIR/robithoh_demo_${TIMESTAMP}.mp4"
REMOTE_REC_PATH="/sdcard/robithoh_demo_rec.mp4"

echo "========================================================"
echo "  🎥 Merekam Video Test Run & Demo Robithoh App"
echo "========================================================"

# Pastikan direktori output video & screenshots tersedia
mkdir -p "$VIDEO_DIR"
mkdir -p "$MAESTRO_DIR/screenshots"

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
fi

# 1. Bersihkan file rekaman lama di device jika ada
adb shell rm -f "$REMOTE_REC_PATH" 2>/dev/null || true

# 2. Mulai perekaman layar via ADB Screenrecord di background (Aspect ratio kompatibel 720x1600)
echo "🎬 Memulai perekaman layar (720x1600, 6Mbps, time-limit=unlimited)..."
adb shell screenrecord --size 720x1600 --bit-rate 6000000 --time-limit 0 "$REMOTE_REC_PATH" &
REC_PID=$!

# Function untuk memastikan screenrecord berhenti dan ditarik saat selesai
cleanup() {
    echo ""
    echo "⏹️  Menghentikan perekaman layar..."
    adb shell pkill -2 -f screenrecord 2>/dev/null || true
    sleep 2
    
    echo "📥 Mengunduh file video ke komputer..."
    if adb pull "$REMOTE_REC_PATH" "$OUTPUT_FILE" 2>/dev/null; then
        adb shell rm -f "$REMOTE_REC_PATH" 2>/dev/null || true
        # Salin screenshots dari maestro tests folder ke .maestro/screenshots
        LATEST_TEST_DIR=$(ls -td "$HOME/.maestro/tests"/* 2>/dev/null | head -n 1)
        if [ -d "$LATEST_TEST_DIR/full_suite/takeScreenshot/screenshots" ]; then
            cp -f "$LATEST_TEST_DIR/full_suite/takeScreenshot/screenshots"/*.png "$MAESTRO_DIR/screenshots/" 2>/dev/null || true
        fi
        echo ""
        echo "========================================================"
        echo "  ✅ Perekaman Video Berhasil!"
        echo "  🎬 File Video: $OUTPUT_FILE"
        echo "  📸 Screenshot tersimpan di: $MAESTRO_DIR/screenshots"
        echo "========================================================"
        ls -lh "$OUTPUT_FILE" 2>/dev/null || true
    else
        echo "⚠️  Gagal menarik video dari perangkat."
    fi
}
trap cleanup EXIT

# 3. Jalankan Maestro Test Suite
echo ""
echo "▶️  Mengeksekusi test flow dan capture screenshots..."
cd "$PROJECT_DIR"
maestro test "$MAESTRO_DIR/full_suite.yaml"
