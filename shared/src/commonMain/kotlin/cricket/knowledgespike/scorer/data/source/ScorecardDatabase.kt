package com.knowledgespike.scorer.data.source

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import cricket.knowledgespike.scorer.data.entity.ScorecardEntity
import cricket.knowledgespike.scorer.data.source.ScorecardDatabaseConstructor

@Database(entities = [ScorecardEntity::class], version = 1)
@ConstructedBy(ScorecardDatabaseConstructor::class)
abstract class ScorecardDatabase : RoomDatabase() {

    abstract val scorecardDao: ScorecardDao

    companion object {
        const val DATABASE_NAME = "scorecards_db"
    }
}