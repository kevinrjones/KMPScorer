package cricket.knowledgespike.scorer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

private val lightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    inversePrimary = InversePrimaryLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    surfaceTint = SurfaceTintLight,
    inverseSurface = InverseSurfaceLight,
    inverseOnSurface = InverseOnSurfaceLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    scrim = ScrimLight,
    surfaceBright = SurfaceBrightLight,
    surfaceContainer = SurfaceContainerLight,
    surfaceContainerHigh = SurfaceContainerHighLight,
    surfaceContainerHighest = SurfaceContainerHighestLight,
    surfaceContainerLow = SurfaceContainerLowLight,
    surfaceContainerLowest = SurfaceContainerLowestLight,
    surfaceDim = SurfaceDimLight,
    primaryFixed = PrimaryFixed,
    primaryFixedDim = PrimaryFixedDim,
    onPrimaryFixed = OnPrimaryFixed,
    onPrimaryFixedVariant = OnPrimaryFixedVariant,
    secondaryFixed = SecondaryFixed,
    secondaryFixedDim = SecondaryFixedDim,
    onSecondaryFixed = OnSecondaryFixed,
    onSecondaryFixedVariant = OnSecondaryFixedVariant,
    tertiaryFixed = TertiaryFixed,
    tertiaryFixedDim = TertiaryFixedDim,
    onTertiaryFixed = OnTertiaryFixed,
    onTertiaryFixedVariant = OnTertiaryFixedVariant,
)

private val darkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    inversePrimary = InversePrimaryDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    surfaceTint = SurfaceTintDark,
    inverseSurface = InverseSurfaceDark,
    inverseOnSurface = InverseOnSurfaceDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    scrim = ScrimDark,
    surfaceBright = SurfaceBrightDark,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    surfaceContainerHighest = SurfaceContainerHighestDark,
    surfaceContainerLow = SurfaceContainerLowDark,
    surfaceContainerLowest = SurfaceContainerLowestDark,
    surfaceDim = SurfaceDimDark,
    primaryFixed = PrimaryFixed,
    primaryFixedDim = PrimaryFixedDim,
    onPrimaryFixed = OnPrimaryFixed,
    onPrimaryFixedVariant = OnPrimaryFixedVariant,
    secondaryFixed = SecondaryFixed,
    secondaryFixedDim = SecondaryFixedDim,
    onSecondaryFixed = OnSecondaryFixed,
    onSecondaryFixedVariant = OnSecondaryFixedVariant,
    tertiaryFixed = TertiaryFixed,
    tertiaryFixedDim = TertiaryFixedDim,
    onTertiaryFixed = OnTertiaryFixed,
    onTertiaryFixedVariant = OnTertiaryFixedVariant,
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun ScorerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
