package com.panda.app.earthquakeapp.ui.map

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.panda.app.earthquakeapp.R
import com.panda.app.earthquakeapp.data.QuakeMapItem


class MarkerClusterRenderer(
    context: Context?,
    map: GoogleMap?,
    clusterManager: ClusterManager<QuakeMapItem>?
) :
    DefaultClusterRenderer<QuakeMapItem>(
        context,
        map,
        clusterManager
    ) {
    private val iconGenerator: IconGenerator = IconGenerator(context)
    private val markerImageView: ImageView = ImageView(context)
    override fun onBeforeClusterItemRendered(
        item: QuakeMapItem,
        markerOptions: MarkerOptions
    ) {

            iconGenerator.setBackground(null)
            val imageID = R.drawable.ic_pin
            markerImageView.setImageResource(imageID)
            val icon = iconGenerator.makeIcon()
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
            markerOptions.title(item.title)

    }

    override fun setOnClusterInfoWindowClickListener(listener: ClusterManager.OnClusterInfoWindowClickListener<QuakeMapItem>?) {
        super.setOnClusterInfoWindowClickListener(listener)
    }

    companion object {
        private const val MARKER_DIMENSION = 75
    }

    init {
        markerImageView.layoutParams = ViewGroup.LayoutParams(MARKER_DIMENSION, MARKER_DIMENSION)
        iconGenerator.setContentView(markerImageView)
    }
}