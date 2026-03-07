package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.foundation.ValidationResult
import cricket.knowledgespike.scorer.foundation.isEmptyValidation
import kmpscorer.shared.generated.resources.Res
import kmpscorer.shared.generated.resources.missing_team_name

class ValidateTeamName {
    operator fun invoke(name: String) : ValidationResult = isEmptyValidation(name, Res.string.missing_team_name)
}

