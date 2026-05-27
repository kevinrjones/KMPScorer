package cricket.knowledgespike.scorer.matchsetup

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal actual fun PlatformMatchDatePickerDialog(
    initialDateIso: String?,
    onDismissRequest: () -> Unit,
    onDateConfirmed: (String) -> Unit,
) {
    val initialDate = remember(initialDateIso) { resolveInitialMatchDate(initialDateIso) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDate.toUtcEpochMillis())

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedDateMillis = datePickerState.selectedDateMillis ?: return@TextButton
                    onDateConfirmed(utcEpochMillisToLocalDate(selectedDateMillis).toString())
                },
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
    ) {
        DatePicker(state = datePickerState)
    }
}