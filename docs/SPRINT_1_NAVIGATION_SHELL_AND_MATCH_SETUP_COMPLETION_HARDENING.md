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

### Workstream B — Setup flow hardening

- Ensure all required fields from `M1` are still validated before transition.
- Confirm optional fields remain optional and represented explicitly.
- Define failure messaging path from domain validation to screen state.

### Workstream C — Test coverage and regression gates

- Domain tests verify setup validation remains deterministic.
- Shared tests verify reducer/store transitions to navigation intent.
- Verify no platform APIs are introduced into `:domain`.

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
