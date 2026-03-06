package cricket.knowledgespike.scorer.foundation.room

import androidx.room.TypeConverter
import kotlin.time.Instant

@Suppress("unused")
class DateTimeConverters {
    @Suppress("unused")
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.fromEpochMilliseconds(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Instant?): Long? {
        return date?.toEpochMilliseconds()
    }
}