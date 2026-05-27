package cricket.knowledgespike.scorer.preferences

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cricket.knowledgespike.scorer.domain.preferences.PreferencesPersistenceError
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.SYSTEM

class OkioPreferencesStorageDataSource(
    configFilePath: String,
    private val fileSystem: FileSystem = FileSystem.SYSTEM,
) : PreferencesStorageDataSource {

    private val preferencesPath: Path = configFilePath.toPath(normalize = true)

    override fun readPreferencesContent(): Either<PreferencesPersistenceError, String?> {
        return runCatching {
            if (!fileSystem.exists(preferencesPath)) {
                null
            } else {
                fileSystem.read(preferencesPath) {
                    readUtf8()
                }
            }
        }.fold(
            onSuccess = { it.right() },
            onFailure = { PreferencesPersistenceError.UnableToReadPreferences(it.message).left() },
        )
    }

    override fun writePreferencesContent(content: String): Either<PreferencesPersistenceError, Unit> {
        return runCatching {
            preferencesPath.parent?.let(fileSystem::createDirectories)
            fileSystem.write(preferencesPath) {
                writeUtf8(content)
            }
        }.fold(
            onSuccess = { Unit.right() },
            onFailure = { PreferencesPersistenceError.UnableToWritePreferences(it.message).left() },
        )
    }
}
