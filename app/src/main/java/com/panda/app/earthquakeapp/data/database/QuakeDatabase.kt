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

@Dao
interface QuakeDao {

    @Query("SELECT * from quakeDatabase ORDER BY time DESC")
    suspend fun getQuakes(): List<DatabaseQuake>

    @Query("SELECT * from quakeDatabase WHERE id = :key")
    suspend fun getQuakeById(key: String): DatabaseQuake

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(quakes: List<DatabaseQuake>)

    @Query("DELETE FROM quakeDatabase")
    suspend fun clearOldData()
}