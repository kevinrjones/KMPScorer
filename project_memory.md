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

#### 2026-05-26 20:56 — Cross-platform JSON preferences + MRU persistence

- Title: `Cross-platform JSON preferences + MRU persistence`.
- What was shipped: added a preferences domain model/repository, JSON-backed file persistence, desktop window state persistence, persisted theme selection, and MRU tracking for recently started matches across Android/iOS/Desktop app sessions.
- Key decisions: used `PreferencesRepository` with Arrow `Either` failures, `OkioPreferencesStorageDataSource` for multiplatform file I/O, `AppPreferencesStateStore` for immutable state updates, and hooked MRU writes from `MatchSetupStateStore` on successful `StartMatchRequested`.
- Gotchas: `shared` had to expose `:domain` as `api` because shared public APIs surface domain preference types; desktop window position restoration required local nullable snapshots to avoid cross-module smart-cast issues.
- Test coverage areas: added `JsonPreferencesRepositoryTest` and `AppPreferencesStateStoreTest`; verified with `./gradlew :shared:jvmTest --no-daemon`, `./gradlew :desktopApp:compileKotlin --no-daemon`, and `./gradlew :androidApp:compileDebugSources --no-daemon`.

#### 2026-05-27 08:12 — Recap log update in `docs/RECAP.md`

- Title: `Recap log update in docs/RECAP.md`.
- What was shipped: appended a new `2026-05-27 08:08` recap entry at the end of `docs/RECAP.md` summarizing work completed since the prior recap, including preferences persistence and MRU tracking delivery context.
- Key decisions: kept recap entries strictly chronological by appending to file end, used the provided local session time for heading accuracy, and included relevant commit references (`0be6130`, `3dd4668`) plus current uncommitted work status.
- Gotchas: recap quality depends on combining repository commit history with in-session uncommitted changes to avoid missing latest delivered work.
- Test coverage areas: no automated tests run (documentation-only update).

#### 2026-05-27 09:35 — Root README overhaul and screenshot placeholders

- Title: `Comprehensive README refresh with screenshot placeholders`.
- What was shipped: replaced the root `README.md` with a full project overview including professional badges (Kotlin, Compose for Desktop, License), core feature highlights, clearer module structure, and explicit getting started/run/build instructions for Android, Desktop, and iOS.
- Key decisions: used a `License: TBD` badge because no top-level `LICENSE` file currently exists, and created `docs/images/screenshots/README.md` to define named screenshot placeholders with capture guidance.
- Gotchas: `docs/images/screenshots` did not previously exist, so directory documentation had to be created alongside README references to avoid ambiguous screenshot naming.
- Test coverage areas: no automated tests run (documentation-only update).

#### 2026-05-27 09:40 — GitHub Actions CI + cross-platform desktop release automation

- Title: `Cross-platform desktop CI packaging and tag-gated release workflow`.
- What was shipped: added `.github/workflows/desktop-ci-release.yml` to run tests (JVM/unit, iOS target tests, Android instrumentation/UI tests on emulator), build native desktop packages for Linux (`.deb`), macOS (`.dmg`), and Windows (`.msi`), upload artifacts, and publish a GitHub Release only on tag pushes.
- Key decisions: split verification into dedicated jobs (`verify-tests`, `verify-ios-tests`, `verify-android-ui-tests`), used OS matrix packaging in `package-desktop`, and gated release creation with `if: startsWith(github.ref, 'refs/tags/')`.
- Gotchas: iOS test task names differ by runner CPU architecture, so workflow resolves `iosSimulatorArm64Test` vs `iosX64Test` dynamically before execution.
- Test coverage areas: validated referenced Gradle task availability with `:desktopApp:tasks --all`, `:domain:tasks --all`, and `:androidApp:tasks --all`; validated workflow YAML syntax with `ruby -e "require 'yaml'; YAML.load_file(...)"`.