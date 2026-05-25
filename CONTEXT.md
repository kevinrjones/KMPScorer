# KMP Cricket Scoring

This context defines the language for preparing a cricket match before scoring begins.
It keeps setup decisions explicit across Android, iOS, and Desktop.

## Language

**Match Setup**:
The pre-scoring workflow that captures required and optional match details.
_Avoid_: New game, preflight

**Start Match Gate**:
The validation boundary that must pass before scoring can start.
_Avoid_: Submit, save only

**Core Required Fields**:
Team A name, Team B name, scheduled overs, toss winner, toss decision, and match date.
_Avoid_: Full setup, extended setup

**Optional Match Details**:
Venue, umpire one, umpire two, and weather that add context but do not block starting.
_Avoid_: Mandatory metadata

**Roster**:
Player list capture that is optional in this first increment.
_Avoid_: Required lineup

## Relationships

- A **Match Setup** includes one **Start Match Gate**
- A **Start Match Gate** validates all **Core Required Fields**
- A **Match Setup** may include **Optional Match Details**
- A **Match Setup** may include a **Roster**

## Example dialogue

> **Dev:** "Can we start scoring if weather is not entered?"
> **Domain expert:** "Yes — weather is an **Optional Match Detail**, not part of the **Start Match Gate**."

## Flagged ambiguities

- "extended setup" was used to imply all metadata is required — resolved: only **Core Required Fields** block starting.
