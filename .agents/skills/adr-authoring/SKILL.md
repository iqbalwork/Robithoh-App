---
name: adr-authoring
description: How to author an Agent Decision Record (ADR) in Robithoh App — using the exact template in `.agents/adr/ADR_TEMPLATE.md`, 4-digit sequential numbering, and indexing in `.agents/AGENTS.md`. Use whenever making significant architectural, structural, or dependency decisions.
---

# Writing an ADR in Robithoh App

ADRs record *why* a particular technical path was chosen, serving as an immutable audit trail for present and future AI sessions. Write an ADR whenever a decision would otherwise be questioned or re-litigated later (e.g. offline-first architecture, UI presentation paradigms, audio platform bridges, or widget data synchronization).

---

## 1. Template & Format

Always base the ADR on [`.agents/adr/ADR_TEMPLATE.md`](../../adr/ADR_TEMPLATE.md):

```markdown
# ADR-XXXX: [Short Descriptive Title]

*   **Status**: [Draft | Proposed | Accepted | Superseded | Obsolete]
*   **Date**: YYYY-MM-DD
*   **Deciders**: [Agent Name/ID, Iqbal Fauzi]
*   **Consulted**: [e.g., Domain Expert, Tech Lead]
*   **Informed**: [e.g., Engineering, QA]

---

## 1. Context and Problem Statement
[Describe the problem, requirements, constraints, and background.]

## 2. Decision
[The chosen approach. Use clear "We decided to..." statements.]

## 3. Rationale
[Why was this selected over alternatives? Tradeoffs and justification.]

## 4. Consequences
*   **Good**: [Positive outcomes]
*   **Bad**: [Negative tradeoffs or new constraints]
*   **Neutral**: [Side-effects]

## 5. References
*   [Relevant files, documentation, or commits]
```

---

## 2. Numbering and Filename

- 4-digit, zero-padded sequential: `NNNN-kebab-case-title.md` (e.g. `0005-new-feature-design.md`).
- Always check the latest number in `.agents/adr/` before assigning a new number to avoid collisions.
- Match the filename slug with the ADR title.

---

## 3. Mandatory Index Update

Immediately after creating the ADR, register it in `.agents/AGENTS.md` and `.agents/CLAUDE.md` under **"🏛️ Agent Decision Records (ADRs)"**:

```markdown
- **[ADR-NNNN: Title](adr/NNNN-slug.md)** — Short 1-sentence summary of decision.
```
