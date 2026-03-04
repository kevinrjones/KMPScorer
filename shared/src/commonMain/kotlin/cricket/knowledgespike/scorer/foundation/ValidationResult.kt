package cricket.knowledgespike.scorer.foundation

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: UiText? = null
)