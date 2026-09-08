---
name: maestro-e2e
description: How to write, maintain, and run declarative End-to-End (E2E) UI test flows using Maestro in Robithoh App. Use when writing new user journey tests or verifying full UI flows.
---

# Maestro End-to-End (E2E) UI Testing

Robithoh App uses **Maestro** for declarative black-box UI testing across Android emulators and devices.

---

## 1. Directory Structure

All Maestro test specifications live in `.maestro/`:
- `.maestro/full_suite.yaml`: Orchestrates the complete suite in sequence.
- `.maestro/flows/`: 10 individual scenario flows (`01_splash_and_home.yaml` through `10_settings_and_profile.yaml`).
- `.maestro/scripts/`: Setup & teardown scripts.

---

## 2. Writing Flow Scenarios

Use Maestro's declarative YAML syntax:

```yaml
appId: com.iqbalwork.robithoh.dev # or production appId
---
- launchApp:
    clearState: true

# Assert initial screen
- assertVisible: "Robithoh"

# Tap interactive elements by text, id, or test tag
- tapOn: "Jadwal Sholat"

# Assert navigation and content
- assertVisible: "Waktu Sholat Hari Ini"

# Scroll if needed
- scroll
```

---

## 3. Running Maestro Flows

Use the helper script:
```bash
# Run full suite
bash .agents/tools/run_maestro.sh

# Run specific flow
bash .agents/tools/run_maestro.sh .maestro/flows/06_tasbih_digital.yaml
```
