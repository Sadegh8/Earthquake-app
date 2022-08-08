package com.panda.app.earthquakeapp.data.dto

import com.panda.app.earthquakeapp.data.database.DatabaseQuake

data class Feature(
    val geometry: Geometry,
    val id: String,
    val properties: Properties,
    val type: String
)

fun Feature.toQuake(): DatabaseQuake {
    return DatabaseQuake(
        id = properties.ids,
        time = properties.time,
        title = properties.title,
        magnitude = properties.mag,
        url = properties.url,
        latitude = geometry.coordinates[1],
        longitude =  geometry.coordinates[0],
        depth = geometry.coordinates[2]
    )
}