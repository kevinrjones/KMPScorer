### Project Memory

This file is a running memory log for the project and should be updated after each completed task.

### Stable Project Context

- Project: `KMPScorer` (Kotlin Multiplatform)
- Platforms in scope: Android, iOS, Desktop
- Architecture direction: explicit domain modeling, immutable UI state/events, centralized navigation routes, and adaptive Compose UI patterns.
- Domain error handling direction: model expected failures explicitly (prefer Arrow types) instead of exceptions for normal control flow.

### Recent Task Log

#### 2026-05-25 — Match Setup feature (completed in previous session)

- Implemented cross-platform `Match Setup` flow (Android/iOS/Desktop shared behavior).
- Added domain models/use case and validation for setup fields.
- Added shared screen state/event/store and wired routes/app entry points.
- Added desktop menu path for setup access while keeping visible screen action.
- Added tests for domain and state transitions.
- Updated docs: `CONTEXT.md`, `README.md`, and ADR `docs/adr/0001-core-start-gate-for-match-setup.md`.

#### 2026-05-25 — Project memory bootstrap (this task)

- Created `project_memory.md` at project root as requested by `.junie/AGENTS.md` guidance.
- Added durable context and a structured task log format to support updates after each task.

#### 2026-05-25 — Scoring-focused theme palette update

- Replaced the shared Material 3 color tokens in `Color.kt` with a cricket scoring-focused "Pitch & Score" palette.
- Shifted primary/secondary/tertiary roles to green, navy, and amber tones for stronger score-state readability and clearer visual hierarchy.
- Kept role coverage complete for both light and dark schemes (including container/fixed/surface variants) so existing theme wiring in `Theme.kt` remains unchanged.

#### 2026-05-25 — Match Setup usability tidy-up (mobile + desktop)

- Refactored `MatchSetupScreen` into structured, card-based sections with clearer hierarchy for required fields, optional metadata, and actions.
- Improved adaptive behavior by applying explicit compact/medium/expanded layout metrics and better content width constraints for phone and desktop usability.
- Added shared theme spacing tokens (`Spacing.kt`) so layout spacing and width values are centralized and reusable.
- Verified with `./gradlew :shared:allTests :desktopApp:compileKotlin :androidApp:assembleDebug` (success).

### Update Rule

After each completed task, append a new dated entry under `Recent Task Log` with:

1. What changed.
2. Why it changed.
3. How it was verified (tests/build/run checks).

#### 2026-05-26 — Sprint document filename normalization

- Renamed sprint documentation files in `docs/` to descriptive name-based filenames using the pattern `SPRINT_[NAME OF SPRINT]_[NUMBER].md` for Sprint `0` through Sprint `7`.
- Updated in-repo references that previously pointed at numeric-only sprint filenames so document pointers remain accurate after the rename.
- Why: improve discoverability and readability of sprint documents while preserving sprint-number ordering.
- Verification: confirmed no remaining references to legacy `SPRINT_[N].md` names via repository markdown scan and reviewed scoped `git status` output.

#### 2026-05-26 11:55 — Desktop window default size and position update

- Title: `Desktop window default size and position update`.
- What was shipped: updated desktop app startup window defaults to open at `1200dp x 800dp` and centered on screen.
- Key decisions: configured `Window` with `rememberWindowState(width = 1200.dp, height = 800.dp, position = WindowPosition(Alignment.Center))` in `desktopApp/src/main/kotlin/cricket/knowledgespike/scorer/main.kt`.
- Gotchas: desktop window sizing/positioning uses Compose window state APIs and `dp` units rather than raw pixel primitives.
- Test coverage areas: no tests added or run for this task (UI startup default configuration change only).