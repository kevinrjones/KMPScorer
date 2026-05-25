# Project Guidelines - KMPScorer

This document outlines the architecture, coding standards, and best practices for the KMPScorer project.

## 1. Architecture: Clean Architecture

The project follows Clean Architecture principles to ensure separation of concerns and testability.

### Layers:
- **Domain Layer (`:domain` module)**: 
    - Pure Kotlin module.
    - Contains **Entities** (Models), **Repository Interfaces**, and **Use Cases**.
    - Should have no dependencies on Android, Compose, or the Data layer.
- **Data Layer (`:shared` module, `data` package)**:
    - Implements Repository interfaces defined in the Domain layer.
    - Handles data sources (SQLDelight, API, etc.) and Mappers.
- **Presentation Layer (`:shared` module, `feature` package)**:
    - Follows **MVI (Model-View-Intent)**.
    - Contains ViewModels, UI States, and Compose UI.

## 2. Presentation Layer Patterns (MVI)

### Components per Feature
Each feature (e.g., `score_match`) should be organized in its own package under `feature/` and include:
- `XxxViewModel`: Extends `androidx.lifecycle.ViewModel`.
- `XxxState`: Data class representing the entire UI state.
- `XxxAction` (or `UiEvent`): Sealed interface for user interactions.
- `XxxScreenRoot`: Composable that handles DI and state collection.
- `XxxScreen`: Stateless Composable for the UI.

### Guidelines:
- **State Management**: Expose a single `StateFlow<XxxState>`.
- **Side Effects**: Use a `SharedFlow` for one-time events like navigation or toasts.
- **Consistency**: Use `onIntent` or `onEvent` as the single entry point for user actions in the ViewModel.
- **Previews**: Always provide a `@Preview` for the stateless `XxxScreen` composable using a mock state.

## 3. Dependency Injection
- Uses **Koin**.
- Modules are defined in the `di` package.
- ViewModels should be injected using `koinViewModel()` (or equivalent for the platform).

## 4. Coding Standards

### Naming Conventions
- **ViewModels**: Must end in `ViewModel`. (Avoid typos like `VewModel`).
- **States**: Must end in `State`.
- **Actions/Intents**: Use `Action` or `UiEvent` for user triggers.
- **UseCases**: Should be named by their action, e.g., `GetScorecardsUseCase`.

### Formatting & Style
- Follow standard Kotlin coding conventions.
- Prefer `sealed interface` for Actions and Events.
- Keep Composables small and focused.

## 5. Technology Stack
- **Kotlin Multiplatform (KMP)**
- **Compose Multiplatform** for UI.
- **Koin** for Dependency Injection.
- **SQLDelight** for local persistence.
- **Kotlinx Coroutines & Flow** for reactivity.
- **Arrow** for functional programming utilities (e.g., `Either`).
