package cricket.knowledgespike.scorer.foundation.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ActionIcon(
    onClick: () -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = MaterialTheme.colorScheme.inverseOnSurface
) {
    Box(
        modifier = Modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        OutlinedIconButton(
            onClick = onClick,
            modifier = modifier
                .clip(CircleShape)
                .background(backgroundColor),
            border = BorderStroke(width = 1.dp, color = tint),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = tint
            )
        }
    }
}

@Preview
@Composable
fun ActionIconPreview() {
    ActionIcon(
        onClick = {},
        icon = Icons.Filled.Delete,
        backgroundColor = MaterialTheme.colorScheme.errorContainer,
        tint = MaterialTheme.colorScheme.error,
        modifier = Modifier.height(64.dp)
            .width(64.dp)
            .padding(4.dp),
    )
}