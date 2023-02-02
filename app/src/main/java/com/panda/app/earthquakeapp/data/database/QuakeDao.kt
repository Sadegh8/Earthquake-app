package com.panda.app.earthquakeapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuakeDao {

    @Query("SELECT * from quakeDatabase ORDER BY time DESC")
    suspend fun getQuakes(): List<DatabaseQuake>

    @Query("SELECT * from quakeDatabase WHERE id = :key")
    suspend fun getQuakeById(key: String): DatabaseQuake

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(quakes: List<DatabaseQuake>)

    @Query("DELETE FROM quakeDatabase WHERE time < :time")
    suspend fun clearOldData(time: Long)
}