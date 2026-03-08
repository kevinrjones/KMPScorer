package cricket.knowledgespike.scorer.foundation.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cricket.knowledgespike.scorer.ui.theme.ScorerTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun Swipeable(
    isLeftRevealed: Boolean,
    isRightRevealed: Boolean = false,
    actions: @Composable (RowScope.() -> Unit)? = null,
    secondaryActions: @Composable (RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier,
    onExpanded: () -> Unit = {},
    onSecondaryExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {},
    content: @Composable () -> Unit
) {
    var contextMenuWidth by remember {
        mutableFloatStateOf(0f)
    }
    var secondaryContextMenuWidth by remember {
        mutableFloatStateOf(0f)
    }
    val offset = remember {
        Animatable(initialValue = 0f)
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isLeftRevealed, isRightRevealed, contextMenuWidth, secondaryContextMenuWidth) {
        if (isLeftRevealed && contextMenuWidth > 0) {
            offset.animateTo(contextMenuWidth)
        } else if (isRightRevealed && secondaryContextMenuWidth > 0) {
            offset.animateTo(-secondaryContextMenuWidth)
        } else {
            offset.animateTo(0f)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        if (actions != null) {
            Row(
                modifier = Modifier
                    .onSizeChanged {
                        contextMenuWidth = it.width.toFloat()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                actions()
            }
        }

        if (secondaryActions != null) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .onSizeChanged {
                        secondaryContextMenuWidth = it.width.toFloat()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                secondaryActions()
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .pointerInput(contextMenuWidth, secondaryContextMenuWidth) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = (offset.value + dragAmount)
                                    .coerceIn(-secondaryContextMenuWidth, contextMenuWidth)
                                offset.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            when {
                                offset.value >= contextMenuWidth / 2f && contextMenuWidth > 0 -> {
                                    scope.launch {
                                        offset.animateTo(contextMenuWidth)
                                        onExpanded()
                                    }
                                }

                                offset.value <= -secondaryContextMenuWidth / 2f && secondaryContextMenuWidth > 0 -> {
                                    scope.launch {
                                        offset.animateTo(-secondaryContextMenuWidth)
                                        onSecondaryExpanded()
                                    }
                                }

                                else -> {
                                    scope.launch {
                                        offset.animateTo(0f)
                                        onCollapsed()
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            content()
        }
    }
}


@Preview
@Composable
fun SwipeablePreviewLeft() {
    ScorerTheme {
        Swipeable(
            isLeftRevealed = true,
            actions = {
                ActionIcon(
                    onClick = {},
                    icon = Icons.Filled.Delete,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .height(64.dp)
                        .width(64.dp)
                        .padding(4.dp),
                )
            },
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(128.dp)
                        .background(color = MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Some Text")
                }
            })
    }
}

@Preview
@Composable
fun SwipeablePreviewRight() {
    ScorerTheme {
        Swipeable(
            isLeftRevealed = false,
            isRightRevealed = true,
            secondaryActions = {
                ActionIcon(
                    onClick = {},
                    icon = Icons.Filled.Delete,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .height(64.dp)
                        .width(64.dp)
                        .padding(4.dp),
                )
            },
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(128.dp)
                        .background(color = MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Some Text")
                }
            })
    }
}
