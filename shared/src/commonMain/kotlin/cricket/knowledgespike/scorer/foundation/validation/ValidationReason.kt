package cricket.knowledgespike.scorer.foundation.validation

sealed interface ValidationReason {
    object Succeeded: ValidationReason
    object Empty: ValidationReason
    object BattingSide: ValidationReason
    object TeamWinningToss: ValidationReason
}

