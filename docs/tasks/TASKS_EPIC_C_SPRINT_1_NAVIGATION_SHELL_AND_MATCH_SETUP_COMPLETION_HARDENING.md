# Tasks — Epic C: Regression tests and quality gates

Source epic definition: `docs/tasks/EPICS_SPRINT_1_NAVIGATION_SHELL_AND_MATCH_SETUP_COMPLETION_HARDENING.md` (Epic C)

## Epic C objective

Close Sprint 1 with explicit regression coverage, cross-module verification, and architecture gate evidence so setup validation and setup-to-route handoff behavior remain deterministic, test-covered, and maintainable.

## Numbered task checklist

### C1 — Domain validation regression tests

1. [x] Review `CreateMatchSetupUseCase` validation outcomes and map each required-field failure to a domain test expectation.
2. [x] Verify required-field failure coverage for blank team names and missing toss selections.
3. [x] Verify invalid-value failure coverage for scheduled overs and match date format.
4. [x] Verify team-name distinctness rule coverage.
5. [x] Verify valid setup success path remains covered with normalization expectations.
6. [x] Verify optional-field omission acceptance remains covered and produces explicit nullable optionals.
7. [x] Verify deterministic behavior for repeated identical `CreateMatchSetupUseCase` invocations.
8. [x] Confirm expected business failures remain explicit typed outcomes (no exception-driven normal flow).

### C2 — Shared state/reducer transition tests

9. [x] Verify reducer/store invalid-start path keeps user on setup state and exposes actionable validation error state.
10. [x] Verify valid-start path transitions to `Ready` and emits `ScorerRoute.ScoringEntryRoute` intent.
11. [x] Verify repeated `StartMatchRequested` actions produce deterministic route intent payloads.
12. [x] Verify validation-error recovery path after user correction transitions to ready and emits route intent.
13. [x] Verify start-gate readiness projection remains consistent after relevant field updates.
14. [x] Confirm tests are platform-neutral and deterministic (`commonTest` coverage only for shared logic).

### C3 — Cross-module compile and downstream confidence checks

15. [x] Run domain and shared JVM test suites relevant to setup validation and route handoff.
16. [x] Run `:desktopApp` compile verification for downstream shared usage.
17. [x] Run `:androidApp` debug-source compile verification for downstream shared usage.
18. [x] Confirm no regressions across impacted modules after Epic A/B/C changes.
19. [x] Record command evidence for verification run in sprint notes.

### C4 — Architecture checklist gate

20. [x] Confirm no Koin service-locator-style lookups were introduced.
21. [x] Confirm `:domain` remains platform-neutral.
22. [x] Confirm expected failures are represented explicitly (no exception control flow for normal invalid input).
23. [x] Confirm navigation routes remain centralized and typed.
24. [x] Confirm UI state updates remain immutable and transition logic complexity remains low.
25. [x] Add architecture-gate evidence to sprint documentation.

## Epic C completion checklist

26. [x] Domain validation regression scenarios are covered across positive, negative, and edge cases.
27. [x] Shared setup transition tests verify valid/invalid/deterministic route intent behavior.
28. [x] Cross-module test and compile checks pass for impacted modules.
29. [x] Architecture checklist constraints are satisfied with explicit evidence references.
