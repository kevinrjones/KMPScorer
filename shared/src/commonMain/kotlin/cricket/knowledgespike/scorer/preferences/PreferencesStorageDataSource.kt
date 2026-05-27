package cricket.knowledgespike.scorer.preferences

import arrow.core.Either
import cricket.knowledgespike.scorer.domain.preferences.PreferencesPersistenceError

interface PreferencesStorageDataSource {
    fun readPreferencesContent(): Either<PreferencesPersistenceError, String?>

    fun writePreferencesContent(content: String): Either<PreferencesPersistenceError, Unit>
}
