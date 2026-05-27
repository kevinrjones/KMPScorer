# Tasks — Epic A: Route model and application shell orchestration

Source epic definition: `docs/tasks/EPICS_SPRINT_1_NAVIGATION_SHELL_AND_MATCH_SETUP_COMPLETION_HARDENING.md` (Epic A)

## Epic A objective

Deliver a centralized, strongly typed route model and route-driven app shell flow so `Match Setup` can transition deterministically into scoring without scattered navigation logic.

## Numbered task checklist

### A1 — Audit current route surface and call sites

1. [x] Create an Epic A audit note section in the active sprint/PR notes for route inventory evidence.
2. [x] Inspect `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/navigation/ScorerRoute.kt` and list every currently defined route type.
3. [x] Inspect `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/App.kt` and document how the current route is selected for rendering.
4. [x] Search shared UI/state files for all usages of `ScorerRoute` (or route-like decisions) and record each consumer location.
5. [x] Classify each existing transition path as explicit (state/event driven) or implicit (UI side effect).
6. [x] Identify and record missing setup-completion destination route(s), starting with scoring entry.
7. [x] Confirm the inventory includes all route consumers before any route-contract changes begin.

### A2 — Expand centralized route contract in `ScorerRoute.kt`

8. [x] Define the new typed route contract entries needed for setup-completion navigation.
9. [x] Verify route naming follows existing intent-revealing conventions used in `ScorerRoute`.
10. [x] Add the new route type(s) to `ScorerRoute.kt` as the single source of truth.
11. [x] Ensure no new string-based route constants are introduced outside centralized route definitions.
12. [x] Update all compile-time references impacted by the new route type(s).
13. [x] Confirm route contract changes compile conceptually across shared consumers (Android/iOS/Desktop entry points).

### A3 — Refactor `App.kt` to route-driven rendering

14. [x] Identify the app-level state path that provides the current route to `App.kt`.
15. [x] Refactor `App.kt` screen selection to render from explicit current route instead of fixed setup-only rendering.
16. [x] Preserve existing safe-area/scaffold behavior while introducing route-based rendering.
17. [x] Verify setup screen remains reachable and behaviorally unchanged before completion transition is triggered.
18. [x] Validate that route-driven rendering logic remains in shared/common code (no platform-specific API leakage).

### A4 — Validate route handoff boundaries

19. [x] Define and document the single owner for setup -> route intent emission (store/reducer boundary).
20. [x] Ensure `App.kt` consumes emitted route intent only, without re-deriving business validation logic.
21. [x] Remove or prevent duplicated navigation-start logic in composables.
22. [x] Verify transition behavior is deterministic for repeated start attempts.
23. [x] Add/update test cases (or test plan items) proving valid setup emits the expected route intent and invalid setup does not transition.
24. [x] Record final Epic A completion evidence linking inventory notes, transition ownership decision, and route handoff validation results.

## Epic A completion checklist

25. [x] Route inventory and impacted-consumer map are documented and complete.
26. [x] Setup-completion route type(s) are centralized in `ScorerRoute.kt` with no scattered route strings.
27. [x] `App.kt` renders from explicit route state and preserves shell behavior.
28. [x] Setup -> route handoff ownership is singular, documented, and test-validated.
