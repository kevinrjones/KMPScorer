package cricket.knowledgespike.scorer.foundation

import org.jetbrains.compose.resources.StringResource

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: StringResource? = null
) {
    val error: Boolean
        get() = !successful
}

fun isEmptyValidation(value: String, message: StringResource): ValidationResult = if(value.isBlank()) {
    ValidationResult(false, message)
} else {
    ValidationResult(true)
}