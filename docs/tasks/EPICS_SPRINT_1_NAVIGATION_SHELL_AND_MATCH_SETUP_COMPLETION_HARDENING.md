# Tasks — Sprint 1: Navigation shell and match setup completion hardening

Source sprint definition: `docs/sprints/SPRINT_1_NAVIGATION_SHELL_AND_MATCH_SETUP_COMPLETION_HARDENING.md`

## Goal of this task breakdown

Convert Sprint 1 scope into concrete implementation and validation tasks so work can be executed in
small, testable increments while protecting existing `M1` behavior and enforcing `M12` architecture
constraints.

## Scope mapping

- Primary scope: `M1`, `M12`
- Foundation scope unlocked by completion: `M2`, `M7`
- Out of scope implementation: scoring engine internals (`M2`, `M3`), offline replay (`M4`, `M5`),
  tournament/statistics/public sharing (`S*`)

## Delivery principles for Sprint 1 execution

- Centralize navigation contracts in `ScorerRoute` (no scattered string routes).
- Keep domain validation source of truth in `CreateMatchSetupUseCase`.
- Express route transitions explicitly in immutable state/events.
- Keep `:domain` platform-neutral.
- Add regression-first tests around setup validity and route handoff.

## Epic A — Route model and application shell orchestration

### A1. Audit current route surface and call sites

- **Objective:** produce a complete inventory of current route types and all locations that consume
  routes.
- **Files to inspect:**
  - `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/navigation/ScorerRoute.kt`
  - `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/App.kt`
  - any screen/store files referencing route decisions.
- **Implementation tasks:**
  1. Enumerate all existing route entries and identify missing setup-completion targets.
  2. Identify if route transitions are currently implicit (UI side effect) vs explicit (event/state).
  3. Capture migration notes for all impacted consumers.
- **Acceptance criteria:**
  - Route inventory document/checklist exists in PR description or sprint notes.
  - All route consumers are identified before code changes begin.

### A2. Expand centralized route contract in `ScorerRoute.kt`

- **Objective:** define strongly typed route(s) needed to leave Match Setup and enter scoring flow.
- **Implementation tasks:**
  1. Add route type(s) representing setup completion destination(s), starting with scoring entry.
  2. Preserve type safety and avoid string-based route propagation.
  3. Ensure naming reflects intent and aligns with existing route conventions.
- **Acceptance criteria:**
  - New route contract compiles across all targets.
  - No new hard-coded navigation strings are introduced.
  - Route definitions remain single-source in `ScorerRoute.kt`.

### A3. Refactor `App.kt` to route-driven screen rendering

- **Objective:** replace setup-only fixed rendering with route-driven app shell behavior.
- **Implementation tasks:**
  1. Introduce route state read path in `App.kt` (or existing app-level state holder).
  2. Render screen content from explicit current route.
  3. Preserve existing safe-area/scaffold behavior.
  4. Keep cross-platform rendering parity (Android/iOS/Desktop shared behavior).
- **Acceptance criteria:**
  - App shell selects screen from route contract, not hardcoded setup path.
  - Existing setup screen remains reachable and behaviorally unchanged pre-completion.
  - No platform-specific API leaks into common app shell logic.

### A4. Validate route handoff boundaries

- **Objective:** ensure route transition responsibility is clear and not duplicated.
- **Implementation tasks:**
  1. Decide and document single owner of setup -> route intent emission (store/reducer boundary).
  2. Ensure `App.kt` consumes emitted route intent without re-deriving business rules.
  3. Remove/avoid duplicated start-navigation logic in composables.
- **Acceptance criteria:**
  - Single authoritative transition path exists.
  - Route handoff is deterministic and observable in tests.

## Epic B — Match Setup flow hardening and transition safety

### B1. Re-validate setup field requirements against `M1`

- **Objective:** guarantee required setup fields remain enforced before route transition.
- **Files to inspect/change:**
  - `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/matchsetup/MatchSetupScreenState.kt`
  - `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/matchsetup/MatchSetupStateStore.kt`
  - `domain` setup use case and related validation models.
- **Implementation tasks:**
  1. Map each required input field to validation output and UI error representation.
  2. Verify optional fields remain optional and explicitly represented.
  3. Confirm no defaulting behavior bypasses required validation.
- **Acceptance criteria:**
  - Invalid required fields block start.
  - Optional fields do not block start when omitted.
  - Validation behavior remains deterministic.

### B2. Keep `CreateMatchSetupUseCase` as start-gate authority

- **Objective:** ensure setup start gating is not duplicated or weakened in UI/store layers.
- **Implementation tasks:**
  1. Verify store invokes use case for start eligibility decisions.
  2. Ensure UI/store do not encode parallel business validation rules.
  3. Propagate use case failures into explicit screen-state error fields.
- **Acceptance criteria:**
  - Setup start gate decisions originate from domain use case.
  - Expected business failures are represented explicitly (no exception-driven control flow).

### B3. Harden state transitions in `MatchSetupStateStore`

- **Objective:** make transition from setup interaction to completion intent explicit and robust.
- **Implementation tasks:**
  1. Define/confirm transition states for: idle/editing, validation-failed, completion-intent-emitted.
  2. Ensure one-shot navigation intents are represented safely and reset/consumed correctly.
  3. Keep state immutable and predictable across repeated user actions.
  4. Avoid introducing global mutable state or hidden dependencies.
- **Acceptance criteria:**
  - Multiple rapid `Start` actions do not produce inconsistent state.
  - Error state is actionable and recoverable after user correction.
  - Successful transition emits correct route intent exactly as designed.

### B4. Ensure composables remain declarative and thin

- **Objective:** keep business logic out of UI and preserve unidirectional flow.
- **Implementation tasks:**
  1. Verify composables dispatch events only; no validation logic in composables.
  2. Ensure UI consumes immutable state from store/viewmodel layer.
  3. Confirm all visible screen states are representable (content, validation error, in-progress if
     present).
- **Acceptance criteria:**
  - Composables are mostly stateless and event-driven.
  - No new business rules appear in UI rendering code.

## Epic C — Regression tests and quality gates

### C1. Domain validation regression tests

- **Objective:** prove setup validity rules remain unchanged unless intentionally specified.
- **Target tests:** `domain/src/commonTest/.../matchsetup`
- **Implementation tasks:**
  1. Add/update tests for required-field failures.
  2. Add/update tests for valid setup success path.
  3. Add/update tests for optional-field omission acceptance.
  4. Keep expected failure modeling explicit.
- **Acceptance criteria:**
  - Domain tests clearly cover positive, negative, and edge validation scenarios.

### C2. Shared state/reducer transition tests

- **Objective:** validate setup store emits correct route intent only on valid setup.
- **Target tests:** `shared/src/commonTest/.../matchsetup`
- **Implementation tasks:**
  1. Add route transition case: valid setup -> scoring entry route intent.
  2. Add non-transition case: invalid setup -> remain on setup with error state.
  3. Add repeat-action stability case (idempotent/deterministic behavior expectation).
- **Acceptance criteria:**
  - State transition tests are deterministic and independent of platform APIs.

### C3. Cross-module compile and downstream confidence checks

- **Objective:** ensure sprint changes do not break dependent modules.
- **Execution tasks:**
  1. Compile/verify `:shared`.
  2. Compile/verify `:androidApp`.
  3. Compile/verify `:desktopApp`.
  4. Run all relevant domain/shared tests impacted by route/setup changes.
- **Acceptance criteria:**
  - No compile failures in impacted modules.
  - Relevant test suites pass.

### C4. Architecture checklist gate (from project guidelines)

- **Objective:** enforce architectural constraints before merge.
- **Checklist tasks:**
  1. Confirm no Koin service-locator style lookups were added.
  2. Confirm domain remains platform-neutral.
  3. Confirm expected failures are explicit (not exception control flow).
  4. Confirm navigation routes remain centralized.
  5. Confirm immutable UI state and low-complexity transition logic.
- **Acceptance criteria:**
  - PR checklist is fully satisfied with evidence references.

## Sequenced execution plan (recommended order)

1. Complete A1 (inventory) before changing route contracts.
2. Implement A2 -> A3 -> A4 (route contract, shell usage, handoff boundaries).
3. Execute B1 -> B2 -> B3 -> B4 (validation and state hardening).
4. Add/refresh C1 and C2 tests in parallel with B tasks (test-first where possible).
5. Run C3 and C4 as final quality gates prior to merge.

## Risk register and preventive tasks

### Risk R1: Fragmented navigation state across layers

- **Preventive tasks:**
  - enforce all route declarations in `ScorerRoute.kt`;
  - keep transition origin in store/reducer;
  - verify via transition tests.

### Risk R2: Setup validation regression during navigation refactor

- **Preventive tasks:**
  - lock down domain tests before route refactor;
  - map validation failure -> UI error path explicitly;
  - run full relevant test set after transition changes.

### Risk R3: Platform leakage into shared/domain layers

- **Preventive tasks:**
  - review changed files for platform API usage;
  - keep navigation/state logic in common code;
  - reject any platform-specific additions in `:domain`.

## Definition of done checklist for this task plan

- [x] Route model expanded and centralized in `ScorerRoute.kt`.
- [x] App shell is route-driven in `App.kt`.
- [x] Match Setup transition logic hardened with explicit state/intents.
- [x] Domain setup gate (`CreateMatchSetupUseCase`) remains authoritative.
- [x] Domain + shared tests cover setup validity and route handoff.
- [x] `:shared`, `:androidApp`, and `:desktopApp` compile checks pass.
- [x] Architecture checklist constraints are satisfied.

## Suggested implementation artifacts to produce during sprint

- PR 1: Route contract expansion + initial app shell routing.
- PR 2: Match setup state hardening + validation error path refinement.
- PR 3: Transition/regression tests + final compile/test gate evidence.

## Traceability matrix (Sprint scope -> task groups)

- Sprint in-scope item 1 (route expansion) -> `A1`, `A2`
- Sprint in-scope item 2 (route-driven App shell) -> `A3`, `A4`
- Sprint in-scope item 3 (setup state hardening) -> `B1`, `B3`, `B4`
- Sprint in-scope item 4 (start gate protection) -> `B2`, `C1`
- Sprint in-scope item 5 (tests for handoff behavior) -> `C1`, `C2`, `C3`
