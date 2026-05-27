# Sprint 1 — Navigation shell and match setup completion hardening

## Objective

Deliver a reliable app shell that can move from `Match Setup` into the scoring journey while
preserving existing `M1` behavior and enforcing `M12` architecture constraints.

## MoSCoW scope

- Primary: `M1`, `M12`
- Foundation for: `M2`, `M7`

## Planned duration and milestone

- Duration: 2 weeks
- End-of-sprint milestone: setup flow emits deterministic route intent and app-level routing is
  centralized.

## In scope

1. Expand centralized routes in `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/navigation/ScorerRoute.kt`.
2. Refactor `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/App.kt` to use route-driven
   rendering instead of fixed setup-only rendering.
3. Harden `Match Setup` state transitions in:
   - `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/matchsetup/MatchSetupScreenState.kt`
   - `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/matchsetup/MatchSetupStateStore.kt`
4. Ensure start gate rules from `CreateMatchSetupUseCase` continue to block invalid match starts.
5. Add/update reducer/store tests for setup completion and route handoff behavior.

## Out of scope

- Ball-by-ball scoring engine logic (`M2`, `M3`) implementation.
- Offline persistence and replay (`M4`, `M5`) implementation.
- Tournament/statistics/public sharing (`S*`) implementation.

## Work breakdown

### Workstream A — Route model and app shell

- Introduce route types for setup completion target(s), beginning with scoring entry.
- Keep route definitions centralized and strongly typed.
- Keep route transitions explicit in state/events rather than implicit UI side effects.

#### Epic A1 audit note — current route surface and call sites (2026-05-27 11:31)

- Audit scope: route contract inventory, app-shell route selection path, and shared route consumer map
  before route-contract changes.

##### Route type inventory (`ScorerRoute.kt`)

- File: `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/navigation/ScorerRoute.kt`
- Currently defined route types:
  1. `ScorerRoute.MatchSetupRoute`

##### Current app-shell route selection behavior (`App.kt`)

- File: `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/App.kt`
- Current rendering selection is fixed to setup:
  - `when (ScorerRoute.MatchSetupRoute) { ... }`
  - Only branch present: `ScorerRoute.MatchSetupRoute -> MatchSetupScreen(...)`
- Result: route selection is not driven by mutable current-route state yet.

##### `ScorerRoute` consumer inventory and route-like transition map

- Direct `ScorerRoute` consumers:
  - `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/navigation/ScorerRoute.kt` (contract definition)
  - `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/App.kt` (fixed `when` rendering)
- Route-like transition-related consumers in setup flow:
  - `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/matchsetup/MatchSetupScreen.kt`
    (`StartMatchRequested` dispatch from UI action)
  - `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/matchsetup/MatchSetupScreenState.kt`
    (`MatchSetupStartMatchResult.Ready` / `ValidationError` state)
  - `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/matchsetup/MatchSetupStateStore.kt`
    (`StartMatchRequested` reduction and `onMatchSetupReady(matchSetup)` callback emission)

##### Transition-path classification (explicit vs implicit)

- Explicit (state/event driven):
  - UI emits `MatchSetupScreenEvent.StartMatchRequested`.
  - Store reduces via `CreateMatchSetupUseCase` into explicit `MatchSetupStartMatchResult`
    (`ValidationError` or `Ready`).
- Implicit / side-effect-driven:
  - Setup completion handoff leaves reducer state via callback side effect
    (`onMatchSetupReady(matchSetup)` in `MatchSetupStateStore`) rather than explicit `ScorerRoute`
    state consumed by `App.kt`.
  - App-level screen selection is currently static and does not consume emitted route intent.

##### Missing setup-completion destination route(s)

- Missing typed route contract entry for scoring flow entry after valid setup.
- Minimum required next route: scoring-entry destination (name to be finalized in A2, e.g.
  `ScoringRoute` / `ScoringEntryRoute`).

##### Inventory completeness confirmation

- Route references were audited across `shared/src/commonMain` and include all current direct
  `ScorerRoute` usages plus setup-flow route-like transition sites.
- A1 inventory is complete and establishes the baseline before route-contract expansion work.

#### Epic A4 completion note — route handoff ownership and deterministic transition evidence (2026-05-27 13:46)

##### Transition ownership decision

- Single owner of setup-completion route intent emission is `MatchSetupStateStore` at the reducer/store boundary.
- On `MatchSetupScreenEvent.StartMatchRequested`, the store reduces through `CreateMatchSetupUseCase` and emits only when the reduced result is `MatchSetupStartMatchResult.Ready`.
- Emitted intent is typed and centralized: `ScorerRoute.ScoringEntryRoute(matchSetup)`.

##### App shell responsibility boundary

- `App.kt` now consumes emitted `ScorerRoute` intents via `onRouteRequested` and updates app-level route state through `AppRouteStateStore.showRoute(route)`.
- `App.kt` does not re-implement setup validity rules; business start gating remains in domain/store (`CreateMatchSetupUseCase` + `MatchSetupStateStore`).
- Preference persistence remains side-effect-only in `App.kt` for scoring-entry routes (`recordRecentlyAccessedMatch`).

##### Duplicate-navigation prevention

- `MatchSetupScreen` remains event-dispatch only (`StartMatchRequested`); no direct navigation branching or route construction was added in composables.
- Route construction is centralized in store/reducer flow and not duplicated in UI.

##### Deterministic handoff test evidence

- `shared/src/commonTest/kotlin/cricket/knowledgespike/scorer/matchsetup/MatchSetupStateStoreTest.kt` includes:
  - invalid setup -> no route intent emitted;
  - valid setup -> `ScorerRoute.ScoringEntryRoute` emitted with expected `MatchSetup`;
  - repeated start requests -> deterministic repeated route intents with equal payload.

##### Verification run evidence

- `./gradlew :shared:jvmTest --no-daemon`
- `./gradlew :desktopApp:compileKotlin :androidApp:compileDebugSources :domain:jvmTest --no-daemon`

### Workstream B — Setup flow hardening

- Ensure all required fields from `M1` are still validated before transition.
- Confirm optional fields remain optional and represented explicitly.
- Define failure messaging path from domain validation to screen state.

#### Epic B completion note — setup validation hardening and transition safety evidence (2026-05-27 14:07)

##### B1 validation audit (`M1` required/optional mapping)

- Required fields from `M1` are mapped and enforced end-to-end:
  - Team A: `MatchSetupFormState.teamAName` -> `MatchSetupDraft.teamAName` ->
    `MatchSetupValidationError.MissingTeamAName` -> UI message `Team A name is required`.
  - Team B: `MatchSetupFormState.teamBName` -> `MatchSetupDraft.teamBName` ->
    `MatchSetupValidationError.MissingTeamBName` (and
    `MatchSetupValidationError.TeamNamesMustDiffer`) -> UI messages
    `Team B name is required` / `Team names must be different`.
  - Scheduled overs: `MatchSetupFormState.scheduledOvers` -> `MatchSetupDraft.scheduledOvers` ->
    `MatchSetupValidationError.InvalidScheduledOvers` -> UI message
    `Scheduled overs must be greater than zero`.
  - Toss winner: `MatchSetupFormState.tossWinner` -> `MatchSetupDraft.tossWinner` ->
    `MatchSetupValidationError.MissingTossWinner` -> UI message `Choose the toss winner`.
  - Toss decision: `MatchSetupFormState.tossDecision` -> `MatchSetupDraft.tossDecision` ->
    `MatchSetupValidationError.MissingTossDecision` -> UI message `Choose the toss decision`.
  - Match date: `MatchSetupFormState.matchDate` -> `MatchSetupDraft.matchDate` ->
    `MatchSetupValidationError.InvalidMatchDate` -> UI message
    `Match date must use YYYY-MM-DD`.
- Optional fields are explicit and not hidden defaults:
  - `venue`, `umpireOne`, `umpireTwo`, and `weather` exist in `MatchSetupFormState` and
    `MatchSetupDraft` and are normalized in domain to nullable optionals (`String?`) via
    `normalizedOrNull()`.
- Omitted optional values do not block start when required fields are valid (covered in
  `CreateMatchSetupUseCaseTest` optional-omission success case and shared valid-start flow test).
- Deterministic validation expectations are covered by repeated identical domain invocation and
  repeated store start-request transition tests.

##### B2 start-gate authority (`CreateMatchSetupUseCase`)

- Start-gate authority remains in domain use case:
  - `MatchSetupStateStore.reduceMatchSetupScreenState` on
    `MatchSetupScreenEvent.StartMatchRequested` calls `CreateMatchSetupUseCase` and only sets
    `Ready` when use case returns success.
  - Form-update readiness projection (`canStartMatch`) is now also computed via the same use case
    path (`isStartGateValid(createMatchSetupUseCase)`), removing duplicated ad hoc field-rule
    checks.
- Route transition intent is emitted only after the reduced state is `MatchSetupStartMatchResult.Ready`.
- Business failures are explicit typed outcomes (`Either<MatchSetupValidationError, MatchSetup>`)
  with explicit UI mapping; no exception-driven normal control flow is used.

##### B3 transition hardening evidence (`MatchSetupStateStore`)

- Canonical transition states are explicit in `MatchSetupStartMatchResult`:
  - `Idle` (editing/idle),
  - `ValidationError` (validation-failed),
  - `Ready` (completion-intent-emitted source state).
- Transition table for start flow:

| Event | Prior state | Next state | Route intent |
| --- | --- | --- | --- |
| Any form field change | `Idle`/`ValidationError`/`Ready` | `Idle` (with recomputed `canStartMatch`) | No |
| `StartMatchRequested` with invalid draft | any | `ValidationError` + `canStartMatch = false` | No |
| `StartMatchRequested` with valid draft | any | `Ready` + `canStartMatch = true` | Yes (`ScorerRoute.ScoringEntryRoute`) |
| `ResetRequested` | any | `MatchSetupScreenState()` initial | No |

- Repeated start actions are deterministic and emit equivalent route payloads.
- Validation error state is recoverable by correction and retry without recreating the store.
- State remains immutable (`data class` copy updates + `MutableStateFlow.update`) with no global
  mutable singleton dependencies.

##### B4 composable boundary evidence

- `MatchSetupScreen` composables remain declarative and event-dispatch only:
  - `Button(onClick = { onEvent(StartMatchRequested) })` dispatches events; no business validation
    or route construction in UI.
  - UI consumes immutable `MatchSetupScreenState` projection and renders all visible result states
    (`Idle`, `ValidationError`, `Ready`).
- Navigation intent emission remains outside rendering in `MatchSetupStateStore`.

##### Epic B verification run evidence

- `./gradlew :shared:jvmTest --no-daemon`
- `./gradlew :domain:jvmTest :desktopApp:compileKotlin :androidApp:compileDebugSources --no-daemon`

### Workstream C — Test coverage and regression gates

- Domain tests verify setup validation remains deterministic.
- Shared tests verify reducer/store transitions to navigation intent.
- Verify no platform APIs are introduced into `:domain`.

#### Epic C completion note — regression gates and architecture closure evidence (2026-05-27 15:41)

##### C1 domain validation regression coverage

- Domain validation coverage is present in
  `domain/src/commonTest/kotlin/cricket/knowledgespike/scorer/domain/matchsetup/CreateMatchSetupUseCaseTest.kt`.
- Required-field and invalid-input failure cases are explicitly covered:
  - `MissingTeamAName`, `MissingTeamBName`, `TeamNamesMustDiffer`,
    `InvalidScheduledOvers`, `MissingTossWinner`, `MissingTossDecision`, `InvalidMatchDate`.
- Positive and edge coverage is present:
  - valid draft success path with normalization,
  - optional-field omission acceptance (`venue`/`umpireOne`/`umpireTwo`/`weather` -> `null`),
  - deterministic repeated invocation expectation.
- Expected failures remain explicitly modeled using typed domain outcomes
  (`Either<MatchSetupValidationError, MatchSetup>`), not exception control flow.

##### C2 shared transition/reducer regression coverage

- Shared transition coverage is present in
  `shared/src/commonTest/kotlin/cricket/knowledgespike/scorer/matchsetup/MatchSetupStateStoreTest.kt`.
- Required regression scenarios are covered:
  - valid setup -> `MatchSetupStartMatchResult.Ready` + `ScorerRoute.ScoringEntryRoute` intent emitted,
  - invalid setup -> `ValidationError` and no route intent,
  - repeated `StartMatchRequested` -> deterministic equivalent emitted route intents,
  - correction-after-error recovery -> transition to `Ready` with route emission.
- Tests remain deterministic and platform-neutral (`commonTest` only).

##### C3 cross-module compile and downstream confidence gate

- Verification run completed successfully:
  - `./gradlew :domain:jvmTest :shared:jvmTest :desktopApp:compileKotlin :androidApp:compileDebugSources --no-daemon`
- Gate outcome:
  - relevant domain/shared tests pass,
  - downstream Android/Desktop compile checks pass,
  - no compile regressions introduced in impacted modules.

##### C4 architecture checklist gate evidence

- No Koin service-locator usage was added (`get()` / `inject()` boundary violations absent).
- Domain remains platform-neutral (`:domain` changes are tests-only and contain no platform APIs).
- Expected business failures remain explicit typed results (no exception-driven normal flow).
- Navigation route definitions remain centralized in `ScorerRoute`.
- UI state and transition logic remain immutable and low-complexity in state-store reducer patterns.

##### Sprint 1 closure status

- Epic A complete.
- Epic B complete.
- Epic C complete.
- Sprint 1 is now formally complete for documented scope (`A + B + C`).

## Deliverables

- Updated route contract in `ScorerRoute.kt`.
- Updated app shell navigation orchestration in `App.kt`.
- Updated setup state/event/store handling for transition intent.
- Tests in `domain/src/commonTest/.../matchsetup` and
  `shared/src/commonTest/.../matchsetup` for setup-to-route behavior.

## Dependencies

- Existing `CreateMatchSetupUseCase` contract remains source of truth for setup validity.
- Existing app safe-area scaffold behavior remains intact while routing changes are introduced.

## Risks and mitigations

- Risk: navigation state becomes fragmented across screens.
  - Mitigation: all route declarations stay in `ScorerRoute`; no string-based routing.
- Risk: setup regression while adding route transitions.
  - Mitigation: preserve current tests and add transition-focused reducer tests.

## Test and validation plan

- Run domain setup tests and shared match-setup state store tests.
- Add route transition test cases for:
  - valid setup -> scoring entry route
  - invalid setup -> remain on setup with actionable error
- Confirm compile passes for `:shared`, `:androidApp`, and `:desktopApp` when implementation starts.

## Definition of done

- Setup remains validation-safe and deterministic.
- Route transitions are centralized and test-covered.
- No domain-layer platform leakage.
- Sprint output enables Sprint 2 to start without reworking setup architecture.
