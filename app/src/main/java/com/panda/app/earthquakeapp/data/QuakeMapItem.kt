package com.panda.app.earthquakeapp.data

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

//Map data class for Cluster manager
data class QuakeMapItem(
    val itemPosition: LatLng,
    val itemTitle: String,
    val itemSnippet: String,
    val id: String
) : ClusterItem {
    override fun getPosition(): LatLng =
        itemPosition

    override fun getTitle(): String =
        itemTitle

    override fun getSnippet(): String =
        itemSnippet

    fun id(): String = id

}
