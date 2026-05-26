## Naming Conventions

Use names that describe intent.

Prefer:

- `LoadUserUseCase`
- `UserRepository`
- `UserScreenState`
- `UserScreenEvent`
- `UserDetailsRoute`
- `toDomain`
- `toUiState`
- `toAppError`

Avoid vague names:

- `Manager`
- `Helper`
- `Util`
- `Handler`
- `Processor`

If a utility is necessary, prefer a domain-specific name.

---

## Code Review Checklist

Before submitting changes, verify:

- Koin is used only for dependency injection at boundaries.
- No arbitrary `get()` or `inject()` calls are added.
- Domain code is platform-neutral.
- Expected failures are modeled with Arrow types.
- UI state is explicit and immutable.
- Composables are small and mostly stateless.
- `WindowWidthSizeClass` is used for adaptive layout decisions.
- Navigation uses Navigation 3 patterns and centralized route definitions.
- Cyclomatic complexity remains low.
- Large functions are split into smaller named functions.
- Tests cover domain logic and state transitions.
- No platform-specific APIs leak into common code.
- No expected business failure is represented by throwing an exception.

---

## Things Agents Must Avoid

Do not:

- Use Koin as a service locator.
- Add global mutable state.
- Hide dependencies inside objects or singleton accessors.
- Add business logic directly into composables.
- Scatter navigation route strings throughout the project.
- Use raw screen width checks when a size class is available.
- Introduce unnecessary inheritance.
- Use exceptions for normal domain/control flow.
- Add deeply nested branching.
- Add large catch-all utility files.
- Couple domain code to UI, platform, Koin, or data implementations.

---

## Preferred Implementation Style

When making changes:

1. Model the domain first.
2. Represent failure explicitly.
3. Keep dependencies constructor-injected.
4. Keep UI state immutable.
5. Keep composables declarative.
6. Use size classes for adaptive layout.
7. Keep navigation centralized.
8. Extract complex logic into pure functions.
9. Add or update tests.
10. Keep functions small and easy to reason about.

## User Interface

For the mobile applications use standard mobile UI patterns, for the desktop application use
standard desktop patterns such as menus

The goal is a Kotlin Multiplatform codebase that is explicit, functional, testable, adaptive, and
easy to maintain.

## Project Memory

After each sprint or completed task, update `docs/project_memory.md` with:

- What was shipped
- Key decisions
- Gotchas
- Title
- Date/time completed
- Test coverage areas
