package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase

import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateBattingSide
import cricket.knowledgespike.scorer.foundation.ValidationReason
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertIsNot

class ValidateBattingSideTest {

    private val validateBattingSide =
        ValidateBattingSide()

    @Test
    fun `test blank batting side returns failure`() {
        val result: ValidationReason = validateBattingSide("", "Team A", "Team B")
        assertIs<ValidationReason.Empty>(result)
    }

    @Test
    fun `test batting side matching team name returns success`() {
        val result = validateBattingSide("Team A", "Team A", "Team B")
        assertIs<ValidationReason.Succeeded>(result)
    }

    @Test
    fun `test batting side matching opponents name returns success`() {
        val result = validateBattingSide("Team B", "Team A", "Team B")
        assertIs<ValidationReason.Succeeded>(result)
    }

    @Test
    fun `test batting side matching neither returns failure`() {
        val result = validateBattingSide("Team C", "Team A", "Team B")
        assertIsNot<ValidationReason.Succeeded>(result)
    }
}
