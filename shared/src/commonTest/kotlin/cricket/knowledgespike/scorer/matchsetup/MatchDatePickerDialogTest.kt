package cricket.knowledgespike.scorer.matchsetup

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class MatchDatePickerDialogTest {

    @Test
    fun `given null initial date when resolving initial match date then today date is returned`() {
        val todayDate = LocalDate(year = 2026, monthNumber = 5, dayOfMonth = 27)

        val resolvedDate = resolveInitialMatchDate(initialDateIso = null, todayDate = todayDate)

        assertEquals(todayDate, resolvedDate)
    }

    @Test
    fun `given blank initial date when resolving initial match date then today date is returned`() {
        val todayDate = LocalDate(year = 2026, monthNumber = 5, dayOfMonth = 27)

        val resolvedDate = resolveInitialMatchDate(initialDateIso = "   ", todayDate = todayDate)

        assertEquals(todayDate, resolvedDate)
    }

    @Test
    fun `given valid initial date when resolving initial match date then parsed date is returned`() {
        val todayDate = LocalDate(year = 2026, monthNumber = 5, dayOfMonth = 27)

        val resolvedDate = resolveInitialMatchDate(initialDateIso = "2026-08-03", todayDate = todayDate)

        assertEquals(LocalDate(year = 2026, monthNumber = 8, dayOfMonth = 3), resolvedDate)
    }
}
