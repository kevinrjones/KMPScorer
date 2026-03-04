package cricket.knowledgespike.scorer.foundation

import org.jetbrains.compose.resources.StringResource

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: UiText? = null
)

fun isEmptyValidation(value: String, message: StringResource): ValidationResult = if(value.isBlank()) {
    ValidationResult(false, UiText.StringResourceId(message))
} else {
    ValidationResult(true)
}