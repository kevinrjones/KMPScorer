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

#### 2026-05-27 10:45 — Cross-platform test coverage expansion (unit + UI)

- Title: `Cross-platform test coverage expansion (unit + UI)`.
- What was shipped: expanded unit coverage for `CreateMatchSetupUseCase`, `MatchSetupStateStore`, and `RecordRecentlyAccessedMatchUseCase`; added UI tests for desktop (`desktopApp` Compose UI test), Android (`androidApp` instrumentation Compose test), and iOS (shared Compose UI test gated to iOS target execution).
- Key decisions: kept domain/state failure modeling explicit in tests, used Compose UI testing APIs per platform constraints, and updated `.github/workflows/desktop-ci-release.yml` so CI explicitly runs JVM/unit suites, iOS target tests (including shared Compose UI), and Android instrumentation UI tests on emulator.
- Gotchas: shared Compose UI tests in `commonTest` can fail on JVM due Skiko runtime loading; mitigated by running those tests only on iOS and adding a dedicated desktop UI test in `desktopApp` where desktop runtime dependencies are present; local Android instrumentation execution requires a connected emulator/device.
- Test coverage areas: verified with `./gradlew :domain:jvmTest :shared:jvmTest :desktopApp:test :androidApp:testDebugUnitTest --no-daemon`, `./gradlew :domain:iosSimulatorArm64Test :shared:iosSimulatorArm64Test --no-daemon`, and `./gradlew :androidApp:assembleDebug :androidApp:assembleDebugAndroidTest --no-daemon`; attempted `:androidApp:connectedDebugAndroidTest` (blocked locally by no connected device).

#### 2026-05-27 11:17 — Sprint 1 task breakdown document creation

- Title: `Detailed task plan for Sprint 1 navigation shell and match setup hardening`.
- What was shipped: created `docs/TASKS_SPRINT_1_NAVIGATION_SHELL_AND_MATCH_SETUP_COMPLETION_HARDENING.md` with a comprehensive execution plan covering epics, task-level acceptance criteria, sequencing, risks/mitigations, quality gates, and traceability back to Sprint 1 in-scope items.
- Key decisions: aligned task decomposition to the sprint source doc under `docs/sprints/`, emphasized centralized typed routing and domain-authoritative setup validation, and included architecture checklist gates from project conventions.
- Gotchas: the source sprint file is in `docs/sprints/` while the requested output path is in `docs/`, so path alignment had to be explicit to avoid creating the file in the wrong directory.
- Test coverage areas: no automated tests run (documentation-only task).

#### 2026-05-27 11:31 — Epic A1 route surface audit documentation

- Title: `Sprint 1 Epic A1 route inventory and call-site audit`.
- What was shipped: added an `Epic A1 audit note` section to `docs/sprints/SPRINT_1_NAVIGATION_SHELL_AND_MATCH_SETUP_COMPLETION_HARDENING.md` documenting current `ScorerRoute` types, app-shell route selection behavior, shared consumer inventory, explicit-vs-implicit transition paths, missing setup-completion route target(s), and completeness confirmation.
- Key decisions: used the active sprint note as the canonical evidence location for A1, treated `ScorerRoute` direct references and setup transition-related state/store/UI call sites as the full route surface baseline, and deferred route naming finalization for scoring entry to A2.
- Gotchas: current `App.kt` route selection is fixed (`when (ScorerRoute.MatchSetupRoute)`) and setup completion leaves store state via callback side effect, so no explicit route-intent handoff exists yet.
- Test coverage areas: no automated tests run (documentation-only audit task; no runtime logic changes).

#### 2026-05-27 12:39 — Epic A2/A3 route contract + route-driven app shell

- Title: `Sprint 1 Epic A2/A3 centralized scoring-entry route and app-shell routing`.
- What was shipped: expanded `ScorerRoute` with typed `ScoringEntryRoute`, added shared `AppRouteStateStore`, and refactored `App.kt` to render by explicit current route instead of a fixed setup-only branch; also added `AppRouteStateStoreTest` for initial/setup/scoring/reset route transitions.
- Key decisions: kept route definitions centralized in `ScorerRoute.kt`, kept app-shell routing in common shared code, and preserved existing setup/store behavior while wiring setup-completion callback to route transition plus existing recent-match persistence.
- Gotchas: `rememberUpdatedState` for setup-ready callback in `App.kt` required explicit `(MatchSetup) -> Unit` typing to avoid Kotlin type inference errors during `:shared:compileKotlinJvm`.
- Test coverage areas: verified with `./gradlew :shared:jvmTest --no-daemon` and downstream compile checks `./gradlew :desktopApp:compileKotlin :androidApp:compileDebugSources --no-daemon`.

#### 2026-05-27 13:46 — Epic A4 handoff-boundary completion and Epic A closure

- Title: `Sprint 1 Epic A4 explicit route-intent ownership and deterministic handoff validation`.
- What was shipped: updated `MatchSetupStateStore` to emit `ScorerRoute` intents (`onRouteRequested`) with `ScorerRoute.ScoringEntryRoute(matchSetup)` as the setup-completion handoff, updated `App.kt` to consume route intents via `AppRouteStateStore.showRoute`, and expanded shared tests to cover invalid/no-transition, valid scoring-entry transition, and repeated-start deterministic route emission.
- Key decisions: established `MatchSetupStateStore` as the single setup->route intent owner, kept `App.kt` as a route consumer without re-deriving domain validation rules, and preserved composable event-dispatch-only behavior to avoid duplicate navigation logic.
- Gotchas: migrating from `onMatchSetupReady(MatchSetup)` to `onRouteRequested(ScorerRoute)` required synchronized updates across app-shell wiring and shared tests to keep route and preference side effects aligned.
- Test coverage areas: verified with `./gradlew :shared:jvmTest --no-daemon` and `./gradlew :desktopApp:compileKotlin :androidApp:compileDebugSources :domain:jvmTest --no-daemon`.

#### 2026-05-27 14:07 — Epic B setup flow hardening and transition safety completion

- Title: `Sprint 1 Epic B domain-authoritative setup validation and transition hardening`.
- What was shipped: hardened `MatchSetupStateStore` so form-update readiness (`canStartMatch`) is derived through `CreateMatchSetupUseCase` rather than duplicated ad hoc checks; added/updated tests for optional-field omission success, deterministic repeated validation outcomes, deterministic repeated start intent emission, and recovery from validation error after correction; documented Epic B validation/architecture/transition/UI-boundary evidence and completed Epic B task checklist.
- Key decisions: kept `CreateMatchSetupUseCase` as the single start-gate authority for both start execution and readiness projection, preserved explicit `MatchSetupStartMatchResult` state modeling (`Idle`/`ValidationError`/`Ready`), and retained composables as event-dispatch/render boundaries with route intent emission outside UI.
- Gotchas: replacing local field-check gating with domain-derived readiness required careful reducer updates to keep `startMatchResult` reset semantics and `canStartMatch` behavior deterministic across rapid repeated events.
- Test coverage areas: verified with `./gradlew :shared:jvmTest --no-daemon` and `./gradlew :domain:jvmTest :desktopApp:compileKotlin :androidApp:compileDebugSources --no-daemon`.

#### 2026-05-27 15:41 — Sprint 1 Epic C closure and formal sprint completion

- Title: `Sprint 1 Epic C regression gates and architecture checklist closure`.
- What was shipped: created `docs/tasks/TASKS_EPIC_C_SPRINT_1_NAVIGATION_SHELL_AND_MATCH_SETUP_COMPLETION_HARDENING.md` with numbered completion checklist, added Epic C completion evidence in `docs/sprints/SPRINT_1_NAVIGATION_SHELL_AND_MATCH_SETUP_COMPLETION_HARDENING.md`, and marked Sprint 1 DoD items complete in `docs/tasks/EPICS_SPRINT_1_NAVIGATION_SHELL_AND_MATCH_SETUP_COMPLETION_HARDENING.md`.
- Key decisions: used existing domain/shared regression tests as the authoritative C1/C2 evidence baseline, recorded C3/C4 gate outcomes directly in sprint notes, and finalized sprint status only after successful cross-module verification.
- Gotchas: Sprint closure required formal artifact parity (task checklist + sprint evidence + DoD checkboxes), not just passing tests from prior epics.
- Test coverage areas: verified with `./gradlew :domain:jvmTest :shared:jvmTest :desktopApp:compileKotlin :androidApp:compileDebugSources --no-daemon`.

#### 2026-05-27 15:54 — New cross-platform Match Setup UI fix task document

- Title: `Match Setup UI fix task document (cross-platform)`.
- What was shipped: added `docs/tasks/TASKS_MATCH_SETUP_UI_FIXES_CROSS_PLATFORM.md` with a detailed numbered checklist for six requested UI items (dynamic toss labels, desktop tab-group focus behavior for toss groups, cross-platform date control, start-match navigation/back path, and `Rosters` -> `Player names` copy update).
- Key decisions: captured each requested item as an independently actionable task group with explicit acceptance-oriented details and verification expectations.
- Gotchas: this was a planning/documentation task only (no runtime behavior changes implemented yet).
- Test coverage areas: no automated tests run (documentation-only update).

#### 2026-05-27 16:18 — Match Setup U1–U3 implementation (dynamic toss labels + desktop tab traversal)

- Title: `Match Setup U1–U3 implementation`.
- What was shipped: implemented dynamic `Toss winner` option labels from current `Team A`/`Team B` values with explicit blank-name fallbacks; added desktop group-level keyboard traversal for `Toss winner` and `Toss decision` so `Tab`/`Shift+Tab` moves between groups while arrow keys move in-group selection.
- Key decisions: centralized label projection in shared state (`MatchSetupFormState.toTossWinnerOptionLabels()`), kept composables declarative with event dispatch only, and introduced focused desktop UI test tags to verify traversal deterministically.
- Gotchas: desktop Compose test key injection required explicit key down/up sequences for tab navigation assertions; direct click-focus assumptions on some text fields were unreliable for reverse-traversal tests.
- Test coverage areas: added state-level label/selection tests in `MatchSetupStateStoreTest` and desktop UI traversal tests in `MatchSetupScreenDesktopUiTest`; verified with `./gradlew :shared:jvmTest :desktopApp:test --no-daemon` and `./gradlew :androidApp:compileDebugSources --no-daemon`.

#### 2026-05-27 16:24 — Match Setup desktop tab-entry regression fix for toss controls

- Title: `Desktop tab-entry regression fix for toss controls`.
- What was shipped: fixed keyboard focus traversal so users can `Tab` into both toss controls again (`Toss winner`, `Toss decision`) while still preventing tab-stops on every option within each group.
- Key decisions: switched to a single tabbable option per toss group (`Team A` / `Bat`) and kept sibling options non-tabbable; retained arrow-key based in-group selection behavior through shared directional key handling.
- Gotchas: group-level focus targets can appear valid in synthetic tests yet still regress real traversal paths; assertions were updated to verify focus on actual option nodes reached by keyboard tabbing.
- Test coverage areas: verified with `./gradlew :desktopApp:test --tests "cricket.knowledgespike.scorer.matchsetup.MatchSetupScreenDesktopUiTest" --no-daemon` and `./gradlew :shared:jvmTest :desktopApp:test :androidApp:compileDebugSources --no-daemon`.

#### 2026-05-27 16:36 — Match Setup arrow-key focus synchronization for toss radio controls

- Title: `Desktop toss radio focus follows arrow-key selection`.
- What was shipped: fixed toss radio keyboard behavior so when arrow keys change selection in `Toss winner` or `Toss decision`, keyboard focus also moves to the newly selected option; this prevents `Space` from re-selecting the previously focused option.
- Key decisions: kept one tab stop per toss group to preserve group-level `Tab` traversal, introduced per-option `FocusRequester` wiring with deferred focus requests after directional navigation, and added explicit desktop regression coverage for arrow+space behavior.
- Gotchas: immediate `requestFocus()` from preview key handling can run before focusability state recomposes, so focus requests were queued via local pending state and applied in `LaunchedEffect`.
- Test coverage areas: verified with `./gradlew :desktopApp:test --tests "cricket.knowledgespike.scorer.matchsetup.MatchSetupScreenDesktopUiTest.given_toss_winner_group_focused_when_direction_changes_selection_then_focus_moves_with_selected_option" --no-daemon` and `./gradlew :shared:jvmTest :desktopApp:test :androidApp:compileDebugSources --no-daemon`.

#### 2026-05-27 16:38 — Task checklist status update for Match Setup U1–U3

- Title: `Marked U1–U3 complete in cross-platform Match Setup task document`.
- What was shipped: updated `docs/tasks/TASKS_MATCH_SETUP_UI_FIXES_CROSS_PLATFORM.md` to mark checklist items `1` through `10` complete for `U1`, `U2`, and `U3`.
- Key decisions: kept `U4`–`U6` and overall completion checklist items unchecked to reflect remaining scope accurately.
- Gotchas: this task was documentation status-only, so no runtime code or behavior was changed.
- Test coverage areas: no automated tests run (documentation-only update).

#### 2026-05-27 16:42 — Match Setup U4 date control implementation (Android/iOS/Desktop)

- Title: `Match Setup U4 cross-platform match-date picker flow`.
- What was shipped: replaced free-text `Match date` entry with a read-only date control + picker trigger; introduced shared canonical-date normalization helpers and platform picker implementations (`Android` native `DatePickerDialog`; `Desktop` and `iOS` Material3 date picker dialog path) while preserving existing `MatchDateChanged` state/event contracts.
- Key decisions: kept canonical domain representation as ISO date string (`YYYY-MM-DD`), suppressed no-op emissions when the selected date equals the current value, and made picker cancellation a true no-op by not dispatching date events.
- Gotchas: function-type picker injection calls cannot use named arguments, and preserving prior desktop tab traversal required keeping the date field action control non-tabbable.
- Test coverage areas: added desktop UI tests for date selection canonicalization, cancellation no-op, and same-date no-op; added state-store boundary coverage for picker-valid date extremes; verified with `./gradlew :desktopApp:test --tests "cricket.knowledgespike.scorer.matchsetup.MatchSetupScreenDesktopUiTest" --no-daemon` and `./gradlew :shared:jvmTest :desktopApp:test :androidApp:compileDebugSources --no-daemon`.

#### 2026-05-27 21:01 — Match Setup U5/U6 completion (start-match back path + copy update)

- Title: `Match Setup U5/U6 implementation completion`.
- What was shipped: updated shared app-shell scoring placeholder UI to include an explicit `Back to Match setup` action from `ScoringEntryRoute`, and replaced setup optional-details copy from `Rosters` to `Player names`.
- Key decisions: kept navigation centralized through `AppRouteStateStore` (`showRoute`/`showMatchSetup`) and implemented the back path in shared UI so behavior is consistent across Android/iOS/Desktop; verified U6 with desktop UI assertions that `Player names` exists and `Rosters` does not.
- Gotchas: compact-layout Compose desktop tests can fail on visibility checks for off-screen content; existence/count assertions were more stable than strict displayed-state assertions for this copy verification.
- Test coverage areas: verified with `./gradlew :desktopApp:test --tests "cricket.knowledgespike.scorer.matchsetup.MatchSetupScreenDesktopUiTest.given_compact_layout_when_screen_renders_then_match_setup_header_and_disabled_start_button_are_visible" --no-daemon` and full impacted suite `./gradlew :shared:jvmTest :desktopApp:test :androidApp:compileDebugSources --no-daemon`.

#### 2026-05-27 21:06 — Match Setup schedule type + amount input (overs/balls/days)

- Title: `Match setup schedule selector and 3-digit amount entry`.
- What was shipped: replaced the single schedule-overs input with a required `Schedule type` selector (`Overs`, `Balls`, `Days`) and a compact numeric `Amount` field capped at 3 digits in shared Match Setup UI; propagated new schedule model through shared state/store and domain use-case validation.
- Key decisions: introduced explicit domain types (`MatchScheduleType`, `MatchSchedule`) so schedule semantics are modeled in domain data instead of inferred from UI text; kept failure handling explicit with `InvalidScheduleAmount` and preserved existing event-driven/immutable state flow.
- Gotchas: Compose Material exposed-menu APIs differ across versions, so the selector uses `ExposedDropdownMenuBox` with `DropdownMenu`; numeric input is sanitized in UI (`digits only`, `take(3)`) and validated again in domain (`1..999`).
- Test coverage areas: added domain tests for `amount > 999` rejection and non-default schedule type preservation, added desktop UI tests for schedule type selection event and 3-digit truncation behavior, and verified with `./gradlew :shared:jvmTest :desktopApp:test :androidApp:compileDebugSources --no-daemon`.

#### 2026-05-27 21:19 — Match date picker default set to today

- Title: `Match date picker initial selection defaults to today`.
- What was shipped: updated shared date-picker initialization so when no date is currently selected, the picker opens on today’s local date instead of the previous fixed fallback date.
- Key decisions: changed the common `resolveInitialMatchDate` fallback to `Clock.System.todayIn(TimeZone.currentSystemDefault())` so Android/iOS/Desktop implementations all inherit the same default behavior; kept valid prefilled-date parsing unchanged.
- Gotchas: `kotlinx-datetime` in this codebase uses `kotlin.time.Clock` APIs, so the resolver import had to use `kotlin.time.Clock` for compatibility.
- Test coverage areas: added `MatchDatePickerDialogTest` for null/blank/typed-date resolver behavior and verified with `./gradlew :shared:jvmTest --tests "cricket.knowledgespike.scorer.matchsetup.MatchDatePickerDialogTest" --no-daemon` plus `./gradlew :shared:jvmTest :desktopApp:test --tests "cricket.knowledgespike.scorer.matchsetup.MatchSetupScreenDesktopUiTest" :androidApp:compileDebugSources --no-daemon`.