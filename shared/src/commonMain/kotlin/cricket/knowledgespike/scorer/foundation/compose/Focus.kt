package cricket.knowledgespike.scorer.foundation.compose

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.key.isShiftPressed

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.moveFocusOnTab(focusManager: FocusManager): Modifier = this.then(
    onPreviewKeyEvent { event ->
        if (event.type == KeyEventType.KeyDown && event.key == Key.Tab) {
            focusManager.moveFocus(
                if (event.isShiftPressed) FocusDirection.Previous else FocusDirection.Next
            )
            true
        } else {
            false
        }
    }
)