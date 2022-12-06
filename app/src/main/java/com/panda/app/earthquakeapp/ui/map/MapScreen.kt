package com.panda.app.earthquakeapp.ui.map

import android.Manifest
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.compose.*
import com.panda.app.earthquakeapp.data.QuakeMapItem
import com.panda.app.earthquakeapp.utils.Routes
import com.panda.app.earthquakeapp.utils.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState? = null,
    location: State<Location?>,
    onNavigate: (UiEvent.Navigate) -> Unit
) {

    val quakes by viewModel.quakeMapItems.collectAsState(initial = emptyList())

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMapClustering(items = quakes, onNavigate, location.value)
    }
}

@OptIn(MapsComposeExperimentalApi::class, ExperimentalPermissionsApi::class)
@Composable
fun GoogleMapClustering(
    items: List<QuakeMapItem>,
    onNavigate: (UiEvent.Navigate) -> Unit,
    userLocation: Location?
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = true)
    }
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val properties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = locationPermissionState.status.isGranted
            )
        )
    }


    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        userLocation?.let {
            position = CameraPosition.fromLatLngZoom(LatLng(userLocation.latitude,userLocation.longitude), 4f)
        }
    }


    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings
        ) {
            var clusterManager by remember { mutableStateOf<ClusterManager<QuakeMapItem>?>(null) }
            MapEffect(items) { map ->
                if (clusterManager == null) {
                    clusterManager = ClusterManager<QuakeMapItem>(context, map)
                }
                clusterManager?.renderer = MarkerClusterRenderer(context, map, clusterManager)
                clusterManager?.addItems(items)

                clusterManager?.setOnClusterItemInfoWindowClickListener {
                    coroutineScope.launch {
                        withContext(Dispatchers.Main) {
                            try {
                                onNavigate(
                                    UiEvent.Navigate(
                                        Routes.DETAILS + "?quakeId=${
                                            it.id
                                        }"
                                    )
                                )
                            } catch (e: Exception) {
                                Log.e("Map", e.message.toString())
                            }
                        }
                    }
                }

                clusterManager?.setOnClusterClickListener {
                    val builder = LatLngBounds.builder()
                    for (item in it.items) {
                        builder.include(item.position)
                    }
                    val bounds = builder.build()
                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                    return@setOnClusterClickListener true
                }
            }
            LaunchedEffect(key1 = cameraPositionState.isMoving) {
                if (!cameraPositionState.isMoving) {
                    clusterManager?.onCameraIdle()
                }

            }
        }

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MapScreenPreview(
) {
    MapScreen(onNavigate = {}, location = mutableStateOf(null))
}