# Sprint 4 — Live scoring UI and scorecard views

## Objective

Deliver the first complete scorer-facing live workflow across platforms, wired to the Sprint 3
domain contracts.

## MoSCoW scope

- Primary: `M7`, `M9`, `M10`
- Continuation: `M2`, `M3`

## Planned duration and milestone

- Duration: 2 weeks
- End-of-sprint milestone: setup-to-live-scoring journey works end-to-end with explicit UI states
  and desktop scorer ergonomics.

## In scope

1. Create scoring feature surface in `shared/src/commonMain/kotlin/.../scoring/`.
2. Create scorecard presentation package in `shared/src/commonMain/kotlin/.../scorecard/`.
3. Extend `ScorerRoute` and `App.kt` to include live scoring and summary route transitions.
4. Add platform-appropriate interaction improvements:
   - mobile fast-entry controls
   - desktop menu and keyboard workflow in `desktopApp/src/main/kotlin/cricket/knowledgespike/scorer/main.kt`
5. Ensure loading/content/empty/error state coverage on all primary scoring screens.

## Out of scope

- Deep correction and replay history tooling (Sprint 5).
- Roster and format management baseline implementation (Sprint 6).
- Public/tournament SHOULD features.

## Work breakdown

### Workstream A — Scoring state store and events

- Define `ScoringScreenState`, `ScoringScreenEvent`, and reducer/store equivalents.
- Keep state immutable and event-driven.
- Separate one-time effects from durable UI state.

### Workstream B — Live scorecard composables

- Build compact and expanded layouts using `WindowWidthSizeClass`.
- Display live metrics (`score/wickets/overs`, rates, striker/bowler figures).
- Render ball timeline/feed with clear event readability.

### Workstream C — Desktop scorer speed path

- Add menu actions for high-frequency operations.
- Add keyboard shortcuts for event entry and correction entry points.
- Preserve desktop-first conventions without leaking platform logic into domain.

### Workstream D — Navigation and journey continuity

- Ensure setup completion transitions into live scoring route.
- Ensure innings complete transitions into summary route.
- Keep route ownership centralized.

## Deliverables

- New scoring and scorecard shared feature packages.
- Updated route graph entries in `ScorerRoute.kt` and route handling in `App.kt`.
- Desktop interaction affordances in `desktopApp/main.kt`.
- Shared reducer/state tests for scoring interactions.

## Dependencies

- Stable domain scoring contracts from Sprint 3.
- Existing match setup route handoff from Sprint 1.

## Risks and mitigations

- Risk: scoring UI mixes business logic into composables.
  - Mitigation: keep logic in state store/use cases; composables remain declarative.
- Risk: desktop ergonomics lag mobile implementation.
  - Mitigation: explicit desktop acceptance checks and menu/shortcut stories in scope.

## Test and validation plan

- Add reducer tests for event dispatch -> state transitions.
- Add tests for loading/content/empty/error state rendering triggers.
- Add desktop-focused behavior checks for shortcut and menu intent mapping.
- Verify adaptive layout behavior by size class.

## Definition of done

- Live scoring journey is functional from setup to score summary.
- UI states are explicit and resilient (`M9`).
- Desktop scorer speed path is present (`M10`).
- UI consumes domain use cases cleanly with no domain/UI coupling violations.
