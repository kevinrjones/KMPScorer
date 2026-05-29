package cricket.knowledgespike.scorer.navigation

import androidx.compose.runtime.Composable

@Composable
actual fun PlatformBackHandler(
    enabled: Boolean,
    onBack: () -> Unit,
) {
    // iOS back gestures/buttons are managed by platform navigation containers.
}
