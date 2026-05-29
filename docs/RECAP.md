# Recap

## 2026-05-26

### 09:07

Recap from the last recorded work window up to now:

- Completed a state-of-the-art review of cricket scoring apps across mobile and desktop ecosystems.
- Researched representative products and capabilities (including CricHeroes, Play-Cricket Scorer/Scorer Pro, NV Play, CricClubs, and CricHQ references) to map current market expectations.
- Produced a detailed MoSCoW backlog and saved it to `docs/FEATURES.md`, including:
  - Research snapshot and source coverage
  - Mobile/desktop state-of-the-art themes
  - Detailed `MUST` / `SHOULD` / `COULD` / `WON'T` feature sets
  - Recommended delivery slices and product quality gates
- Confirmed `docs/FEATURES.md` exists and is fully populated for planning use.

Git commits reviewed (relevant context):

- `44b95d2` (2026-05-25 20:53:33 +0100): `feat: Implement new "Match Setup" flow and refactor project structure`
- No new commit has been created yet for the `docs/FEATURES.md` addition in this session context.

### 11:06

Recap from the last recorded work window up to now:

- Consolidated planning baseline from `docs/FEATURES.md` and current architecture anchors in `:domain` and `:shared`.
- Added `docs/SPRINT_PLAN.md` with a MUST-first delivery roadmap, then SHOULD waves (`Sprint 1` to `Sprint 8`).
- Mapped MoSCoW backlog IDs to sprint waves, including explicit deferred scope for `COULD` and `WON'T` items.
- Documented dependencies, delivery risks/mitigations, and cross-sprint quality gates tied to existing project patterns.
- Confirmed this execution step is documentation-focused with planning artifacts in `docs/`.

Git commits reviewed (relevant context):

- `44b95d2` (2026-05-25 20:53:33 +0100): `feat: Implement new "Match Setup" flow and refactor project structure`
- No new commit has been created yet for this sprint-planning documentation pass.

### 11:17

Recap from the last recorded work window up to now:

- Created one detailed sprint document per planned wave and saved them in `docs/sprints/`:
  - `docs/sprints/SPRINT_0_UI_TIDY_UP_PLACEHOLDER.md`
  - `docs/sprints/SPRINT_1_NAVIGATION_SHELL_AND_MATCH_SETUP_COMPLETION_HARDENING.md`
  - `docs/sprints/SPRINT_2_DATABASE_FOUNDATION_AND_MATCH_HISTORY_HOME.md`
  - `docs/sprints/SPRINT_3_BALL_BY_BALL_SCORING_DOMAIN_CORE.md`
  - `docs/sprints/SPRINT_4_LIVE_SCORING_UI_AND_SCORECARD_VIEWS.md`
  - `docs/sprints/SPRINT_5_CORRECTION_WORKFLOWS_AND_OFFLINE_DURABILITY.md`
  - `docs/sprints/SPRINT_6_MUST_COMPLETION_WAVE.md`
  - `docs/sprints/SPRINT_7_SHOULD_WAVE_1_DIFFERENTIATION.md`
  - `docs/sprints/SPRINT_8_SHOULD_WAVE_2_COMPETITION_OPERATIONS.md`
- Expanded each sprint document with implementation-ready detail: objective, MoSCoW coverage, in/out-of-scope boundaries, workstreams, dependencies, risks/mitigations, validation plan, and definition-of-done.
- Kept the sprint-document pack aligned with `docs/SPRINT_PLAN.md` sequencing and `docs/FEATURES.md` priority mapping (MUST-first through SHOULD waves).
- Maintained this pass as documentation-only work.

Git commits reviewed (relevant context):

- `44b95d2` (2026-05-25 20:53:33 +0100): `feat: Implement new "Match Setup" flow and refactor project structure`
- No new commit has been created yet for this per-sprint documentation pass.

## 2026-05-27

### 08:08

Recap from the last recorded work window up to now:

- Continued delivery after the 2026-05-26 planning pass with implementation-focused updates.
- Added product-planning and UI-flow changes captured by commit `0be6130` (`feat: Add product roadmap and refactor Match Setup UI`).
- Applied desktop startup and process/documentation improvements captured by commit `3dd4668` (`chore: Update desktop window defaults and add testing strategy documentation`).
- Implemented a cross-platform preferences mechanism (Android, iOS, Desktop) to persist app settings across sessions, including:
  - JSON-backed local preferences persistence via `PreferencesRepository` (`JsonPreferencesRepository` + `OkioPreferencesStorageDataSource`)
  - Domain preferences models and explicit Arrow `Either` persistence failures
  - Persisted theme preference (`System` / `Light` / `Dark`)
  - Desktop window state persistence (size and position restore/save)
  - MRU tracking for recently accessed matches, recorded from match setup completion
- Wired preferences state usage into platform entry points (`MainActivity`, `MainViewController`, desktop `main.kt`) and shared app/state-store flows.
- Verified behavior/build stability with:
  - `./gradlew :shared:jvmTest --no-daemon`
  - `./gradlew :desktopApp:compileKotlin --no-daemon`
  - `./gradlew :androidApp:compileDebugSources --no-daemon`

Git commits reviewed (relevant context):

- `0be6130` (2026-05-26 11:38:03 +0100): `feat: Add product roadmap and refactor Match Setup UI`
- `3dd4668` (2026-05-26 20:53:24 +0100): `chore: Update desktop window defaults and add testing strategy documentation`
- No new commit has been created yet for the current cross-platform preferences implementation in the working tree.

### 21:27

Recap from the last recorded work window up to now:

- Completed and tracked the Match Setup UI hardening workstream end-to-end, including documenting and then implementing `U1` to `U6` from `docs/tasks/TASKS_MATCH_SETUP_UI_FIXES_CROSS_PLATFORM.md`.
- Shipped keyboard and interaction fixes for toss sections on desktop:
  - `Toss winner` labels now reflect entered team names.
  - `Tab` / `Shift+Tab` traversal works at group level (not per radio option) for `Toss winner` and `Toss decision`.
  - Arrow-key navigation now moves both selection and focus, so `Space` activates the currently focused option reliably.
- Replaced free-text `Match date` input with a cross-platform date picker flow (Android native picker, shared desktop/iOS dialog path), and later updated default picker initialization to open on today's date when no date is set.
- Enabled `Start a Match` completion flow to navigate into a scoring placeholder route with an explicit `Back to Match setup` path, and updated setup copy from `Rosters` to `Player names`.
- Extended match schedule modeling and UI from single-field overs input to explicit `schedule type` (`Overs` / `Balls` / `Days`) plus compact numeric amount entry (digits-only, max 3 chars) with domain validation bounds.
- Expanded and stabilized test coverage across domain/state/UI layers (shared + desktop), repeatedly validating with targeted and aggregate Gradle runs including:
  - `./gradlew :shared:jvmTest :desktopApp:test --no-daemon`
  - `./gradlew :shared:jvmTest :desktopApp:test :androidApp:compileDebugSources --no-daemon`
  - focused desktop UI regression runs for Match Setup keyboard/date/schedule behaviors.
- Added and maintained sprint/epic task documentation and route/navigation hardening artifacts, including Epic A/B/C task-tracking files and route-driven shell/navigation completion work.

Git commits reviewed (relevant context):

- `41d474f` (2026-05-27 09:55:14 +0100): `feat: Implement cross-platform preferences persistence and GitHub Actions CI/CD`
- `ae5a842` (2026-05-27 10:00:25 +0100): `ci: Add Android Lint job and concurrency control to desktop workflow`
- `dcd600c` (2026-05-27 10:02:18 +0100): `chore: Add MIT license and clean up desktop app imports`
- `b24c99d` (2026-05-27 11:13:33 +0100): `test: Expand cross-platform test coverage for unit and UI layers`
- `e3cb43e` (2026-05-27 11:14:38 +0100): `Merge branch 'feat/uiupdates'`
- `5ee6cd5` (2026-05-27 11:16:25 +0100): `chore: Reorganize sprint documentation into a dedicated subdirectory`
- `4c97e75` (2026-05-27 21:26:15 +0100): `feat: Implement route-driven navigation and harden Match Setup flow`
- `e8e8268` (2026-05-27 21:26:28 +0100): `Merge branch 'feat/navigation'`

## 2026-05-29

### 06:57

Recap from the last recorded work window up to now:

- Completed the full DB/Home history sprint implementation plan end-to-end after the 2026-05-27 recap point, including:
  - Versioned Room persistence foundation for `Match` + `ScoreEvent` with cross-platform wiring (Android/iOS/Desktop).
  - Domain persistence contracts/models/use-cases with Arrow-style typed failures.
  - `Start Match` flow updated to validate -> persist -> route using persisted `matchId`.
  - Home as the default app entry route, backed by stored match history.
  - Read-only Match Summary route/state loading by `matchId`.
  - Sprint roadmap updates (inserted DB sprint, renumbered downstream sprint docs and references).
- Fixed a navigation/history regression where `Back to Match setup` from scoring fallback incorrectly returned to Home, and where Home history could appear blank after returning from another route.
- Implemented Home row action dialogs for saved matches:
  - `Edit` routes to Match Setup.
  - `Score` routes to the current scoring fallback path (Match Summary).
  - `Delete` requires explicit confirmation, then deletes and refreshes the Home list.
- Extended persistence contract/repositories/DAO for deletion support with explicit `MatchPersistenceError.UnableToDeleteMatch` mapping and consistent error handling across state stores.
- Added and updated tests across shared/desktop for repository behavior, state transitions, UI flows, and regressions tied to Home refresh, route transitions, and delete confirmation handling.

Git commits reviewed (relevant context):

- `6dd1016` (2026-05-28 09:01:52 +0100): `refactor: Reduce cyclomatic complexity in match setup and preferences logic`
- No newer commit has been created yet for the additional Home actions/delete and related follow-up fixes completed in the current working tree.