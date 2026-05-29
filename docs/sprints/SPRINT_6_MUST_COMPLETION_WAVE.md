# Sprint 6 — MUST completion wave

## Objective

Complete the MUST baseline by delivering roster management, multi-format templates, and sync/export
fundamentals on top of the stable scoring core.

## MoSCoW scope

- Primary: `M6`, `M8`, `M11`
- Completion checkpoint: full `M1`–`M12`

## Planned duration and milestone

- Duration: 2 weeks
- End-of-sprint milestone: all MUST capabilities are present at baseline depth and can support an
  MVP release candidate.

## In scope

1. Add roster domain and feature flows:
   - `domain/src/commonMain/kotlin/.../domain/roster/`
   - `shared/src/commonMain/kotlin/.../roster/`
2. Add format profile contracts and setup flows:
   - `domain/src/commonMain/kotlin/.../domain/format/`
   - `shared/src/commonMain/kotlin/.../format/`
3. Add export/sync state foundations:
   - `domain/src/commonMain/kotlin/.../domain/export/`
   - `shared/src/commonMain/kotlin/.../syncstatus/`
4. Ensure setup and scoring flows consume roster/format state consistently.
5. Execute MUST completion verification for `M1` through `M12`.

## Out of scope

- SHOULD wave enhancements (`S1`-`S10`) beyond enabling seams.
- COULD/WON'T backlog items.

## Work breakdown

### Workstream A — Roster baseline (`M8`)

- Team/player create-select flows for pre-match setup.
- Reusable roster persistence for future matches.
- Role markers (captain/wicketkeeper) in domain and UI state.

### Workstream B — Format baseline (`M11`)

- Limited-overs templates and two-innings-style configuration.
- Explicit profile parameters for innings count, over limits, and wickets assumptions.
- Setup integration with validation through typed outcomes.

### Workstream C — Sync/export fundamentals (`M6`)

- Export-ready scorecard contract (JSON/CSV baseline).
- Explicit sync status model: `pending`, `synced`, `failed`, `conflict`.
- UI visibility for sync status and conflict awareness.

### Workstream D — MUST closure gate

- Validate all previously delivered MUST slices are still intact.
- Resolve integration edge cases between setup/scoring/corrections/roster/format.

## Deliverables

- New roster, format, and export domain contracts.
- Shared feature packages for roster/format/sync status.
- Cross-flow integration from setup to scoring with new baseline features.
- MUST completion matrix showing `M1`-`M12` baseline coverage.

## Dependencies

- Correction and persistence foundations from Sprint 5.
- Stable setup and scoring route flow from Sprints 1-4.

## Risks and mitigations

- Risk: adding roster/format introduces setup complexity regressions.
  - Mitigation: progressive disclosure in UI state and strict validation in domain use cases.
- Risk: sync status is implemented as hidden internal state.
  - Mitigation: sync state is a first-class, user-visible screen state contract.

## Test and validation plan

- Domain tests for roster constraints, role selection rules, and format validation.
- Shared tests for setup integration and sync status transitions.
- Export contract tests for deterministic serialization shape.
- MUST regression pass across previously completed features.

## Definition of done

- All MUST IDs (`M1`–`M12`) are baseline-delivered and traceable.
- Roster and format setup works for real match creation workflows.
- Sync/export state is explicit and test-covered.
- Product is ready to enter SHOULD differentiation waves.
