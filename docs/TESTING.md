# Testing Strategy

This document outlines the testing strategy, frameworks, and patterns used in KMPScorer.

## 1. Overview

KMPScorer uses a multi-layered testing approach to ensure the accuracy of cricket scoring logic and the reliability of the application:

- **Unit Tests**: Focus on domain logic, state reduction (MVI), and cricket rules (e.g., over completion, extras, wicket handling).
- **Integration Tests**: Verify the interaction between domain use cases and data repositories.
- **UI Tests**: (Planned) Verify user workflows for match setup and ball-by-ball scoring.

## 2. Frameworks & Tools

- **JUnit 4**: The primary test runner for common and platform-specific tests.
- **kotlin.test**: The standard Kotlin testing library used for assertions in common code.
- **Arrow**: Used for domain modeling; tests often verify `Either` results (`isLeft`, `isRight`).
- **Kotlinx Coroutines Test**: For testing asynchronous logic and Flow-based state stores.
- **Compose UI Testing**: (Planned) For testing UI components in `:androidApp` and `:desktopApp`.

## 3. Unit Testing

Unit tests are located in the `src/commonTest` directory of the `shared` and `domain` modules. We aim for high coverage of the core scoring engine.

### Example (kotlin.test)
```kotlin
@Test
fun `given blank team A when create match setup then missing team A error is returned`() {
    val result = createMatchSetupUseCase(validDraft(teamAName = ""))

    assertTrue(result.isLeft())
    assertEquals(MatchSetupValidationError.MissingTeamAName, result.leftOrNull())
}
```

## 4. Testing State Stores (MVI)

We test our state stores by asserting on the reduced state after an event is processed.

### Example
```kotlin
@Test
fun `given required fields when start match requested then match setup is ready`() {
    val stateWithRequiredFields = MatchSetupScreenState(
        formState = MatchSetupFormState(teamAName = "Falcons", ...),
        canStartMatch = true,
    )

    val reducedState = reduceMatchSetupScreenState(
        currentState = stateWithRequiredFields,
        event = MatchSetupScreenEvent.StartMatchRequested,
        createMatchSetupUseCase = createMatchSetupUseCase,
    )

    assertTrue(reducedState.canStartMatch)
    assertIs<MatchSetupStartMatchResult.Ready>(reducedState.startMatchResult)
}
```

## 5. UI Testing (Robot Pattern)

While we are currently focusing on domain logic, UI tests should utilize the **Robot Pattern** to separate test intent from implementation details.

### The Robot Pattern (Draft)

Robots provide a domain-specific language (DSL) for interacting with the UI:

- `MatchSetupRobot`: Handles actions like entering team names, setting overs, and tossing.
- `ScoringRobot`: Handles ball-by-ball actions like recording runs, wickets, and extras.
- `ScorecardRobot`: Handles assertions on the current match state and scorecard.

## 6. Running Tests

### All Tests
```bash
./gradlew test
```

### Module Specific Tests
```bash
./gradlew :domain:allTests
./gradlew :shared:allTests
```

## 7. Best Practices

- **Descriptive Test Names**: Use backticks for readable test names (e.g., `` `given last ball of over when run scored then over is completed` ``).
- **Isolated State**: Ensure each test starts with fresh instances of use cases and state stores.
- **Domain Accuracy**: Use realistic cricket scenarios in test data to ensure the scoring engine handles edge cases correctly (e.g., no-balls on the last ball of an innings).
