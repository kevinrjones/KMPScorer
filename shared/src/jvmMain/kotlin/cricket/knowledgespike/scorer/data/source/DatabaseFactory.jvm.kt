package cricket.knowledgespike.scorer.data.source

import androidx.room.Room
import androidx.room.RoomDatabase
import com.knowledgespike.scorer.data.source.ScorecardDatabase
import java.io.File

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<ScorecardDatabase> {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")

        val appDataDirectory = when {
            os.contains("mac") -> File(userHome, "Library/Application Support/Scorecard")
            os.contains("win") -> File(System.getenv("APPDATA"), "Scorecard")
            else -> File(userHome, ".local/share/Scorecard")
        }

        if (!appDataDirectory.exists()) {
            appDataDirectory.mkdirs()
        }

        val dbFile = File(appDataDirectory, ScorecardDatabase.DATABASE_NAME)
        return Room.databaseBuilder(dbFile.absolutePath)
    }
}