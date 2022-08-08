package com.panda.app.earthquakeapp.domain.model

import com.google.android.gms.maps.model.LatLng
import com.panda.app.earthquakeapp.data.QuakeMapItem


data class Quake(
    val id: String,
    val title: String,
    val time: Long,
    val url: String,
    val magnitude: Double,
    val coordinate: LatLng,
    val depth: Double
)


fun Quake.toQuakeMap(): QuakeMapItem {
    return QuakeMapItem(
        id = id,
        itemPosition = coordinate,
        itemTitle = String.format("%.2f", magnitude),
        itemSnippet = title
    )
}