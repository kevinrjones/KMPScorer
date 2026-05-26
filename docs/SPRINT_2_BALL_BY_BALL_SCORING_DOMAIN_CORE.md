# Sprint 2 — Ball-by-ball scoring domain core

## Objective

Build the deterministic scoring engine in `:domain` as the source of truth for legal cricket state
transitions, preparing for UI integration in Sprint 3.

## MoSCoW scope

- Primary: `M2`, `M3`, `M12`
- Foundation for: `M4`, `M7`, `M10`

## Planned duration and milestone

- Duration: 2 weeks
- End-of-sprint milestone: domain use cases can process a complete innings from event history with
  typed validation failures for invalid operations.

## In scope

1. Add scoring domain models under `domain/src/commonMain/kotlin/.../domain/scoring/`.
2. Add explicit rules modules under `domain/src/commonMain/kotlin/.../domain/rules/`.
3. Define delivery event model and aggregation contracts:
   - legal delivery outcomes
   - extras
   - wickets
   - strike rotation
   - over progression
4. Implement use cases for recording/scoring a ball with typed errors (`Either`-style outcomes).
5. Add exhaustive domain tests for legal, illegal, and edge transitions.

## Out of scope

- Full scoring screen Compose implementation.
- Offline persistence adapters and restart recovery implementation.
- Public sharing, tournament, and analytics visualizations.

## Work breakdown

### Workstream A — Domain model design

- Define event entities as immutable records.
- Define derived innings/match snapshots generated from event history.
- Keep naming intent-specific (no generic `Helper`/`Manager` types).

### Workstream B — Rule engine and validation

- Encode over/wicket/innings transitions as pure functions.
- Capture expected business failures as explicit domain error types.
- Prevent invalid commits before state mutation.

### Workstream C — Determinism and replay behavior

- Support deterministic rebuild from event stream.
- Verify replay outputs are identical across repeated runs.
- Add edge cases for extras + wickets + strike changes in same over windows.

### Workstream D — Contract readiness for UI layer

- Stabilize use case boundaries for `:shared` consumption in Sprint 3.
- Keep domain contracts platform-neutral and serialization-safe where relevant.

## Deliverables

- New scoring/rules domain packages and core models.
- Use cases for recording deliveries and deriving innings state.
- Typed error hierarchy for expected invalid scoring operations.
- Domain test suite covering nominal path and edge cases.

## Dependencies

- Sprint 1 route/app shell foundation for later integration.
- Existing domain validation pattern from `CreateMatchSetupUseCase` (`Either` outcomes).

## Risks and mitigations

- Risk: hidden state coupling in scoring logic.
  - Mitigation: pure functions + immutable event stream + replay tests.
- Risk: under-specification of cricket rules leads to later refactor churn.
  - Mitigation: codify rule assumptions in tests before UI wiring.

## Test and validation plan

- Add table-driven tests for deliveries, extras, wickets, and strike rotation.
- Add invalid transition tests (e.g., impossible bowler change or over completion overrun).
- Add deterministic replay tests:
  - same events -> same aggregate every run
  - event edit scenario baseline for upcoming correction sprint

## Definition of done

- Scoring core is deterministic and test-covered.
- Illegal operations fail through typed outcomes, not exceptions.
- Domain remains independent of UI/platform/DI frameworks.
- Sprint 3 can consume stable domain contracts without redesign.
