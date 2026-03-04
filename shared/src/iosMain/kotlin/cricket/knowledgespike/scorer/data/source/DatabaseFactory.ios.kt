package cricket.knowledgespike.scorer.data.source

import androidx.room.Room
import androidx.room.RoomDatabase
import com.knowledgespike.scorer.data.source.ScorecardDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<ScorecardDatabase> {
        val dbFile = documentDirectory() + "/${ScorecardDatabase.DATABASE_NAME}"
        return Room.databaseBuilder<ScorecardDatabase>(dbFile)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory() : String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    return requireNotNull(documentDirectory?.path)
}
