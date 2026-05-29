# Tasks — Sprint 2: Database foundation and match history Home

Source sprint definition: `docs/sprints/SPRINT_2_DATABASE_FOUNDATION_AND_MATCH_HISTORY_HOME.md`

## Sprint 2 objective

Introduce cross-platform Room persistence with schema versioning, persist validated match setup before navigation, and make Home the entry route with persisted match history and read-only summary access by `matchId`.

## Task execution rule (test-first, no separate testing phase)

For each unchecked task below, execute in the same task slice: write/adjust a failing test first, implement the minimal change to pass, then refactor.

## Numbered task checklist

### S2-A — Persistence contracts and schema baseline

1. [x] Confirm `ScorecardDatabase` includes `MatchEntity` and `ScoreEventEntity` as Room tables with explicit DAO registration.
2. [x] Confirm schema baseline is exported with `exportSchema = true` and database version constant set for Sprint 2 (`v1`).
3. [x] Confirm bundled SQLite persistence wiring is present on Android/JVM/iOS via shared `buildScorecardDatabase` and platform path providers.
4. [x] Confirm domain persistence contracts remain platform-neutral with typed expected failures (`MatchPersistenceError` and repository `Either` outcomes).
5. [x] Consolidate Room schema export history into one canonical package path under `shared/schemas` and remove/resolve legacy duplicate schema path artifacts.
6. [x] Add migration-readiness guard task: create/update a schema bootstrap test that fails fast if exported schema path/version drifts from `ScorecardDatabase` configuration.
7. [x] Document the `v1` migration baseline contract in code/docs next to `ScorecardDatabaseMigrations` so future schema bumps have an explicit starting point.

### S2-B — Start flow persistence integration

8. [x] Keep validation ownership in `CreateMatchSetupUseCase` before persistence and routing.
9. [x] Persist validated setup via `CreateAndSaveMatchUseCase` inside `MatchSetupStateStore` before any route transition.
10. [x] Keep `MatchSetupScreenState` start-flow states explicit and immutable (`Saving`, `PersistenceError`, `Saved`).
11. [x] Route from setup using persisted identity only (`ScorerRoute.ScoringEntryRoute(matchId)`), not setup payload objects.

### S2-C — Home entry flow and match summary navigation

12. [x] Keep `HomeRoute` as default app entry in `AppRouteStateStore`.
13. [x] Keep Home state machine explicit and immutable for loading/empty/content/error in `HomeStateStore` + `HomeScreenState`.
14. [x] Keep `New` action navigation from Home to `MatchSetupRoute` centralized in route/store flow.
15. [x] Align Home “open saved match” interaction with Sprint 2 contract: row tap should open read-only summary by `matchId` (or explicitly document approved deviation if inline-only actions remain).
16. [x] Keep read-only Match Summary slice backed by repository query-by-id (`MatchSummaryStateStore(matchId, matchRepository)`).
17. [x] Keep summary error handling explicit for not-found/read failures without exception-driven normal flow.

### S2-D — Sprint closure and evidence

18. [x] For every remaining unchecked Sprint 2 task, keep tests embedded in the same implementation slice (no standalone testing workstream/task list).
19. [x] After completing each remaining slice, run impacted shared/domain tests and downstream compile checks before marking the task complete.
20. [x] Record Sprint 2 completion evidence (delivered scope vs deviations) in sprint notes and `project_memory.md`.
