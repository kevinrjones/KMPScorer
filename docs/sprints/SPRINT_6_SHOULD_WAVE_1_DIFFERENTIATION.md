# Sprint 6 — SHOULD wave 1 (differentiation)

## Objective

Deliver the first differentiation wave after MUST completion, focused on sharing, collaboration,
advanced insights, desktop workflow quality, and accessibility.

## MoSCoW scope

- `S1`, `S3`, `S5`, `S8`, `S9`

## Planned duration and milestone

- Duration: 2 weeks
- End-of-sprint milestone: product meaningfully exceeds baseline scorer apps in collaboration,
  consumption, and desktop/usability quality.

## In scope

1. Public/live match sharing baseline (`S1`):
   - spectator-facing score view
   - share entry (link/code)
   - match status indicators (`Live`, `Innings Break`, `Result`)
2. Advanced stats baseline (`S3`):
   - worm/manhattan/wagon-wheel foundations
   - season/tournament leaderboard seams
3. Collaborative handover (`S5`):
   - active scorer identity state
   - lock/handover control states
4. Desktop workspace uplift (`S8`):
   - denser workspace options
   - stronger keyboard-first workflow
5. Accessibility improvements (`S9`) across all new surfaces.

## Out of scope

- Tournament/fixture operations and rain adjustments (Sprint 7).
- COULD backlog features such as predictive analytics and streaming hooks.

## Work breakdown

### Workstream A — Sharing and spectator experience

- Add route(s) and state models for public score viewing.
- Expose match lifecycle state clearly to non-scorer viewers.
- Keep internal scorer actions separated from public presentation flows.

### Workstream B — Statistics baseline

- Define chart-ready data contracts in shared/domain seams.
- Start with deterministic aggregates; avoid speculative prediction logic.
- Keep visual components adaptive by size class.

### Workstream C — Collaboration and lock semantics

- Model scorer ownership and lock states explicitly.
- Ensure handover transitions are auditable and deterministic.
- Prevent conflicting concurrent edit paths in UI/state layer.

### Workstream D — Desktop + accessibility hardening

- Extend menu and keyboard affordances for advanced workflows.
- Add semantic labels and contrast-safe components.
- Validate scalable typography and responsive spacing behavior.

## Deliverables

- Shared feature packages for sharing/statistics/collaboration state.
- Route and UI updates for spectator/public views.
- Desktop-focused interaction enhancements and accessibility updates.
- State tests for collaboration lock/handover and public match status transitions.

## Dependencies

- MUST-complete baseline from Sprint 5.
- Stable scoring event model and summary contracts from Sprints 2-5.

## Risks and mitigations

- Risk: public sharing leaks scorer-only controls.
  - Mitigation: explicit view-role boundary in state and route model.
- Risk: collaboration introduces race conditions.
  - Mitigation: lock-state-first workflow with deterministic transition rules.
- Risk: accessibility is treated as post-processing.
  - Mitigation: include accessibility checks in definition-of-done, not post-sprint.

## Test and validation plan

- State tests for handover/lock transitions and conflict scenarios.
- UI behavior checks for spectator status updates and stats data consistency.
- Accessibility checks for labels, contrast, and scalable typography.
- Desktop workflow checks for keyboard-driven critical paths.

## Definition of done

- Sharing, statistics baseline, and collaboration workflows are usable and test-covered.
- Desktop workspace quality and accessibility improvements are visibly delivered.
- MUST quality gates remain green with no regressions.
