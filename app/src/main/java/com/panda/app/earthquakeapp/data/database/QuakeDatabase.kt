package com.panda.app.earthquakeapp.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Database(
    entities = [DatabaseQuake::class],
    version = 1
)
abstract class QuakeDatabase: RoomDatabase() {
    abstract val dao: QuakeDao
}

