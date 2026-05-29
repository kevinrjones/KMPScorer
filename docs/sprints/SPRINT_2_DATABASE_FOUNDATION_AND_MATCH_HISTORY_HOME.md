# Sprint 2 — Database foundation and match history home

## Objective

Introduce cross-platform Room persistence with schema versioning, persist match setup on start,
and deliver a new Home entry flow backed by stored match history.

## MoSCoW scope

- Primary: `M1`, `M12`
- Foundation for: `M2`, `M4`, `M7`

## Planned duration and milestone

- Duration: 2 weeks
- End-of-sprint milestone: users can create a match from setup, have it persisted to SQLite,
  and view/open saved matches from the Home screen.

## In scope

1. Establish Room + bundled SQLite persistence in KMP shared code:
   - `Match` table
   - `ScoreEvent` table
   - schema export and version baseline (`v1`)
2. Persist validated match setup on `Start Match` before navigation.
3. Route using persisted match identity (`matchId`) instead of setup payload objects.
4. Add Home as app entry route with:
   - loading/empty/content/error states
   - `New` action to open Match Setup
   - row tap navigation to read-only Match Summary
5. Add read-only Match Summary state/store/screen backed by repository query by `matchId`.
6. Add/refresh tests for mapper/repository/store navigation transitions and Home/Summary states.

## Out of scope

- Full live scoring UI and ball-by-ball action capture workflows.
- Sync/cloud replication and conflict handling.
- Advanced analytics or leaderboards.

## Work breakdown

### Workstream A — Persistence contracts and schema

- Keep domain contracts platform-neutral in `:domain`.
- Keep expected failures explicit via typed `Either` errors.
- Export Room schema and preserve migration-ready structure.

### Workstream B — Start flow persistence integration

- Keep validation ownership in `CreateMatchSetupUseCase`.
- Add `CreateAndSaveMatchUseCase` and integrate in `MatchSetupStateStore`.
- Route by `matchId` after save success; surface save failures in immutable UI state.

### Workstream C — Home and summary navigation slice

- Add Home store/screen with explicit list states.
- Add Match Summary store/screen for read-only match details.
- Keep route transitions centralized via `ScorerRoute` + `AppRouteStateStore`.

## Deliverables

- Room-backed persistence foundation for `Match` + `ScoreEvent`.
- Start-match persistence integrated before route transition.
- New Home entry route and read-only summary flow.
- Updated state/store/route tests for success + error transitions.

## Dependencies

- Sprint 1 navigation and setup validation baseline.
- Existing shared preferences wiring patterns for platform path conventions.

## Risks and mitigations

- Risk: route payload refactor impacts multiple tests.
  - Mitigation: centralize route changes and update route-store tests first.
- Risk: malformed persisted values could crash mapping.
  - Mitigation: map invalid records into typed persistence errors.

## Test and validation plan

- Mapper and repository tests for read/write + failure mapping.
- Store tests for validate -> persist -> route and persistence failure.
- Home/Summary state tests for loading/empty/content/error paths.
- Compile and run shared/domain tests plus downstream app module compile checks.

## Definition of done

- Match setup persistence works before scoring route transition.
- Home is the default route and lists persisted matches.
- Summary opens by `matchId` in read-only mode.
- Expected persistence failures are explicit typed outcomes (no normal-flow exceptions).