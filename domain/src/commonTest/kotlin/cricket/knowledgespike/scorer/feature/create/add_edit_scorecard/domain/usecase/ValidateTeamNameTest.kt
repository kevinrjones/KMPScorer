package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase

import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateTeamName
import cricket.knowledgespike.scorer.foundation.ValidationReason
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs

class ValidateTeamNameTest {

    private lateinit var validateTeamName: ValidateTeamName

    @BeforeTest
    fun setUp() {
        validateTeamName =
            ValidateTeamName()
    }

    @Test
    fun `test validate team name with empty string returns failure`() {
        val result = validateTeamName("")
        assertIs<ValidationReason.Empty>(result)
    }

    @Test
    fun `test validate team name with blank string returns failure`() {
        val result = validateTeamName("   ")
        assertIs<ValidationReason.Empty>(result)
    }

    @Test
    fun `test validate team name with valid string returns success`() {
        val result = validateTeamName("England")
        assertIs<ValidationReason.Succeeded>(result)
    }
}
