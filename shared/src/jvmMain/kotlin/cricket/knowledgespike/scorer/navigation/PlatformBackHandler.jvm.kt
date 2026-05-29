package cricket.knowledgespike.scorer.navigation

import androidx.compose.runtime.Composable

@Composable
actual fun PlatformBackHandler(
    enabled: Boolean,
    onBack: () -> Unit,
) {
    // No system back button integration on desktop.
}
