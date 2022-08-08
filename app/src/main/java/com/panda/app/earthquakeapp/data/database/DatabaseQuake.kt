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
    val title: String,
    val time: Long,
    val url: String,
    val magnitude: Double,
    val latitude: Double,
    val longitude: Double,
    val depth: Double
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