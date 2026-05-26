# Sprint 0 — UI tidy-up placeholder

## Objective

Create a short pre-sprint placeholder for tidying the current UI before feature-heavy delivery
starts.

## MoSCoW scope

- Placeholder sprint (no direct `M*`/`S*` mapping).
- Supports smoother execution of Sprint 1 by reducing obvious UI rough edges.

## Planned duration and milestone

- Duration: up to 1 week (time-boxed placeholder).
- End-of-sprint milestone: UI polish candidates are documented and a small bounded tidy-up pass is
  complete.

## In scope

1. Tidy visual/layout rough edges in the existing match setup experience.
2. Apply low-risk consistency improvements to spacing, alignment, and content framing.
3. Preserve current safe-area/scaffold behavior and existing match setup flow.
4. Capture follow-up UI items that should move to later planned sprints.

## Out of scope

- New scoring, roster, sync, or tournament capabilities.
- Domain model/rule changes.
- Any re-planning of Sprint 1-7 feature scope.

## Placeholder workstreams

### Workstream A — Existing UI baseline tidy-up

- Focus on the currently implemented setup surface only.
- Keep changes small, reversible, and implementation-safe.

### Workstream B — Scope control and handoff

- Record what is fixed now vs deferred.
- Keep MUST-first roadmap intact after this placeholder sprint.

## Suggested touchpoints

- `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/App.kt`
- `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/matchsetup/MatchSetupScreen.kt`
- `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/matchsetup/MatchSetupScreenState.kt`

## Risks and mitigations

- Risk: placeholder expands into unbounded redesign.
  - Mitigation: strict time-box and explicit non-goals.
- Risk: Sprint 1 delivery slips due to pre-sprint churn.
  - Mitigation: treat this as polish-only and preserve Sprint 1 scope unchanged.

## Definition of done

- Sprint 0 exists as a documented placeholder in the sprint pack.
- UI tidy-up scope is bounded and does not alter roadmap priorities.
- Sprint 1 remains the first MUST-mapped implementation sprint.
