package com.knowledgespike.scorer.data.source

import androidx.room.AutoMigration
import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cricket.knowledgespike.scorer.data.entity.ScorecardEntity
import cricket.knowledgespike.scorer.data.source.ScorecardDatabaseConstructor
import cricket.knowledgespike.scorer.foundation.room.DateTimeConverters

@Database(
    entities = [ScorecardEntity::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3)
    ]
)
@TypeConverters(DateTimeConverters::class)
@ConstructedBy(ScorecardDatabaseConstructor::class)
abstract class ScorecardDatabase : RoomDatabase() {

    abstract val scorecardDao: ScorecardDao

    companion object {
        const val DATABASE_NAME = "scorecards_db"
    }
}


