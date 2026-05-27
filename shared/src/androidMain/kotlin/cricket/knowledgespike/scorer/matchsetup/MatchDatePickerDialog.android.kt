package cricket.knowledgespike.scorer.matchsetup

import android.app.DatePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.datetime.LocalDate

@Composable
internal actual fun PlatformMatchDatePickerDialog(
    initialDateIso: String?,
    onDismissRequest: () -> Unit,
    onDateConfirmed: (String) -> Unit,
) {
    val context = LocalContext.current
    val initialDate = remember(initialDateIso) { resolveInitialMatchDate(initialDateIso) }

    DisposableEffect(context, initialDate) {
        var isConfirmed = false
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, zeroBasedMonth, dayOfMonth ->
                isConfirmed = true
                onDateConfirmed(
                    LocalDate(
                        year = year,
                        monthNumber = zeroBasedMonth + 1,
                        dayOfMonth = dayOfMonth,
                    ).toString(),
                )
            },
            initialDate.year,
            initialDate.monthNumber - 1,
            initialDate.dayOfMonth,
        )

        datePickerDialog.setOnCancelListener {
            if (!isConfirmed) {
                onDismissRequest()
            }
        }
        datePickerDialog.setOnDismissListener {
            if (!isConfirmed) {
                onDismissRequest()
            }
        }
        datePickerDialog.show()

        onDispose {
            datePickerDialog.setOnCancelListener(null)
            datePickerDialog.setOnDismissListener(null)
            if (datePickerDialog.isShowing) {
                datePickerDialog.dismiss()
            }
        }
    }
}