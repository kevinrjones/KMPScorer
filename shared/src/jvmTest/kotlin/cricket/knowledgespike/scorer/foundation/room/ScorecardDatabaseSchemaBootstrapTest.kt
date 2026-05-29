package cricket.knowledgespike.scorer.foundation.room

import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ScorecardDatabaseSchemaBootstrapTest {

    @Test
    fun `given sprint 2 baseline when schema folders are inspected then only canonical scorecard schema package exists`() {
        val schemaRoot = resolveSchemaRoot()

        val scorecardSchemaPackages = Files.list(schemaRoot).use { directories ->
            directories
                .filter(Files::isDirectory)
                .map { it.fileName.toString() }
                .filter { it.endsWith("ScorecardDatabase") }
                .sorted()
                .toList()
        }

        assertEquals(
            listOf("cricket.knowledgespike.scorer.foundation.room.ScorecardDatabase"),
            scorecardSchemaPackages,
        )
    }

    @Test
    fun `given schema version constant when exported schema is inspected then matching version json exists`() {
        val schemaJson = resolveSchemaRoot()
            .resolve("cricket.knowledgespike.scorer.foundation.room.ScorecardDatabase")
            .resolve("$ScorecardDatabaseSchemaVersion.json")

        assertTrue(Files.exists(schemaJson), "Expected schema file: $schemaJson")
        assertTrue(
            Files.readString(schemaJson).contains("\"version\": $ScorecardDatabaseSchemaVersion"),
            "Expected schema version $ScorecardDatabaseSchemaVersion in $schemaJson",
        )
    }

    @Test
    fun `given migration baseline contract when inspected then baseline version is explicitly declared as v1`() {
        assertEquals(1, ScorecardDatabaseMigrations.BaselineSchemaVersion)
    }

    private fun resolveSchemaRoot(): Path {
        val candidatePaths = listOf(
            Path.of("schemas"),
            Path.of("shared", "schemas"),
        )

        return candidatePaths.firstOrNull(Files::exists)
            ?: error("Unable to resolve Room schema root. Checked: $candidatePaths")
    }
}