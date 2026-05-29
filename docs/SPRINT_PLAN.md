# KMPScorer sprint plan

## Purpose

This document translates `docs/FEATURES.md` into an implementation sequence that is grounded in the
current codebase baseline and module structure.

It is the delivery plan for moving from today’s `Match Setup` slice to a competition-ready
cross-platform scoring product.

## Current baseline (confirmed)

- Modules in scope: `:domain`, `:shared`, `:androidApp`, `:iosApp`, `:desktopApp`
  (`settings.gradle.kts`).
- Existing implemented flow anchors:
  - `domain/src/commonMain/kotlin/cricket/knowledgespike/scorer/domain/matchsetup/CreateMatchSetupUseCase.kt`
  - `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/matchsetup/MatchSetupStateStore.kt`
  - `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/navigation/ScorerRoute.kt`
  - `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/App.kt`
- Existing patterns to preserve:
  - Typed domain outcomes (`Either`) for expected validation failures.
  - Immutable `ScreenState` + explicit `ScreenEvent` + reducer/store flow.
  - Centralized route definitions via `ScorerRoute`.

## Scope and assumptions

### In scope for this delivery plan

- MUST-first delivery sequence, then SHOULD waves.
- Sprint outcomes, architectural touchpoints, and quality gates.
- Explicit traceability from MoSCoW IDs (`M*`, `S*`, `C*`, `W*`) to sprint waves.

### Out of scope for this delivery plan

- Implementing any code in this planning task.
- Pulling COULD scope (`C1`-`C10`) into committed sprint scope.
- Pulling WON'T scope (`W1`-`W6`) into roadmap without an explicit re-plan.

### Planning assumptions

- Two-week sprints.
- One cross-platform squad delivering vertical slices through `:domain` then `:shared`.
- Domain-first sequencing: model rules/use cases before UI event wiring.

## Sprint sequence (MUST first, SHOULD follow-up)

### Sprint 0 — UI tidy-up placeholder

#### Outcome

- A lightweight pre-sprint placeholder exists for initial UI polish before feature-heavy delivery.

#### MoSCoW coverage

- Placeholder sprint (no direct `M*`/`S*` mapping).
- Purpose: reduce obvious UI rough edges in the existing match setup flow.

#### Module/file touchpoints

- `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/App.kt`
- `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/matchsetup/MatchSetupScreen.kt`
- `shared/src/commonMain/kotlin/cricket/knowledgespike/scorer/matchsetup/MatchSetupScreenState.kt`

#### Definition of done

- UI tidy-up scope is documented as a bounded placeholder (not a feature expansion sprint).
- Existing setup behavior remains intact while visual/layout polish candidates are identified.
- Sprint 1 scope and MUST-first sequencing remain unchanged.

### Sprint 1 — Navigation shell and setup completion hardening

#### Outcome

- App shell supports route-driven flow from match setup into scoring journey entry.

#### MoSCoW coverage

- Primary: `M1`, `M12`

#### Module/file touchpoints

- `shared/.../navigation/ScorerRoute.kt`
- `shared/.../App.kt`
- `shared/.../matchsetup/MatchSetupStateStore.kt`
- `shared/.../matchsetup/MatchSetupScreenState.kt`

#### Definition of done

- Setup flow remains valid and emits explicit navigation intent.
- Route handling remains centralized and test-covered.
- No platform-specific APIs leak into `:domain`.

### Sprint 2 — Database foundation and match history home

#### Outcome

- Match setup persistence and match history home flow are delivered on a versioned SQLite foundation.

#### MoSCoW coverage

- Primary: `M1`, `M12`
- Foundation for: `M2`, `M4`, `M7`

#### Module/file touchpoints

- `domain/src/commonMain/kotlin/.../domain/repository/*` (new persistence contracts)
- `shared/src/commonMain/kotlin/.../foundation/room/*` (new)
- `shared/src/commonMain/kotlin/.../data/entity/*` and `.../data/source/*`
- `shared/src/commonMain/kotlin/.../home/*` (new)
- `shared/src/commonMain/kotlin/.../matchsummary/*` (new)
- `shared/src/commonMain/kotlin/.../navigation/ScorerRoute.kt`
- platform entry points (`MainActivity.kt`, `MainViewController.kt`, `desktopApp/main.kt`)

#### Definition of done

- `Start Match` persists validated setup before route transition.
- Home is app entry and lists persisted matches with explicit load states.
- Match tap opens read-only summary by `matchId`.
- Room schema baseline is exported and migration-ready (`v1`).

### Sprint 3 — Ball-by-ball scoring domain core

#### Outcome

- Deterministic, testable scoring core exists with legal transitions and typed failures.

#### MoSCoW coverage

- Primary: `M2`, `M3`, `M12`

#### Module/file touchpoints

- `domain/src/commonMain/kotlin/.../domain/scoring/*` (new)
- `domain/src/commonMain/kotlin/.../domain/rules/*` (new)
- `domain/src/commonMain/kotlin/.../domain/matchsetup/*` (integration seams)

#### Definition of done

- Delivery events and derived innings state are deterministic.
- Illegal operations are blocked with explicit error types.
- Domain tests cover nominal, invalid, and edge transitions.

### Sprint 4 — Live scoring UI and scorecard views

#### Outcome

- Scorers can run a live innings flow end-to-end across platforms.

#### MoSCoW coverage

- Primary: `M7`, `M9`, `M10`
- Secondary continuation: `M2`, `M3`

#### Module/file touchpoints

- `shared/src/commonMain/kotlin/.../scoring/*` (new)
- `shared/src/commonMain/kotlin/.../scorecard/*` (new)
- `shared/src/commonMain/kotlin/.../navigation/ScorerRoute.kt`
- `desktopApp/src/main/kotlin/cricket/knowledgespike/scorer/main.kt`

#### Definition of done

- UI has explicit loading/content/empty/error states.
- Desktop includes dedicated scorer ergonomics (menu/shortcut support).
- Mobile and desktop use shared state contracts, not duplicated business logic.

### Sprint 5 — Correction workflows and offline durability

#### Outcome

- Scorers can trust edits/undo and safely resume interrupted matches.

#### MoSCoW coverage

- Primary: `M4`, `M5`
- Secondary continuation: `M12`

#### Module/file touchpoints

- `domain/src/commonMain/kotlin/.../domain/corrections/*` (new)
- `domain/src/commonMain/kotlin/.../domain/scoring/*`
- `shared/src/commonMain/kotlin/.../scoring/*`
- `shared/src/commonMain/kotlin/.../data/*` (persistence adapters/seams)

#### Definition of done

- Delivery-level undo and historical-ball edit are deterministic.
- Correction metadata (`who`, `when`, `what changed`) is persisted.
- Crash/restart recovery restores active match state without corruption.

### Sprint 6 — MUST completion wave

#### Outcome

- All MUST items are covered at MVP-capable baseline depth.

#### MoSCoW coverage

- Primary: `M6`, `M8`, `M11`
- Completion checkpoint: full `M1`-`M12`

#### Module/file touchpoints

- `domain/src/commonMain/kotlin/.../domain/roster/*` (new)
- `domain/src/commonMain/kotlin/.../domain/format/*` (new)
- `domain/src/commonMain/kotlin/.../domain/export/*` (new)
- `shared/src/commonMain/kotlin/.../roster/*` (new)
- `shared/src/commonMain/kotlin/.../format/*` (new)
- `shared/src/commonMain/kotlin/.../syncstatus/*` (new)

#### Definition of done

- Roster selection/persistence works for pre-match flows.
- Format templates support limited overs and two-innings configuration.
- Sync/export state is explicit (`pending`, `synced`, `failed`, `conflict`).

### Sprint 7 — SHOULD wave 1 (differentiation)

#### Outcome

- Product expands beyond MVP into collaboration and richer match insights.

#### MoSCoW coverage

- `S1`, `S3`, `S5`, `S8`, `S9`

#### Module/file touchpoints

- `shared/src/commonMain/kotlin/.../sharing/*` (new)
- `shared/src/commonMain/kotlin/.../statistics/*` (new)
- `shared/src/commonMain/kotlin/.../scoring/*`
- `desktopApp/src/main/kotlin/cricket/knowledgespike/scorer/main.kt`

#### Definition of done

- Live/public score sharing is available with clear match status.
- Collaboration lock/handover state is explicit and test-covered.
- Accessibility and desktop workflow improvements are verified.

### Sprint 8 — SHOULD wave 2 (competition operations)

#### Outcome

- Product reaches organized-competition operational readiness.

#### MoSCoW coverage

- `S2`, `S4`, `S6`, `S7`, `S10`

#### Module/file touchpoints

- `domain/src/commonMain/kotlin/.../domain/tournament/*` (new)
- `domain/src/commonMain/kotlin/.../domain/rules/*`
- `shared/src/commonMain/kotlin/.../tournament/*` (new)
- `shared/src/commonMain/kotlin/.../observability/*` (new)

#### Definition of done

- Fixtures/standings and competition templates behave consistently.
- Rain/target adjustment has explicit audit trail.
- Sign-off/official lock and diagnostics states are visible and test-covered.

## Feature-to-sprint traceability

### MUST mapping

`Sprint 0` is intentionally a placeholder and is not mapped to `M*` IDs.

| Feature ID | Sprint wave |
| --- | --- |
| `M1` | Sprint 1 (harden + route handoff), Sprint 6 (final integration pass) |
| `M2` | Sprint 3 (domain core), Sprint 4 (UI integration) |
| `M3` | Sprint 3 (rules), Sprint 4 (interaction validation) |
| `M4` | Sprint 5 |
| `M5` | Sprint 5 |
| `M6` | Sprint 6 |
| `M7` | Sprint 4 |
| `M8` | Sprint 6 |
| `M9` | Sprint 4 (baseline), then enforced each sprint |
| `M10` | Sprint 4 |
| `M11` | Sprint 6 |
| `M12` | Sprint 1-8 cross-cutting quality gate |

### SHOULD mapping

| Feature ID | Sprint wave |
| --- | --- |
| `S1` | Sprint 7 |
| `S2` | Sprint 8 |
| `S3` | Sprint 7 |
| `S4` | Sprint 8 |
| `S5` | Sprint 7 |
| `S6` | Sprint 8 |
| `S7` | Sprint 8 |
| `S8` | Sprint 7 |
| `S9` | Sprint 7 |
| `S10` | Sprint 8 |

### Deferred scope (explicitly not in planned waves)

- `COULD`: `C1`-`C10`
- `WON'T`: `W1`-`W6`

## Dependencies and risks

### Key dependencies

1. `M2/M3` scoring core must be stable before `M7` live scorecard behaviors.
2. Event history determinism is prerequisite for `M4` corrections and `M5` recovery.
3. Roster/format models (`M8/M11`) must settle before fixture/tournament SHOULD work (`S2`, `S6`).
4. Sync/export state model (`M6`) should exist before diagnostics (`S10`) can be meaningful.

### Delivery risks and mitigations

- **Risk:** domain-rule complexity creates late regressions.
  - **Mitigation:** prioritize high-volume domain transition tests in Sprint 3 before UI scale-out.
- **Risk:** correction and offline persistence drift out of sync.
  - **Mitigation:** use immutable event history and deterministic replay as the source of truth.
- **Risk:** desktop UX gets treated as stretched mobile UI.
  - **Mitigation:** enforce desktop-specific workflow checkpoints in Sprints 4 and 7.
- **Risk:** SHOULD scope starts before MUST closure.
  - **Mitigation:** Sprint 6 includes explicit `M1`-`M12` completion gate before Sprint 7 starts.

## Quality gates and definition-of-done checkpoints

### Cross-sprint quality gates

- `:domain` remains platform-neutral and free from UI/platform dependencies.
- Expected business failures stay typed (no exception-driven normal flow).
- UI state stays immutable and event-driven (`ScreenState` + `ScreenEvent` + reducer/store).
- Navigation remains centralized through `ScorerRoute`.
- Adaptive layout decisions continue to use `WindowWidthSizeClass` where layout splits are needed.

### Validation checkpoints

- **Sprint 0 checkpoint:** placeholder UI tidy-up scope is documented and bounded.
- **Sprint 1 checkpoint:** setup-to-scoring route transition is explicit and reducer-tested.
- **Sprint 2 checkpoint:** persisted setup + home history + summary route flow are validated.
- **Sprint 3 checkpoint:** domain rules pass deterministic transition test suite.
- **Sprint 4 checkpoint:** live scoring UI/state transitions cover loading/content/empty/error.
- **Sprint 5 checkpoint:** undo/edit/replay and restart recovery scenarios are test-covered.
- **Sprint 6 checkpoint:** all MUST IDs (`M1`-`M12`) are marked baseline-delivered.
- **Sprint 7-8 checkpoint:** SHOULD items delivered without regressing MUST quality gates.