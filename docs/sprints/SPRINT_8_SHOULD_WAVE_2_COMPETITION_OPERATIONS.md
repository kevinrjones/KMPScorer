# Sprint 8 — SHOULD wave 2 (competition operations)

## Objective

Deliver organized-competition operations and diagnostics so the platform can support structured
league/tournament workflows with auditable outcomes.

## MoSCoW scope

- `S2`, `S4`, `S6`, `S7`, `S10`

## Planned duration and milestone

- Duration: 2 weeks
- End-of-sprint milestone: competition-grade fixtures/rules/sign-off/diagnostics are operational and
  integrated with prior scoring workflows.

## In scope

1. Tournament and fixture workflows (`S2`):
   - tournament/series setup
   - fixture scheduling
   - standings baseline updates
   - roster reuse across fixtures
2. Rain interruption and target adjustment workflows (`S4`):
   - interruption/resumption state flow
   - target adjustment module seam
   - auditable target-change log
3. Rule profiles and competition templates (`S6`):
   - configurable policies (powerplay, super over, junior variants)
4. Official sign-off and lock mode (`S7`):
   - provisional vs official score state
   - sign-off transition workflow
5. Operational observability surfaces (`S10`):
   - scoring event diagnostics
   - sync/recovery diagnostics for admin users

## Out of scope

- COULD backlog experimentation (voice input, predictive models, device connectors).
- New MUST features beyond regression stabilization.

## Work breakdown

### Workstream A — Tournament entity and fixture lifecycle

- Define tournament/fixture domain entities and transitions.
- Link fixture progression to existing match scoring lifecycle.
- Keep standings logic deterministic and test-driven.

### Workstream B — Rule template and interruption policy layer

- Add reusable competition rule templates.
- Define rain interruption and target adjustment state machine boundaries.
- Persist policy applications and rationale in audit metadata.

### Workstream C — Sign-off and governance workflow

- Model provisional/official states explicitly.
- Add scorer/admin sign-off transition rules with lock semantics.
- Prevent post-lock edits without explicit governance path.

### Workstream D — Diagnostics and operations visibility

- Surface structured logs and sync telemetry in admin-facing screens.
- Keep diagnostics readable and actionable (not raw dumps).
- Align diagnostics with correction and sync state models from prior sprints.

## Deliverables

- Domain packages for tournament flow and competition policy profiles.
- Shared feature packages for tournament operations and observability views.
- Official sign-off/lock state contracts and UI surfaces.
- Test coverage for tournament progression, policy enforcement, and lock constraints.

## Dependencies

- MUST completion baseline from Sprint 6.
- Collaboration and sharing state maturity from Sprint 7.
- Existing correction + sync state contracts from Sprints 5-6.

## Risks and mitigations

- Risk: competition templates conflict with match-level format settings.
  - Mitigation: define precedence rules and validate at fixture creation time.
- Risk: sign-off lock blocks necessary corrections.
  - Mitigation: explicit governed unlock/revision workflow with audit trail.
- Risk: diagnostics become noisy and low-signal.
  - Mitigation: curate admin-focused diagnostic summaries with clear severity/action states.

## Test and validation plan

- Domain tests for fixture progression and standings updates.
- Policy tests for interruption/resumption and target adjustment invariants.
- State tests for provisional -> official lock transitions.
- Diagnostics tests validating visibility of sync/correction telemetry.

## Definition of done

- Competition workflows are operational and auditable.
- Rule profiles, sign-off modes, and diagnostics are integrated and test-covered.
- SHOULD roadmap wave is complete without regressing MUST quality gates.
