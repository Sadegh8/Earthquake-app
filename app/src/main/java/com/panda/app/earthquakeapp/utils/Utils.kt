package com.panda.app.earthquakeapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.panda.app.earthquakeapp.data.QuakeMapItem
import com.panda.app.earthquakeapp.domain.model.Quake
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    val fakeListQuake = listOf(
        Quake(
            id = "0",
            title = "USA",
            time = Date().time,
            "https://www.google.com/",
            4.4,
            LatLng(0.0, 43.4),
            56.5
        ),
        Quake(
            id = "0",
            title = "Japan",
            time = Date().time - 90000,
            "https://www.google.com/",
            5.2,
            LatLng(0.0, 43.4),
            46.5
        )
    )
    val fakeListQuakeMap = listOf(
        QuakeMapItem(
            id = "0",
            itemTitle = "USA",
            itemSnippet = "",
            itemPosition =  LatLng(0.0, 43.4)
        ),
        QuakeMapItem(
            id = "0",
            itemTitle = "Japan",
            itemSnippet = "",
            itemPosition = LatLng(0.0, 43.4),
        )
    )

    fun formatDate(dateObject: Long): String {
        val dateFormat =
            SimpleDateFormat("LLL dd, yyyy, HH:mm", Locale.getDefault())
        return dateFormat.format(dateObject)
    }

    fun getDistanceBetween(
        destinationCoordinate: LatLng,
        currentLocation: LatLng
    ): Int {
        val locationA = Location("point A")
        locationA.latitude = destinationCoordinate.latitude
        locationA.longitude = destinationCoordinate.longitude
        val locationB = Location("point B")
        locationB.latitude = currentLocation.latitude
        locationB.longitude = currentLocation.longitude
        val distance = locationA.distanceTo(locationB)
        return distance.toInt()
    }

    /**
     * convert map pin icon to bitmap
     */
    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    /**
     * Check if should refresh quake list
     */
    fun shouldRefresh(quakes: List<Quake>): Boolean {
            if (quakes.isEmpty()) {
                return true
            } else {
                val lastQuake = quakes.maxByOrNull { it.time }
                lastQuake?.let {
                    val now = Date().time
                    val lastItem = it.time
                    val diff: Long = now - lastItem
                    val seconds = diff / 1000
                    val minutes = seconds / 60
                    return minutes > 5

                }
            }

        return true
    }

}

