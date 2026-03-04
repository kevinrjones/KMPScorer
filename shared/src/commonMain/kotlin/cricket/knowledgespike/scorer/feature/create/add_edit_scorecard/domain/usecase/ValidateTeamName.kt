package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase

import cricket.knowledgespike.scorer.foundation.UiText
import cricket.knowledgespike.scorer.foundation.ValidationResult
import kmpscorer.shared.generated.resources.Res
import kmpscorer.shared.generated.resources.missing_team_name

class ValidateTeamName {
    operator fun invoke(name: String) : ValidationResult {
        return if(name.isBlank()) {
            ValidationResult(false, UiText.StringResourceId(Res.string.missing_team_name))
        } else {
            ValidationResult(true)
        }
    }
}