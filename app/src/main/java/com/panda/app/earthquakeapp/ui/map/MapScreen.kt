package com.panda.app.earthquakeapp.ui.map

import android.Manifest
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
import com.panda.app.earthquakeapp.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//StateFul version
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState? = null,
    onNavigate: (UiEvent.Navigate) -> Unit
) {

    val quakes by viewModel.quakeMapItems.collectAsState(initial = emptyList())
    val location by viewModel.locationHelper.locationStateFlow.collectAsState(initial = null)
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    MapScreen(modifier = modifier,  snackbarHostState= snackbarHostState, location = location, quakes = quakes, permissionsEnable = locationPermissionState.status.isGranted, onNavigate = onNavigate)
}

//StateFul less
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState? = null,
    location: Location?,
    quakes: List<QuakeMapItem>,
    permissionsEnable: Boolean,
    onNavigate: (UiEvent.Navigate) -> Unit
) {

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMapClustering(items = quakes, onNavigate, location, permissionsEnable = permissionsEnable)
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun GoogleMapClustering(
    items: List<QuakeMapItem>,
    onNavigate: (UiEvent.Navigate) -> Unit,
    userLocation: Location?,
    permissionsEnable: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = true)
    }


    val properties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = permissionsEnable
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
    MapScreen(quakes = Utils.fakeListQuakeMap, location = null, permissionsEnable = true, onNavigate = {})
}