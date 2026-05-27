# Tasks — Match Setup UI fixes (cross-platform)

Source request: `<issue_description>` (2026-05-27)

## Objective

Improve `Match Setup` usability and completion flow by fixing dynamic labels, keyboard focus traversal, date input UX, start-match navigation behavior, and one copy update.

## Numbered task checklist

### U1 — Dynamic Toss Winner labels

1. [x] Bind `Toss Winner` option labels to current `Team A` and `Team B` field values instead of static placeholders.
2. [x] Ensure label text updates immediately when either team name changes and remains deterministic after repeated edits.
3. [x] Keep state explicit when team names are blank (show clear fallback labels) and prevent ambiguous winner labels.
4. [x] Add/update state-level tests covering label updates and winner-selection consistency after team-name edits.

### U2 — Desktop `Tab` traversal for Toss Winner group

5. [x] Make `Toss Winner` keyboard focus behavior group-level on desktop: `Tab`/`Shift+Tab` enters/leaves the group once, not each option.
6. [x] Preserve in-group option navigation with directional keys while keeping `Tab` reserved for group-to-group traversal.
7. [x] Add/update desktop UI focus tests for forward and reverse traversal around the `Toss Winner` group.

### U3 — Desktop `Tab` traversal for Toss Decision group

8. [x] Apply the same group-level `Tab` focus model to `Toss Decision` so tabbing skips internal option-by-option stops.
9. [x] Verify parity between `Toss Winner` and `Toss Decision` keyboard behavior for accessibility consistency.
10. [x] Add/update desktop UI focus tests for forward and reverse traversal around the `Toss Decision` group.

### U4 — Match Date as date control (Android + iOS + Desktop)

11. [x] Replace free-text `Match Date` entry with a date control flow and keep a single canonical domain date representation.
12. [x] Use native date-picking UX on mobile targets where available (`Android` and `iOS`), and provide a desktop-compatible picker/dialog path.
13. [x] Ensure selected values map to the domain date format expected by validation (no exception-driven normal control flow).
14. [x] Add/update tests for valid date selection, cancellation/no-op behavior, and edge-case boundaries.

### U5 — Enable Start Match navigation + back path

15. [x] Wire `Start a Match` so valid completed setup navigates to a scoring destination.
16. [x] Add a temporary placeholder scoring screen when the final scoring UI is not yet implemented.
17. [x] Ensure users can navigate back from the placeholder destination to `Match Setup` on all targets.
18. [x] Add/update route/state tests for successful navigation and deterministic repeat behavior.

### U6 — Copy update

19. [x] Replace user-visible text `Rosters` with `Player names` in setup UI and related strings/resources.
20. [x] Verify no stale `Rosters` text remains in affected screens and previews.

## Completion checklist

21. [x] All six requested UI fixes are implemented and verified on shared logic + platform-specific UI paths.
22. [x] Domain/state tests and relevant desktop/mobile UI tests pass for impacted areas.
23. [x] Navigation and keyboard-access behavior are deterministic and documented in sprint/recap notes.
