# Tasks — Epic B: Match Setup flow hardening and transition safety

Source epic definition: `docs/tasks/EPICS_SPRINT_1_NAVIGATION_SHELL_AND_MATCH_SETUP_COMPLETION_HARDENING.md` (Epic B)

## Epic B objective

Harden `Match Setup` validation and setup-completion transition behavior so required `M1` inputs are enforced, start-gate authority remains in domain, and setup-to-route handoff is explicit, deterministic, and UI-safe.

## Numbered task checklist

### B1 — Re-validate setup field requirements against `M1`

1. [x] Create an Epic B validation-audit note section in sprint/PR notes to capture field-by-field findings.
2. [x] List all required `M1` setup inputs and map each one to its source field in `MatchSetupFormState` / `MatchSetupScreenState`.
3. [x] Trace each required field through `MatchSetupStateStore` event handling to confirm it reaches `CreateMatchSetupUseCase` input unchanged.
4. [x] Enumerate all optional setup inputs and verify they are represented explicitly (not inferred via hidden defaults).
5. [x] Verify that omitted optional fields do not block start when all required fields are valid.
6. [x] Verify that each missing/invalid required field yields explicit, user-visible error state mapping.
7. [x] Confirm no fallback/default behavior can silently satisfy required fields and bypass validation.
8. [x] Document deterministic validation expectations for repeated identical inputs and repeated start attempts.

### B2 — Keep `CreateMatchSetupUseCase` as start-gate authority

9. [x] Identify every call site that decides start eligibility and confirm the gate path flows through `CreateMatchSetupUseCase`.
10. [x] Remove or reject any parallel business-validation logic in UI/store layers that duplicates domain eligibility rules.
11. [x] Verify store/reducer passes complete draft data to use case before any transition intent is emitted.
12. [x] Ensure use-case validation failures are translated into explicit `MatchSetupScreenState` failure representation.
13. [x] Confirm expected business failures use explicit result modeling (no exception-driven control flow for normal invalid input).
14. [x] Add/update architectural evidence notes showing domain gate authority and non-authority of UI/composables.

### B3 — Harden state transitions in `MatchSetupStateStore`

15. [x] Define and document the canonical transition states: editing/idle, validation-failed, completion-intent-emitted.
16. [x] Verify `StartMatchRequested` transition logic emits completion intent only from a valid `Ready` result.
17. [x] Ensure one-shot route intent behavior is safe for consumers (clear emission contract and consumption/reset expectations).
18. [x] Validate repeated rapid `StartMatchRequested` actions keep state consistent and deterministic.
19. [x] Verify validation error state is recoverable after user correction without requiring store recreation.
20. [x] Confirm state remains immutable and update logic avoids global mutable state or hidden singleton dependencies.
21. [x] Record transition table (event -> prior state -> next state -> emitted intent/no intent) for key start-flow paths.

### B4 — Ensure composables remain declarative and thin

22. [x] Audit `MatchSetupScreen` composables to confirm they dispatch events only and do not run business validation logic.
23. [x] Confirm composables consume immutable state projections from store/viewmodel layer and do not mutate domain/setup data directly.
24. [x] Verify all visible screen states are representable and wired in UI (content, validation-error, and in-progress if applicable).
25. [x] Ensure no route construction logic is introduced inside composables; navigation intent emission remains outside UI rendering.
26. [x] Check composable functions for size/complexity and split any oversized handlers into smaller named pure helpers where needed.
27. [x] Capture final UI-boundary evidence note confirming unidirectional event -> state -> render flow for setup screen.

## Epic B completion checklist

28. [x] Required `M1` field validation behavior is mapped, verified, and documented end-to-end.
29. [x] `CreateMatchSetupUseCase` remains the single authoritative start gate with explicit failure modeling.
30. [x] `MatchSetupStateStore` transition behavior is explicit, deterministic, and safe under repeated actions.
31. [x] `MatchSetupScreen` composables remain declarative, mostly stateless, and free of business validation logic.
