# 🚀 Panduan Setup CI/CD Google Play (Internal Testing)

Dokumen ini menjelaskan langkah-langkah konfigurasi GitHub Secrets dan Google Play Console agar GitHub Actions pada branch `main` dapat secara otomatis mem-build Android App Bundle (`.aab`) dan mengunggahnya ke track **Internal Testing** di Google Play Store.

---

## 📋 Daftar GitHub Secrets yang Dibutuhkan

Buka repository GitHub Anda, lalu navigasi ke:  
**Settings** ➔ **Secrets and variables** ➔ **Actions** ➔ **New repository secret**.

Tambahkan 5 (lima) secret berikut:

| Nama Secret | Deskripsi | Cara Mendapatkan |
|---|---|---|
| `KEYSTORE_BASE64` | String Base64 dari berkas signing keystore produksi (`keystore.jks`) | Jalankan `base64 -w 0 keystore.jks` di terminal, lalu copy seluruh outputnya |
| `KEYSTORE_PASSWORD` | Password keystore produksi | Sesuai konfigurasi `signing_keystore_password` di `keystore.properties` lokal |
| `KEY_ALIAS` | Alias key release di keystore | Sesuai konfigurasi `signing_key_alias` di `keystore.properties` lokal |
| `KEY_PASSWORD` | Password key alias release | Sesuai konfigurasi `signing_key_password` di `keystore.properties` lokal |
| `PLAY_STORE_JSON_KEY` | Berkas Service Account Google Cloud Platform dalam format JSON plain text | Diambil dari Google Cloud Console & Google Play Console (lihat panduan di bawah) |

---

## 🔑 Langkah Membuat `PLAY_STORE_JSON_KEY` (Service Account)

Untuk mengizinkan GitHub Actions mengunggah AAB ke Google Play Console secara otomatis:

### 1. Hubungkan Google Play Console ke Google Cloud
1. Masuk ke [Google Play Console](https://play.google.com/console).
2. Di menu navigasi kiri, pilih **API access** (Akses API).
3. Jika belum memiliki Google Cloud Project yang terhubung, klik **Link existing project** atau **Create new Google Cloud project**.

### 2. Buat Service Account di Google Cloud Console
1. Di halaman **API access**, klik tautan menuju **Google Cloud Console** (atau buka [Google Cloud Console - Service Accounts](https://console.cloud.google.com/iam-admin/serviceaccounts)).
2. Pastikan project yang aktif sama dengan project Google Play API Anda.
3. Klik tombol **+ CREATE SERVICE ACCOUNT** (+ Buat Akun Layanan).
4. Beri nama, misalnya: `github-actions-play-store`.
5. Klik **CREATE AND CONTINUE**.
6. Pada bagian Role, Anda dapat memberikan role **Service Account User** (opsional di GCP, karena izin rilis sesungguhnya diatur di Google Play Console).
7. Klik **DONE**.

### 3. Buat Kunci JSON (JSON Key)
1. Cari Service Account yang baru dibuat di daftar Service Accounts.
2. Klik tombol menu titik tiga (Actions) ➔ **Manage keys**.
3. Klik **ADD KEY** ➔ **Create new key**.
4. Pilih format **JSON** ➔ Klik **CREATE**.
5. Berkas `.json` akan otomatis terunduh ke komputer Anda.
6. Buka berkas JSON tersebut dengan teks editor, lalu salin **seluruh isinya**.
7. Buka GitHub ➔ Masukkan isi berkas JSON tersebut ke secret `PLAY_STORE_JSON_KEY`.

### 4. Berikan Izin Akun Layanan di Google Play Console
1. Kembali ke [Google Play Console](https://play.google.com/console) ➔ Menu **API access**.
2. Service Account baru akan muncul di bagian **Service accounts**.
3. Klik **Grant access** (Berikan akses) atau **Manage permissions** pada akun layanan tersebut.
4. Di tab **Account permissions** (atau **App permissions**):
   - Pilih aplikasi **Robithoh** (`com.iqbalwork.robithoh`).
   - Centang izin berikut:
     - ✅ **Release apps to testing tracks** (Merilis aplikasi ke jalur pengujian).
     - ✅ **Manage testing tracks and edit testing lists** (opsional, jika ingin mengelola tester).
     - ✅ **Release to production, exclude devices, and use Play App Signing** (jika nanti ingin rilis ke production).
5. Klik tombol **Invite user** atau **Apply / Save changes**.

---

## 🛠️ Cara Kerja Alur CI/CD

1. **Trigger Otomatis (Push/Merge ke `main`)**:
   - Setiap kali branch `main` menerima push atau merge dari PR, workflow `Android Deploy to Google Play` langsung berjalan.
   - Keystore di-decode dan ditandatangani.
   - Catatan rilis (*What's New*) di-generate otomatis dari 5 commit terakhir (maks. 500 karakter).
   - Bundle produksi (`.aab`) dan APK di-compile (`bundleProductionRelease` & `assembleProductionRelease`).
   - AAB & APK diunggah ke GitHub Artifacts (tersimpan selama 30 hari).
   - AAB beserta file deobfuscation ProGuard `mapping.txt` diunggah langsung ke track **Internal Testing** Google Play Store.
   - Rilis GitHub (`release-v<run_number>`) dibuat secara otomatis.

2. **Trigger Manual (`workflow_dispatch`)**:
   - Anda dapat menjalankan workflow secara manual dari tab **Actions** di GitHub dengan memilih workflow `Android Deploy to Google Play`.
   - Tersedia opsi input:
     - **Track**: `internal` (default), `alpha`, `beta`, atau `production`.
     - **Release Status**: `completed` (default), `draft`, atau `inProgress`.
     - **Override versionCode**: Tentukan versionCode secara manual jika diperlukan (misal: `8`).
     - **Override versionName**: Tentukan versionName (misal: `1.1.1`).
     - **Catatan Rilis (Release Notes)**: Tulis catatan perubahan khusus untuk penguji internal.

---

## ❓ Troubleshooting

### Error: "Version code X has already been used"
Google Play tidak mengizinkan unggahan dengan `versionCode` yang sama atau lebih rendah dari build sebelumnya.
- **Solusi 1**: Naikkan nilai `versionCode` di `androidApp/build.gradle.kts`.
- **Solusi 2**: Jika menjalankan manual via GitHub Actions, masukkan angka `version_code` yang lebih tinggi pada input form `workflow_dispatch`.

### Error: "Changes cannot be sent for review automatically"
Biasanya terjadi jika Google Play Console memerlukan pengisian deklarasi kebijakan (Policy Declarations / Data Safety) yang belum selesai di UI Play Console.
- **Solusi**: Pada form trigger manual, centang opsi `changes_not_sent_for_review: true`, sehingga rilis masuk sebagai draf terlebih dahulu tanpa langsung submit review.
