package cricket.knowledgespike.scorer.matchsetup

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ChoiceSectionDirectionalNavigationTest {

    @Test
    fun `given empty options when calculating next focus then null is returned`() {
        val nextOption = calculateNextFocusedOption(
            options = emptyList<String>(),
            selectedValue = null,
            directionStep = 1,
        )

        assertNull(nextOption)
    }

    @Test
    fun `given missing selection when moving forward then first option is returned`() {
        val nextOption = calculateNextFocusedOption(
            options = listOf("A", "B", "C"),
            selectedValue = null,
            directionStep = 1,
        )

        assertEquals("A", nextOption)
    }

    @Test
    fun `given missing selection when moving backward then last option is returned`() {
        val nextOption = calculateNextFocusedOption(
            options = listOf("A", "B", "C"),
            selectedValue = null,
            directionStep = -1,
        )

        assertEquals("C", nextOption)
    }

    @Test
    fun `given selected middle option when moving forward then next option is returned`() {
        val nextOption = calculateNextFocusedOption(
            options = listOf("A", "B", "C"),
            selectedValue = "B",
            directionStep = 1,
        )

        assertEquals("C", nextOption)
    }

    @Test
    fun `given selected middle option when moving backward then previous option is returned`() {
        val nextOption = calculateNextFocusedOption(
            options = listOf("A", "B", "C"),
            selectedValue = "B",
            directionStep = -1,
        )

        assertEquals("A", nextOption)
    }

    @Test
    fun `given selected first option when moving backward then first option remains selected`() {
        val nextOption = calculateNextFocusedOption(
            options = listOf("A", "B", "C"),
            selectedValue = "A",
            directionStep = -1,
        )

        assertEquals("A", nextOption)
    }

    @Test
    fun `given selected last option when moving forward then last option remains selected`() {
        val nextOption = calculateNextFocusedOption(
            options = listOf("A", "B", "C"),
            selectedValue = "C",
            directionStep = 1,
        )

        assertEquals("C", nextOption)
    }
}