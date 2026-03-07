package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase

import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateTeamName
import cricket.knowledgespike.scorer.foundation.compose.UiText
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertIs

class ValidateTeamNameTest {

    private lateinit var validateTeamName: ValidateTeamName

    @BeforeTest
    fun setUp() {
        validateTeamName = ValidateTeamName()
    }

    @Test
    fun `test validate team name with empty string returns failure`() {
        val result = validateTeamName("")
        assertFalse(result.successful)
        assertIs<UiText.StringResourceId>(result.errorMessage)
    }

    @Test
    fun `test validate team name with blank string returns failure`() {
        val result = validateTeamName("   ")
        assertFalse(result.successful)
        assertIs<UiText.StringResourceId>(result.errorMessage)
    }

    @Test
    fun `test validate team name with valid string returns success`() {
        val result = validateTeamName("England")
        assertTrue(result.successful)
    }
}
