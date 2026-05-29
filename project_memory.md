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

#### 2026-05-28 06:38 — Match Setup + Preferences complexity reduction refactor

- Title: `Cyclomatic complexity reduction across match setup flow and preferences mapping`.
- What was shipped: decomposed `reduceMatchSetupScreenState` in `MatchSetupStateStore` into focused sub-reducers (core info, toss details, optional metadata, and start-request handling); refactored `CreateMatchSetupUseCase` into normalized draft + declarative validation rule pipeline + separate object creation; extracted directional choice-focus calculation into standalone shared utility (`ChoiceSectionDirectionalNavigation.kt`) and simplified `MatchSetupScreen` preview-key handling to dispatch logic.
- Key decisions: moved preferences DTO declarations and mappings out of `JsonPreferencesRepository` into dedicated files (`AppPreferencesConfigDto.kt`, `AppPreferencesConfigMappings.kt`) to reduce repository noise and keep persistence orchestration separate from serialization/mapping concerns; added focused utility unit tests (`ChoiceSectionDirectionalNavigationTest`) while relying on existing domain/state/repository and desktop UI tests for behavior preservation.
- Gotchas: the new declarative validation flow in `CreateMatchSetupUseCase` depends on validation-before-creation invariants, so creation now uses guarded non-null requirements only after rule evaluation; keyboard-navigation behavior had to remain clamped at list edges to preserve existing desktop interaction semantics.
- Test coverage areas: verified with `./gradlew :domain:allTests :shared:allTests :desktopApp:test --no-daemon` (success).

#### 2026-05-28 09:16 — Compose Navigation 3 dependency confirmation and upgrade

- Title: `Compose Navigation 3 dependency confirmation and upgrade`.
- What was shipped: confirmed the project was not on Navigation 3 (`org.jetbrains.androidx.navigation:navigation-compose:2.9.2`), then upgraded shared navigation dependency wiring to `org.jetbrains.androidx.navigation3:navigation3-ui:1.1.1` via version catalog updates and shared module dependency update.
- Key decisions: kept the existing centralized route state pattern (`AppRouteStateStore` + `ScorerRoute`) unchanged for this task and performed a dependency-line upgrade only; used JetBrains multiplatform Navigation 3 UI artifact directly after runtime artifact resolution proved unavailable in current repository setup.
- Gotchas: `navigation3-runtime` under `org.jetbrains.androidx.navigation3` did not resolve in this environment, while `navigation3-ui` did; using the UI artifact directly resolved dependency compatibility for current module targets.
- Test coverage areas: verified with `./gradlew :shared:allTests --no-daemon` (BUILD SUCCESSFUL).

#### 2026-05-28 10:58 — Room persistence + Home history + summary flow sprint delivery

- Title: `Room persistence foundation, persisted start flow, Home history entry, and summary route slice`.
- What was shipped:
  - Added typed domain persistence contracts/models and use cases (`MatchRepository`, `ScoreEventRepository`, `CreateAndSaveMatchUseCase`, `StoredMatch`, `ScoreEvent`, `MatchSummary`).
  - Implemented Room persistence foundation in shared code (`MatchEntity`, `ScoreEventEntity`, DAOs, `ScorecardDatabase`, schema export v1, local data sources, Room repositories, mapper layer).
  - Wired platform DB builders/paths and repository injection across Android/iOS/Desktop entry points.
  - Refactored setup start flow to `validate -> persist -> route(matchId)` with explicit save states/errors.
  - Added Home route/state/store/screen with loading/empty/content/error states, `New` action, and match-row summary navigation.
  - Added read-only Match Summary route/state/store/screen backed by repository query by `matchId`.
  - Inserted new Sprint 2 DB/Home sprint docs, renumbered downstream sprint docs to Sprint 8, and updated roadmap/recap references.
- Key decisions:
  - Kept expected failures explicit with Arrow `Either` and typed persistence errors (no exception-driven normal flow).
  - Kept domain models platform-neutral and route ownership centralized in `ScorerRoute` + `AppRouteStateStore`.
  - Used constructor-injected dependencies and avoided service-locator-style DI calls.
- Gotchas:
  - `kotlinx-datetime` API expectations required aligning epoch-day storage to `Long` and using current Kotlin time APIs for timestamp defaults.
  - New `MatchSetupStartMatchResult` variants required exhaustive UI handling in `MatchSetupScreen`.
- Test coverage areas:
  - Added/updated mapper/repository/data-source/store/UI tests including `EntityMappingsTest`, `RoomMatchRepositoryTest`, `RoomScoreEventRepositoryTest`, `RoomLocalDataSourceTest`, `MatchSetupStateStoreTest`, `AppRouteStateStoreTest`, `HomeStateStoreTest`, `MatchSummaryStateStoreTest`, and `HomeScreenUiTest`.
  - Verified with `./gradlew :shared:jvmTest :domain:jvmTest :desktopApp:compileKotlin :androidApp:compileDebugKotlin` (success).

#### 2026-05-28 11:27 — Home history blank after creating match regression fix

- Title: `Scoring back-route correction and deterministic Home history refresh on route re-entry`.
- What was shipped:
  - Fixed `ScoringEntryRoute` placeholder action so `Back to Match setup` navigates to `MatchSetupRoute` (not `HomeRoute`).
  - Added Home re-entry refresh in `App.kt` via route-keyed effect dispatching `HomeScreenEvent.RefreshRequested` when the current route is `Home`.
  - Added desktop regression tests in `desktopApp/src/test/kotlin/cricket/knowledgespike/scorer/AppDesktopUiTest.kt` for both reported symptoms.
- Key decisions:
  - Kept navigation ownership centralized in `AppRouteStateStore` and route handling in `App` (no composable-level business logic drift).
  - Used route-entry refresh trigger instead of repository polling to keep state updates explicit and deterministic.
- Gotchas:
  - Route changes issued in the same frame can be coalesced in Compose tests; regression sequence was stabilized by separating `showMatchSetup()` and `showHome()` across idles.
  - Desktop test module needed explicit `testImplementation` for `arrow-core` and `kotlinx-datetime` due direct repository/domain usage in tests.
- Test coverage areas:
  - Reproducer/fix validation: `./gradlew :desktopApp:test --tests "cricket.knowledgespike.scorer.AppDesktopUiTest"`.
  - Broader impacted verification: `./gradlew :shared:jvmTest :desktopApp:test`.

#### 2026-05-28 11:37 — Home match action menu with confirmation-gated delete

- Title: `Home match actions: Edit, Score fallback, and delete with confirmation`.
- What was shipped:
  - Changed Home match-row interaction from direct navigation to action dialog options: `Edit`, `Score`, and `Delete`.
  - Implemented `Edit` to route to `MatchSetupRoute` and `Score` to route to current fallback `MatchSummaryRoute(matchId)`.
  - Implemented confirmation-gated delete flow in Home state/UI so matches are deleted only after explicit confirmation.
  - Extended persistence contract and implementations with `MatchRepository.deleteMatch(matchId)` plus typed delete failures.
- Key decisions:
  - Kept route ownership centralized in `HomeStateStore` + `ScorerRoute`; UI remains declarative and event-driven.
  - Modeled delete failures explicitly with Arrow-style error typing (`MatchPersistenceError.UnableToDeleteMatch`) instead of exception-driven flow.
- Gotchas:
  - Adding a new persistence error variant required updating all exhaustive UI error mappers and all `MatchRepository` test doubles.
  - Existing Home tests assumed direct row-to-summary routing; they were reworked to verify dialog-state transitions before routing/deleting.
- Test coverage areas:
  - Added/updated tests in `HomeStateStoreTest`, `HomeScreenUiTest`, `RoomMatchRepositoryTest`, `RoomLocalDataSourceTest`, `MatchSetupStateStoreTest`, and `MatchSummaryStateStoreTest`.
  - Verified with `./gradlew :shared:jvmTest :desktopApp:test --tests "cricket.knowledgespike.scorer.AppDesktopUiTest"` and `./gradlew :shared:jvmTest :desktopApp:test` (success).

#### 2026-05-29 11:15 — Home inline row actions (no action popup)

- Title: `Inline Home row actions for Edit/Score/Delete`.
- Date/time completed: `2026-05-29 11:15`.
- What was shipped:
  - Replaced Home match-row action popup flow with inline per-row `Edit`, `Score`, and `Delete` buttons in `HomeScreen`.
  - Kept delete safety behavior by preserving confirmation dialog before `MatchRepository.deleteMatch(matchId)` executes.
  - Simplified Home presentation model by removing `MatchSelected` and `HomeMatchDialogState.MatchActions` from screen state/events.
  - Added desktop app-route UI coverage for inline `Edit` and `Score` button navigation outcomes.
- Key decisions:
  - Kept navigation centralized in `HomeStateStore` and `ScorerRoute`; composables remain declarative and stateless.
  - Introduced stable per-row button test tags (`home_edit_match_button_*`, `home_score_match_button_*`, `home_delete_match_button_*`) for deterministic UI tests.
- Gotchas:
  - Shared Compose UI tests are iOS-gated in this repo, so desktop UI regression tests are required to validate inline button interactions on JVM.
  - Dialog `Delete` button text now coexists with row `Delete` labels, so tests must avoid ambiguous node selection.
- Test coverage areas:
  - Updated `HomeStateStoreTest` and `HomeScreenUiTest` for inline action events and confirmation behavior.
  - Added `AppDesktopUiTest` coverage for inline `Edit` -> `MatchSetupRoute` and inline `Score` -> `MatchSummaryRoute(matchId)`.
  - Verified with `./gradlew :shared:jvmTest :desktopApp:test --tests "cricket.knowledgespike.scorer.AppDesktopUiTest"` (success).

#### 2026-05-29 11:37 — Stack-based navigation shell with adaptive back behavior

- Title: `Route stack navigation with adaptive app shell and desktop rail`.
- Date/time completed: `2026-05-29 11:37`.
- What was shipped:
  - Refactored `AppRouteStateStore` from single-route state to stack-backed navigation (`routeStack`) with explicit `push`, `replaceTop`, `pop`, and `resetToHome` APIs.
  - Updated shared `App` shell to render a stack-aware `TopAppBar` with conditional back arrow and to route back actions through stack `pop`.
  - Added expanded-width adaptive shell behavior using `NavigationRail` actions (`Home`, `New Match`) and made `Home` always reset stack depth.
  - Updated navigation wiring so `ScoringEntryRoute` completion/back path replaces top route where appropriate, while new forward routes push to stack.
  - Added platform back-handler abstraction (`PlatformBackHandler`) with Android actual using `androidx.activity.compose.BackHandler` and non-Android no-op actuals.
- Key decisions:
  - Kept navigation ownership centralized in `AppRouteStateStore` and route usage centralized in `App` to avoid route logic scattering.
  - Used an expect/actual bridge for back handling to keep common code platform-neutral while still using Android idiomatic `BackHandler`.
- Gotchas:
  - `androidx.activity:activity-compose` cannot be used in `commonMain`; it must stay platform-scoped (`androidMain`) to avoid JVM target resolution failures.
  - `TopAppBar` usage required `@OptIn(ExperimentalMaterial3Api::class)` under current library versions.
- Test coverage areas:
  - Updated `AppRouteStateStoreTest` for stack semantics (push/pop/reset/replace and helper route methods).
  - Added desktop UI regressions in `AppDesktopUiTest` for top-bar back pop and expanded rail `Home`/`New Match` behaviors.
  - Verified with `./gradlew :shared:jvmTest :desktopApp:test --tests "cricket.knowledgespike.scorer.AppDesktopUiTest"` and `./gradlew :shared:jvmTest :desktopApp:test :androidApp:compileDebugKotlin` (success).