# 🛡️ Security & Asset Integrity Rules

These rules are non-negotiable and strictly enforced for all AI agents working in **Robithoh App**.

---

## 1. Keystore & Signing Protection
* **NEVER** read, modify, output, or commit `keystore.jks`, `keystore.properties`, or any release signing credentials.
* **NEVER** expose keystore passwords, alias names, or key passwords in logs, PR descriptions, or chat output.
* If a task relates to build signing, only touch the build script abstractions without outputting raw credential values.

---

## 2. Sacred Text & Liturgy Integrity (Teks Arab & Liturgi)
* Teks ayat Al-Qur'an, Dzikir, Sholawat, Doa, dan Manaqib Silsilah 38 adalah teks liturgi sakral yang telah diteliti dan diverifikasi.
* **DO NOT** edit, correct, translate, or rewrite Arabic liturgical texts without explicit user instruction.
* Always preserve Unicode marks, tajwid signs, and harakat formatting.
* When manipulating fonts or line heights in Compose, ensure line heights are sufficiently tall (`lineHeight` ≥ 32.sp for Arabic texts) so that fathah, dhommah, kasrah, and shaddah do not clip.

---

## 3. 100% Offline-First Architecture
* Robithoh App is designed to be fully functional without an internet connection.
* **DO NOT** introduce external network calls, cloud backend dependencies, or dynamic remote fetching for core liturgy content.
* All assets (Markdown docs, audio recitation files, fonts, and pre-seeded SQLite databases) must reside locally in the application bundle (`composeResources`).
