package com.panda.app.earthquakeapp.utils.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationHelper(private val context: Context) {
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val isStart: MutableState<Boolean> = mutableStateOf(false)
    val gpsProviderState = mutableStateOf(false)

    private val _locationStateFlow: MutableStateFlow<Location?> = MutableStateFlow(null)
    val locationStateFlow: StateFlow<Location?>
        get() = _locationStateFlow.asStateFlow()

    private val locationJob = Job()

    fun start() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.also { locationA ->
                    CoroutineScope(locationJob).launch {
                        _locationStateFlow.emit(locationA)
                    }
                }
            }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        isStart.value = true
    }


    fun stop() {
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            isStart.value = false
            locationJob.cancel()

        } catch (e: Exception) {
            Log.e("Location", e.message.toString())
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            CoroutineScope(locationJob).launch {
                for (location in p0.locations) {
                    _locationStateFlow.emit(location)
                }
            }
        }

        override fun onLocationAvailability(p0: LocationAvailability) {
            super.onLocationAvailability(p0)
            gpsProviderState.value = p0.isLocationAvailable
        }
    }


    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 2000
            priority =  Priority.PRIORITY_BALANCED_POWER_ACCURACY
        }

    }

}

