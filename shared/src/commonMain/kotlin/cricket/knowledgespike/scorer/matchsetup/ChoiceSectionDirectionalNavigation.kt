package cricket.knowledgespike.scorer.matchsetup

internal fun <T> calculateNextFocusedOption(
    options: List<T>,
    selectedValue: T?,
    directionStep: Int,
): T? {
    if (options.isEmpty()) {
        return null
    }

    val selectedIndex = options.indexOf(selectedValue)
    if (selectedIndex < 0) {
        return if (directionStep > 0) options.first() else options.last()
    }

    val nextIndex = (selectedIndex + directionStep).coerceIn(0, options.lastIndex)
    return options[nextIndex]
}