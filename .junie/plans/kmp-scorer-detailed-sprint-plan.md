---
sessionId: session-260525-213104-3gud
---

# Requirements

### Overview & Goals
Produce a documentation-only planning pack for `KMPScorer` based on the current codebase and `docs/FEATURES.md`, with **no source code implementation** in this task.

### Scope
#### In Scope
- Create/update planning documents that define roadmap, sequencing, and expected outcomes.
- Ground the documentation in the current project baseline (`:domain`, `:shared`, Android/iOS/Desktop entry points).
- Keep delivery artifacts in Markdown and aligned with existing docs style.

#### Out of Scope
- Any Kotlin/Gradle/platform code changes.
- New UI/domain/data implementations.
- Build/test execution as a gate for this documentation-only task (unless needed only for factual verification).

### Baseline Inputs Used
- Product feature source of truth: `docs/FEATURES.md`.
- Current recap log format and chronology rules: `docs/RECAP.md`.
- Current architecture baseline referenced for planning context:
  - `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/App.kt`
  - `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/navigation/ScorerRoute.kt`
  - `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/matchsetup/MatchSetupStateStore.kt`
  - `domain/src/commonMain/kotlin/cricket/knowledgespike/scorer/domain/matchsetup/CreateMatchSetupUseCase.kt`

### Acceptance Criteria
- Documentation clearly states a phased delivery plan mapped to MoSCoW priorities.
- Documentation uses concrete module/file references from the current codebase.
- Resulting change set is documentation-only.

# Technical Design

### Current Implementation Context
- Project is a Kotlin Multiplatform setup with modules `:domain`, `:shared`, `:androidApp`, `:iosApp`, and `:desktopApp` (`settings.gradle.kts`).
- Existing implemented product slice is `Match Setup`, using:
  - domain validation use case (`CreateMatchSetupUseCase`)
  - immutable UI state/events with reducer/store (`MatchSetupScreenState`, `MatchSetupStateStore`)
  - centralized route type (`ScorerRoute`) currently centered on setup flow.
- Existing documentation assets already in place:
  - `docs/FEATURES.md` (state-of-the-art + detailed MoSCoW backlog)
  - `docs/RECAP.md` (chronological work recap format).

### Key Decisions
1. **Documentation-first output only**
   - This task will create/update Markdown artifacts only; no implementation files under `shared/`, `domain/`, `androidApp/`, `iosApp/`, or `desktopApp/` will be modified.
2. **Use `docs/FEATURES.md` as scope authority**
   - Sprint sequencing and priorities will be derived from `MUST/SHOULD/COULD/WON'T` already defined there.
3. **Keep planning artifacts traceable to current architecture**
   - Each roadmap section will reference real modules and existing patterns (use case + state store + centralized route) to reduce handoff ambiguity.
4. **Preserve recap chronology conventions**
   - Any recap additions will append at file end with date heading and time subheading to maintain ordered history.

### Proposed Document Changes
- **`docs/FEATURES.md`**: keep as the feature authority; only adjust if a clarification is needed for sprint mapping consistency.
- **`docs/SPRINT_PLAN.md` (new)**: create a detailed sprint-by-sprint execution document aligned to `FEATURES.md` and current architecture baseline.
- **`docs/RECAP.md`**: append a new timestamped entry summarizing newly created/updated planning documents.

### Document Structure
- `docs/SPRINT_PLAN.md` sections:
  - Scope and assumptions
  - Sprint sequence (MUST first, SHOULD follow-up)
  - Dependencies and risks
  - Quality gates and definition-of-done checkpoints
- `docs/RECAP.md` update format:
  - `## YYYY-MM-DD`
  - `### HH:MM`
  - Bullet recap of documentation work + relevant commit context (if any).

### Risks
- **Risk**: roadmap drifts from current code reality.
  - **Mitigation**: tie each sprint item to concrete modules/files already present.
- **Risk**: documentation becomes redundant or conflicting.
  - **Mitigation**: treat `docs/FEATURES.md` as authoritative feature catalog and keep sprint doc focused on sequencing/delivery.

# Documentation Deliverables

### Deliverable 1 — Sprint plan document
- A dedicated `docs/SPRINT_PLAN.md` capturing phased delivery from current `Match Setup` baseline through MUST then SHOULD priorities.
- Each sprint describes outcome, scope focus, and primary module touchpoints (`:domain`, `:shared`, platform entry points where relevant).

### Deliverable 2 — Feature-to-sprint traceability
- Explicit mapping from `FEATURES.md` MoSCoW items to sprint waves.
- Clear notation of deferred scope (`COULD`/`WON'T`) to prevent accidental expansion.

### Deliverable 3 — Recap update
- Append a chronological entry in `docs/RECAP.md` that records this documentation pass.
- Include timestamp and concise summary of what documentation was added/updated.

# Validation

### Validation Approach
- Verify all referenced files and modules exist in the repository.
- Check cross-document consistency between `docs/FEATURES.md`, `docs/SPRINT_PLAN.md`, and `docs/RECAP.md`.
- Confirm the final diff contains documentation files only.

### Review Checklist
- No source code files changed under `shared/`, `domain/`, `androidApp/`, `iosApp/`, or `desktopApp/`.
- Sprint sequencing prioritizes `MUST` before `SHOULD`.
- Language is implementation-ready (specific, actionable, and non-ambiguous).

# Delivery Steps

### ✓ Step 1: Consolidate documentation baseline and sprint framing
The documentation baseline is validated and a clear sprint-plan outline is defined from existing project artifacts.

- Re-read `docs/FEATURES.md` to extract MoSCoW priorities and delivery constraints.
- Reconfirm current architecture anchors from `App.kt`, `ScorerRoute.kt`, `MatchSetupStateStore.kt`, and `CreateMatchSetupUseCase.kt`.
- Define the sprint plan outline (scope, assumptions, MUST-first cadence, deferred scope rules) before drafting the full document.

### ✓ Step 2: Author the detailed sprint plan document
A complete `docs/SPRINT_PLAN.md` exists with phased, implementation-ready sprint guidance grounded in the current codebase.

- Write sprint-by-sprint outcomes and scope boundaries using concrete module references (`:domain`, `:shared`, platform entry points).
- Map `FEATURES.md` MoSCoW items to sprint waves and clearly mark out-of-scope items.
- Add dependencies, risks, and quality gates so implementation can proceed without architectural ambiguity.

### ✓ Step 3: Append recap and verify documentation-only output
`docs/RECAP.md` includes a new chronological entry and the change set is confirmed to be documentation-only.

- Append a date/time-stamped recap section at the end of `docs/RECAP.md` summarizing documentation work completed.
- Include relevant git context in recap when available.
- Perform a final consistency check across docs and confirm no non-documentation files are part of the intended changes.

### ✓ Step 4: Define per-sprint document template and mapping
Create the structure and content template for one document per sprint based on `docs/SPRINT_PLAN.md`.

- Confirm all seven sprint entries and MoSCoW mappings from `docs/SPRINT_PLAN.md`.
- Define a consistent per-document structure (objective, scope, dependencies, risks, acceptance gates).
- Establish file naming for sprint documents under `docs/`.

### ✓ Step 5: Author one detailed document per sprint
Create seven sprint-specific markdown files in `docs/` with implementation-ready detail aligned to current architecture.

- Produce one file each for Sprints 1-7 using concrete module/file touchpoints.
- Include delivery scope, non-goals, dependencies, test focus, and definition-of-done per sprint.
- Keep MUST-first sequencing and SHOULD wave details consistent with `docs/FEATURES.md` and `docs/SPRINT_PLAN.md`.

### ✓ Step 6: Recap update and documentation-only verification
Append recap entry and confirm the final change set is documentation-only.

- Append a correctly timestamped summary entry to `docs/RECAP.md`.
- Include relevant git commit context where applicable.
- Verify only documentation/plan-tracking files were changed for this task.

### ✓ Step 7: Add Sprint 0 placeholder to sprint planning docs
Add a placeholder pre-sprint for initial UI tidy-up work without changing implementation scope.

- Add a Sprint 0 placeholder section to `docs/SPRINT_PLAN.md` ahead of Sprint 1.
- Create `docs/SPRINT_UI_TIDY_UP_PLACEHOLDER_0.md` with lightweight placeholder scope, non-goals, and definition-of-done.
- Verify updates remain documentation-only and consistent with existing Sprint 1-7 sequence.

### ✓ Step 8: Rename sprint documents to descriptive name-based filenames
Rename each per-sprint document in `docs/` to the `SPRINT_[NAME OF SPRINT]_[NUMBER].md` pattern.

- Rename Sprint 0-7 files from numeric-only names to descriptive name + number filenames.
- Update all in-repo references so links/path mentions point to the renamed files.
- Verify the final change set remains documentation/plan-tracking only.