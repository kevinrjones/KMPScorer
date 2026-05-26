# KMP Scorer feature review and MoSCoW backlog

## Purpose

This document captures:

1. A state-of-the-art review of current cricket scoring products across mobile and desktop/laptop
   usage.
2. A detailed MoSCoW feature list for `KMPScorer`.

The goal is to define a practical, implementation-ready product scope for a Kotlin Multiplatform
cricket scoring app that supports Android, iOS, and Desktop.

---

## Research snapshot (as of 2026-05-25)

### Sources reviewed

- CricHeroes listings and feature descriptions (Google Play + App Store)
- Play-Cricket Scorer Pro feature page (desktop/laptop scorer)
- Play-Cricket Scorer App Store listing (mobile scorer)
- NV Play Cricket Scorer App Store listing
- NV Play ecosystem/club rollout references
- CricClubs ecosystem references (league + scoring workflow positioning)

### Representative products and notable capabilities

| Product                                                    | Platforms                                                | Notable capabilities surfaced                                                                                                                                                                                         | Strategic takeaway                                                                                              |
|------------------------------------------------------------|----------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------|
| CricHeroes                                                 | Android, iOS (mobile-first ecosystem)                    | Ball-by-ball live scoring, match analytics (wagon wheel, manhattan, worm, run-rate), tournament management, live streaming, AI highlights, player profiles/leaderboards, community discovery, offline scoring signals | Mobile-first apps now bundle scoring + operations + fan engagement; users expect more than a digital scorebook  |
| Play-Cricket Scorer (mobile) + Scorer Pro (desktop/laptop) | Mobile + Windows laptop (Mac via Parallels patterns)     | Strong traditional scorer workflow, keyboard-oriented desktop operation, extensive editing/undo, offline capture with deferred upload, scoreboard operations, DLS handling, broad format support, laws/rules updates  | Desktop remains important for serious/official scorers; speed, control, and recoverability are differentiators  |
| NV Play ecosystem                                          | Mobile + Pro desktop software + governing-body workflows | Collaborative scoring, account-governed access, live scoring, streaming/video add-ons, junior format support, DLS updates, sync consistency controls, scorecard-only entry options                                    | Enterprise/association usage requires role controls, compliance, and operational reliability beyond casual apps |
| CricClubs ecosystem                                        | Mobile + web/league management positioning               | Integrated league/tournament setup, scheduling, live scoring, scorecards, tables, role-oriented workflows                                                                                                             | League administration and scoring are converging into one workflow surface                                      |

---

## State-of-the-art themes (mobile + desktop)

### 1) Scoring is now an ecosystem, not a single screen

Modern products combine:

- Match setup
- Live scoring
- Statistical analysis
- Public/live publishing
- Tournament administration
- Player profile history

Implication for `KMPScorer`: even if releases are phased, architecture should anticipate this full
workflow.

### 2) Offline-first with robust reconciliation is baseline

Leading tools support:

- Reliable scoring with no network
- Automatic or controlled sync when online
- Recovery tools for sync inconsistency

Implication: offline persistence and conflict strategy are not optional for production-grade
scoring.

### 3) Desktop/laptop remains core for advanced scorers

State-of-the-art desktop expectations:

- Keyboard-first input speed
- Multi-pane customizable scoring workspace
- Rich edit/undo and correction flows
- Integration with external scoreboards/feeds

Implication: desktop UX should not be a stretched mobile layout; it needs dedicated interaction
design.

### 4) Rule engines are increasingly explicit

Advanced scorers expect built-in support for:

- Competition-specific rules
- DLS/rain handling
- Junior/variant formats
- Continuous law updates

Implication: model rules as explicit, testable domain logic rather than ad hoc UI checks.

### 5) Data visibility and storytelling matter

Users now expect:

- Live scorecards for remote followers
- Visual analytics (worm/manhattan/wagon wheel)
- Post-match summaries
- Shareable links/cards

Implication: output and presentation features materially affect adoption, not just core data entry.

### 6) League operations and scoring are converging

Winning products align fixtures, rosters, officiating, scoring, and publishing in a single workflow.

Implication: model entities and IDs so tournament workflows can be added without rewriting scoring
core.

### 7) Reliability controls are a competitive feature

Products expose tools like:

- Edit-at-ball granularity
- Validation guardrails
- Upload/sync status controls
- Error recovery paths

Implication: trust is earned through transparent correction and audit behavior.

---

## MoSCoW for KMPScorer

### MoSCoW definitions

- **MUST**: required for v1 viability and credible usage in real matches.
- **SHOULD**: high-value features that significantly improve competitiveness and adoption after
  MUST.
- **COULD**: strategic enhancements with clear value, but not required for near-term release.
- **WON’T (for now)**: intentionally deferred to protect focus and delivery speed.

---

## MUST have

### M1. Match Setup and Start Match Gate

- Implement explicit `Match Setup` with required fields from current domain language:
    - Team A
    - Team B
    - Scheduled overs
    - Toss winner
    - Toss decision
    - Match date
- Block scoring until the `Start Match Gate` is valid.
- Keep optional details explicit (venue, umpires, weather, roster).

### M2. Full ball-by-ball scoring engine

- Record each delivery with complete scoring outcomes:
    - Legal delivery / dot / runs
    - Extras (wide, no-ball, byes, leg-byes, penalty)
    - Wicket types and dismissal attribution
    - Striker/non-striker changes
    - Bowler over progression
- Preserve immutable event history plus derived inning state.

### M3. Core cricket rules and guardrails

- Enforce legal state transitions for overs, wickets, innings, and strike rotation.
- Detect invalid operations (e.g., impossible bowler changes) before commit.
- Support configurable over length and innings count per format profile.

### M4. In-match correction workflow (trust-critical)

- Delivery-level undo.
- Edit any previous ball with deterministic recalculation.
- Change bowler/striker and dismissal correction paths.
- Keep correction log metadata (`who`, `when`, `what changed`).

### M5. Offline-first data model

- Score a complete match with no internet.
- Local durable persistence for in-progress matches.
- Resume interrupted sessions safely after app restart/crash.

### M6. Sync and export fundamentals

- Deterministic sync model between device and backend (when backend exists).
- Visible sync state (`pending`, `synced`, `failed`, `conflict`).
- Exportable scorecard (at minimum JSON/CSV; PDF optional in later phases).

### M7. Real-time scorecard presentation

- Live innings summary:
    - Score/wickets/overs
    - Current run rate
    - Required run rate (chase)
    - Batter and bowler figures
- Ball-by-ball feed view.
- Completed match summary screen.

### M8. Team/player roster management (local first)

- Create/select teams and players quickly before match start.
- Persist reusable rosters for future matches.
- Mark captain/wicketkeeper roles.

### M9. Explicit UI states and resilience

- Every primary screen handles:
    - Loading
    - Content
    - Empty
    - Error
- Error messages should guide the next action (retry, edit, continue offline).

### M10. Platform-specific ergonomics

- **Mobile**:
    - Fast tap targets for high-pressure scoring moments
    - One-hand-friendly input flow
- **Desktop**:
    - Keyboard shortcuts for common events
    - Dedicated menu actions (e.g., `Match -> New Match`, `Undo Last Ball`, `End Innings`)
    - Multi-pane scorer-oriented layout

### M11. Multi-format match support (core formats)

- At minimum support:
    - Limited overs (e.g., T20, 40/50-over style)
    - Two-innings style configuration via format template
- Format profile defines innings structure, over limits, and wickets/roster assumptions.

### M12. Domain-safe architecture foundation

- Keep scoring domain logic platform-neutral and testable.
- Model expected failures as typed outcomes (not exception-driven control flow).
- Use immutable UI state and explicit screen events.

---

## SHOULD have

### S1. Public/live match sharing

- Share live score link or match code.
- Spectator-friendly public scorecard with auto-refresh.
- Match status badges (`Live`, `Innings Break`, `Stumps`, `Result`).

### S2. Tournament and fixture workflows

- Create tournament/series.
- Schedule fixtures.
- Auto-update points table and basic standings.
- Reuse team rosters across fixtures.

### S3. Advanced statistics pack

- Visualizations:
    - Worm graph
    - Manhattan graph
    - Wagon wheel (batting)
- Leaderboards by tournament/season.

### S4. Rain interruptions and result methods

- DLS or configurable target-adjustment module.
- Explicit interruption/resumption workflow.
- Audit trail for target changes.

### S5. Collaborative scoring and handover

- Allow handoff to another authenticated scorer.
- Protect against concurrent conflicting edits.
- Show active scorer identity and lock state.

### S6. Rule profiles and competition templates

- Competition-level rule configuration:
    - Powerplay rules
    - Super over policy
    - Retired out/hurt policy
    - Junior variants (configurable)
- Save templates for repeated use.

### S7. Review/approval mode for official competitions

- “Provisional score” and “officially locked score” states.
- Post-match sign-off workflow for scorer/admin.

### S8. Strong desktop scorer workspace

- Configurable panel layout.
- Enhanced keyboard-only flow.
- High-density event timeline for deep review.

### S9. Accessibility and inclusive UX

- Screen reader labels on key controls.
- High-contrast support.
- Scalable typography and responsive spacing.

### S10. Observability and diagnostics

- Structured event logs for scoring actions.
- Crash-safe recovery telemetry.
- Sync diagnostics visible to admin users.

---

## COULD have

### C1. Live streaming hooks

- Link scoreboard data to video stream overlays.
- Basic stream metadata integration.

### C2. Automated highlight packaging

- Tag notable moments (boundaries, wickets, milestones).
- Export highlight markers for external video tools.

### C3. Predictive analytics

- Win probability and projected totals.
- Context-aware chase pressure indicators.

### C4. Voice-assisted scorer input

- Optional voice shortcuts for common events.
- Confirmation prompts before commit.

### C5. Coach and player performance dashboards

- Session/season trends for batting and bowling.
- Workload and form indicators.

### C6. External scoreboard/device connectors

- Integrate with popular digital scoreboard protocols.
- Health indicator for device connection status.

### C7. Rich notifications

- Milestone notifications for followers.
- Configurable alert subscriptions by match/team.

### C8. Multi-language UI/content support

- Locale-aware formats and labels.
- Language packs for regions with high grassroots cricket usage.

### C9. Import/migration utilities

- Import teams/players/fixtures from CSV or existing systems.
- Conflict preview before final import commit.

### C10. Smart assistant workflows

- Suggest likely next actions (e.g., striker swap checks, innings closure checks).
- Warning prompts for uncommon but legal scenarios.

---

## WON’T have (for now)

### W1. Full social network layer

- No feed-first social product (posts/comments/follow graph) in early releases.

### W2. Fantasy gaming and wagering features

- Out of scope for scorer reliability roadmap.

### W3. Computer-vision auto-scoring

- Camera-driven ball classification is deferred due to complexity and reliability risk.

### W4. Deep broadcast production suite

- Full in-app live production/graphics studio deferred; prioritize clean data APIs first.

### W5. Complex sponsorship/commerce tooling

- Advertising inventory and monetization dashboards are not early priorities.

### W6. Large-scale federation ERP features

- Complex finance/compliance/reporting modules for governing bodies are out of initial scope.

---

## Prioritized delivery slices (recommended)

### Slice A: Trusted scorer core (MUST baseline)

- M1, M2, M3, M4, M5, M7, M9, M10 (minimum desktop keyboard actions), M12

### Slice B: Reuse and portability

- M8, M11, M6

### Slice C: Competitive differentiation

- S1, S3, S5, S8

### Slice D: Competition-grade operations

- S2, S4, S6, S7, S10

### Slice E: Strategic extensions

- Selected COULD items based on product traction and partner demand

---

## Product quality gates for this backlog

Each implemented feature should satisfy:

1. **Rule correctness**: behavior is deterministic and tested at domain level.
2. **Recoverability**: users can correct mistakes without data loss.
3. **Offline resilience**: match can proceed without network.
4. **Cross-platform clarity**: same domain outcomes on Android, iOS, Desktop.
5. **Operational transparency**: visible status for sync, lock, and publication state.

---

## Summary

The current market standard for cricket scoring is no longer just run/wicket entry. Competitive
products combine reliable scoring, correction safety, offline continuity, publish/share flows, and
progressively deeper analytics and tournament operations. The MoSCoW set above defines a practical
path for `KMPScorer`: first establish a trusted multi-platform scoring core, then layer visibility,
collaboration, and competition-grade operations.
