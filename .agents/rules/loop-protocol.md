# 🔄 Loop Engineering Autonomous Protocol

This protocol governs the **Closed-Loop Autonomous Execution Cycle** for AI agents working in **Robithoh App**. Agents operating in Loop Mode must execute task items sequentially through this deterministic feedback loop without stopping for human intervention unless blocked by an explicit ambiguity or decision boundary.

---

## 🔁 The Closed-Loop Execution Cycle

```
[1. Pick Task] ──► [2. Code Fix/Feature] ──► [3. Multi-Guard Linter Checks]
                                                       │
                                                       ├── (Violation) ──► [Self-Correction Fix]
                                                       │
                                                       ▼
[6. Checkpoint & Commit] ◄── [5. Auto-Changelog] ◄── [4. Unit & E2E Tests Pass]
```

---

## 📜 Step-by-Step Protocol Guidelines

### Step 1: Task Check-out & Scope Confirmation
- Read active task list in `task.artifact.md` (or `.agents/plan/`).
- Mark the current target item as in-progress: `[/] Task description`.

### Step 2: Surgical Implementation
- Implement code changes according to MVI architecture guidelines (`robithoh-mvi` skill).
- Maintain strict Screen vs Content separation (`*Screen.kt` stateful, `*Content.kt` stateless).
- Never modify Arabic liturgical texts or keystore credentials without explicit user command.

### Step 3: Multi-Layer Quality Enforcement (Lint Loop)
Execute the four automated quality gates:
1. `bash .agents/tools/check_mvi_architecture.sh` (MVI & UI separation compliance)
2. `bash .agents/tools/check_sacred_texts.sh` (Sacred text SHA-256 integrity check)
3. `bash .agents/tools/check_markdown_assets.sh` (Markdown liturgical asset validation)
4. `bash .agents/tools/check_hardcoded_strings.sh --ratchet` (Hardcoded string ratchet scanner)

*If any linter fails*: Fix the specific violation and re-run the linter block before proceeding.

### Step 4: Test Verification & Self-Correction
- Run unit test suite via `gradle_build(":shared:allTests")` or `verify_build.sh --fast`.
- **Self-Correction Loop**: If compilation or unit tests fail:
  1. Feed failure output to `python3 .agents/tools/extract_build_errors.py` to extract exact file, line number, and assertion diagnostics.
  2. Analyze error diagnostics and apply surgical code fix.
  3. Re-run test suite.
  4. Repeat up to **3 iterations**. If still failing after 3 attempts, halt and request human guidance with exact diagnostic summary.

### Step 5: Checkpoint & State Synchronization
Once all linters and tests pass:
- Mark task completed in `task.artifact.md`: `[x] Task description`.
- Append activity record in `.agents/changelog/AI_CHANGELOG.md`.
- Update active focus and context in `.agents/memory/STATE.md`.

### Step 6: Handover & Next Task Loop
- Proceed to next pending task `[ ]` in `task.artifact.md` or await user instruction.
