# Sprint 4 — Correction workflows and offline durability

## Objective

Deliver trust-critical correction capabilities and offline-safe recovery so scorers can safely edit
history and continue matches after interruptions.

## MoSCoW scope

- Primary: `M4`, `M5`
- Continuation: `M12`

## Planned duration and milestone

- Duration: 2 weeks
- End-of-sprint milestone: undo/edit/replay and crash/restart recovery are deterministic,
  user-visible, and test-covered.

## In scope

1. Add correction domain contracts in `domain/src/commonMain/kotlin/.../domain/corrections/`.
2. Support delivery-level undo and historical-ball edit operations.
3. Persist correction metadata (`who`, `when`, `what changed`).
4. Add durable local persistence for active/in-progress scoring sessions.
5. Add app restart recovery path for restoring active match state.
6. Surface correction and recovery states in shared scoring state.

## Out of scope

- Competition/tournament operations (`S2`, `S6`, `S7`).
- Rich public sharing/statistics SHOULD work.

## Work breakdown

### Workstream A — Correction domain operations

- Define explicit commands for undo and point-in-time edit.
- Recompute derived state via deterministic replay after correction.
- Keep correction failures typed and user-actionable.

### Workstream B — Persistence and recovery

- Define persistence seam for scoring event log and correction log.
- Persist snapshots/checkpoints as needed for recovery speed without losing event-source truth.
- Restore active session at app start with clear resume behavior.

### Workstream C — UX state and transparency

- Add correction history and status visibility in screen state.
- Expose recovery status (`restored`, `restoration_failed`, `resume_required`) in UI model.
- Keep workflows explicit to avoid hidden state mutation.

## Deliverables

- Correction use cases and replay logic in `:domain`.
- Local persistence adapters/seams in `:shared` data layer.
- Scoring state updates to represent correction/recovery status.
- Tests covering edit/undo/replay and restart recovery.

## Dependencies

- Sprint 2 event model determinism.
- Sprint 3 live scoring UI/state contracts for correction UX integration.

## Risks and mitigations

- Risk: correction semantics differ from original scoring semantics.
  - Mitigation: single replay path for both initial scoring and corrected recompute.
- Risk: persistence corruption causes unsafe restore behavior.
  - Mitigation: checksum/version guards and fallback-safe resume states.

## Test and validation plan

- Domain tests:
  - undo latest delivery
  - edit prior delivery and replay
  - correction metadata integrity
- Shared tests:
  - persistence write/read integrity
  - restart restoration flows
  - UI state transitions for recovery outcomes

## Definition of done

- Scorers can undo and edit any previous delivery with deterministic recompute.
- Offline scoring survives process death/app restart without data corruption.
- Correction and recovery states are explicit in UI.
- Foundation is ready for MUST completion wave in Sprint 5.
