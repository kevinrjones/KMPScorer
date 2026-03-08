package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase

import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateBattingSide
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidateBattingSideTest {

    private val validateBattingSide =
        _root_ide_package_.cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateBattingSide()

    @Test
    fun `test blank batting side returns failure`() {
        val result = validateBattingSide("", "Team A", "Team B")
        assertFalse(result.successful)
    }

    @Test
    fun `test batting side matching team name returns success`() {
        val result = validateBattingSide("Team A", "Team A", "Team B")
        assertTrue(result.successful)
    }

    @Test
    fun `test batting side matching opponents name returns success`() {
        val result = validateBattingSide("Team B", "Team A", "Team B")
        assertTrue(result.successful)
    }

    @Test
    fun `test batting side matching neither returns failure`() {
        val result = validateBattingSide("Team C", "Team A", "Team B")
        assertFalse(result.successful)
    }
}
