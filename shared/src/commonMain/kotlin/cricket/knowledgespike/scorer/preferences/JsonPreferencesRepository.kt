package cricket.knowledgespike.scorer.preferences

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import cricket.knowledgespike.scorer.domain.preferences.AppPreferences
import cricket.knowledgespike.scorer.domain.preferences.PreferencesPersistenceError
import cricket.knowledgespike.scorer.domain.preferences.PreferencesRepository
import kotlinx.serialization.json.Json

class JsonPreferencesRepository(
    private val preferencesStorageDataSource: PreferencesStorageDataSource,
    private val json: Json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        prettyPrint = true
    },
) : PreferencesRepository {

    override fun loadPreferences(): Either<PreferencesPersistenceError, AppPreferences> {
        return preferencesStorageDataSource.readPreferencesContent().flatMap { rawContent ->
            if (rawContent.isNullOrBlank()) {
                AppPreferences().right()
            } else {
                runCatching {
                    json.decodeFromString<AppPreferencesConfigDto>(rawContent).toDomain()
                }.fold(
                    onSuccess = { it.right() },
                    onFailure = { PreferencesPersistenceError.UnableToReadPreferences(it.message).left() },
                )
            }
        }
    }

    override fun savePreferences(preferences: AppPreferences): Either<PreferencesPersistenceError, Unit> {
        return runCatching {
            json.encodeToString(preferences.toConfigDto())
        }.fold(
            onSuccess = preferencesStorageDataSource::writePreferencesContent,
            onFailure = { PreferencesPersistenceError.UnableToWritePreferences(it.message).left() },
        )
    }
}

object InMemoryPreferencesRepository : PreferencesRepository {
    private var currentPreferences: AppPreferences = AppPreferences()

    override fun loadPreferences(): Either<PreferencesPersistenceError, AppPreferences> {
        return currentPreferences.right()
    }

    override fun savePreferences(preferences: AppPreferences): Either<PreferencesPersistenceError, Unit> {
        currentPreferences = preferences
        return Unit.right()
    }
}
