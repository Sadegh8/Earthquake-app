package com.panda.app.earthquakeapp.ui.detail

import android.Manifest
import android.location.Location
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.panda.app.earthquakeapp.utils.Utils
import com.panda.app.earthquakeapp.R
import com.panda.app.earthquakeapp.domain.model.Quake
import com.panda.app.earthquakeapp.ui.detail.components.MapCardInfo
import kotlinx.coroutines.delay

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
    location: State<Location?>,
) {
    val quake by viewModel.quake.collectAsState(initial = null)
    Box(modifier = modifier.fillMaxSize()) {

        Map(
            modifier = Modifier.fillMaxSize(),
            quakeM = quake,
            location = location
        )
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun Map(
    modifier: Modifier = Modifier,
    quakeM: Quake?,
    location: State<Location?>
) {
    val context = LocalContext.current
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = true)
    }
    val properties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = locationPermissionState.hasPermission,
                )
        )
    }

    var quakeLoc = quakeM?.coordinate ?: LatLng(0.0, 0.0)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(quakeLoc, 8f)
    }
    Box(modifier = modifier.fillMaxWidth()) {
        GoogleMap(
            modifier = Modifier
                .matchParentSize(),
            properties = properties,
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
                quakeLoc = quakeM?.coordinate ?: LatLng(0.0, 0.0)
                cameraPositionState.position = CameraPosition.fromLatLngZoom(quakeLoc, 8f)
            }
        ) {
            quakeM?.let { quake ->
                Marker(
                    state = MarkerState(position = quakeLoc),
                    title = quake.title,
                    snippet = quake.title,
                    icon = Utils.bitmapDescriptorFromVector(context, R.drawable.ic_pin)
                )
            }
        }

        //Little animation for visibility!
        var isVisible by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(key1 = quakeM){
            if (quakeM != null) {
                isVisible = true
            }
        }

        AnimatedVisibility(visible = isVisible,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(start = 16.dp, end = 16.dp, bottom = 30.dp)
                .align(Alignment.BottomCenter)) {
            quakeM?.let { quake ->
                MapCardInfo(
                    modifier = Modifier
                        .fillMaxSize(), currentLocation = location.value, quakeM = quake
                )

            }
        }

    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetailScreenPreview() {
    DetailScreen(location = mutableStateOf(null))
}