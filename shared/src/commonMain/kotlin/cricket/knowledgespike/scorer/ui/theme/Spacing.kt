package cricket.knowledgespike.scorer.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ScorerSpacingScale(
    val XSmall: Dp,
    val Small: Dp,
    val Medium: Dp,
    val Large: Dp,
    val XLarge: Dp,
    val XXLarge: Dp,
    val CompactContentMaxWidth: Dp,
    val MediumContentMaxWidth: Dp,
    val ExpandedContentMaxWidth: Dp,
)

val ScorerSpacing = ScorerSpacingScale(
    XSmall = 4.dp,
    Small = 8.dp,
    Medium = 12.dp,
    Large = 16.dp,
    XLarge = 24.dp,
    XXLarge = 32.dp,
    CompactContentMaxWidth = 640.dp,
    MediumContentMaxWidth = 860.dp,
    ExpandedContentMaxWidth = 1240.dp,
)