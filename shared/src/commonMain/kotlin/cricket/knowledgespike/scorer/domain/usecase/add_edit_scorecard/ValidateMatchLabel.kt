package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.foundation.ValidationResult
import cricket.knowledgespike.scorer.foundation.isEmptyValidation
import kmpscorer.shared.generated.resources.Res
import kmpscorer.shared.generated.resources.type_of_match_label

class ValidateMatchLabel {
    operator fun invoke(match: String) : ValidationResult {
        return isEmptyValidation(match, Res.string.type_of_match_label)
    }
}

