package com.panda.app.earthquakeapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.panda.app.earthquakeapp.data.QuakeMapItem
import com.panda.app.earthquakeapp.domain.model.Quake
import java.util.concurrent.Flow

@Entity(tableName = "quakeDatabase")
data class DatabaseQuake constructor(
    @PrimaryKey val id: String,
    val title: String = "",
    val time: Long = System.currentTimeMillis(),
    val url: String = "",
    val magnitude: Double = 0.0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val depth: Double = 0.0
)

fun DatabaseQuake.toQuake(): Quake {
    return Quake(
        id = id,
        title = title,
        time = time,
        url = url,
        magnitude = magnitude,
        coordinate = LatLng(latitude, longitude),
        depth = depth
    )
}