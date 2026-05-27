# KMPScorer

[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Compose for Desktop](https://img.shields.io/badge/Compose%20for%20Desktop-1.9.0-4285F4?logo=jetbrains&logoColor=white)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

`KMPScorer` is a Kotlin Multiplatform cricket scoring app targeting Android, iOS, and Desktop (JVM), with shared domain and UI logic built using Compose Multiplatform.

## Core Features

- **Cross-platform match setup flow** with required start-gate validation (teams, overs, toss winner/decision, match date).
- **Optional match metadata capture** (venue, umpires, weather) for richer setup context.
- **Adaptive UI behavior** driven by `WindowWidthSizeClass` for mobile and desktop layouts.
- **Cross-session preferences persistence** for active theme selection and desktop window size/position.
- **MRU history tracking** for recently accessed matches.
- **Desktop-native workflow support** including menu entry (`Match -> New Match Setup`) and persisted window restore.

## Project Structure

- [`domain`](./domain): platform-neutral domain models/use-cases.
- [`shared`](./shared): shared Compose UI, state stores, navigation, and cross-platform logic.
  - [`commonMain`](./shared/src/commonMain/kotlin): common Kotlin sources.
  - Platform-specific source sets (`androidMain`, `iosMain`, `jvmMain`) for target-specific integration points.
- [`androidApp`](./androidApp): Android application entry point.
- [`iosApp`](./iosApp): iOS host application (Xcode project).
- [`desktopApp`](./desktopApp): Desktop JVM entry point and packaging.

## Screenshots

Screenshot placeholders are defined under [`docs/images/screenshots`](./docs/images/screenshots).

| Placeholder file | Description |
| --- | --- |
| `android-match-setup.png` | Android Match Setup screen showing required and optional sections. |
| `desktop-match-setup.png` | Desktop Match Setup screen in expanded layout. |
| `desktop-theme-menu.png` | Desktop menu with theme preference options (`System`, `Light`, `Dark`). |
| `desktop-window-restore.png` | Desktop app restored to previously saved window size and position. |

See [`docs/images/screenshots/README.md`](./docs/images/screenshots/README.md) for capture notes.

## Getting Started

### Prerequisites

- JDK 17+ (recommended for modern Android Gradle Plugin tooling).
- macOS, Linux, or Windows.
- One of:
  - Android Studio or IntelliJ IDEA for Android/Desktop development.
  - Xcode (macOS only) for iOS builds.

### Clone and sync

```bash
git clone <your-repo-url>
cd KMPScorer
./gradlew --version
```

On Windows, use `gradlew.bat` instead of `./gradlew`.

### Run the Desktop app

```bash
./gradlew :desktopApp:run
```

### Build the Android app

```bash
./gradlew :androidApp:assembleDebug
```

To install/run on a connected device or emulator, use your IDE run configuration for `androidApp`.

### Run the iOS app

1. Open [`iosApp`](./iosApp) in Xcode.
2. Select a simulator/device.
3. Run the `iosApp` target.

### Build all major targets

```bash
./gradlew :shared:assemble :desktopApp:assemble :androidApp:assembleDebug
```

### Optional verification checks

```bash
./gradlew :shared:jvmTest :desktopApp:compileKotlin :androidApp:compileDebugSources
```

## Documentation

- Domain glossary and language context: [`CONTEXT.md`](./CONTEXT.md)
- Feature review and roadmap context: [`docs/FEATURES.md`](./docs/FEATURES.md)
- Architecture decision record for setup validation boundary: [`docs/adr/0001-core-start-gate-for-match-setup.md`](./docs/adr/0001-core-start-gate-for-match-setup.md)

## License

License is currently marked as **TBD** in this repository. Add a top-level `LICENSE` file and update the badge/link when finalized.
