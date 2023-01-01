package com.panda.app.earthquakeapp.ui.detail

import android.Manifest
import android.location.Location
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.panda.app.earthquakeapp.utils.Utils
import com.panda.app.earthquakeapp.R
import com.panda.app.earthquakeapp.domain.model.Quake
import com.panda.app.earthquakeapp.ui.detail.components.MapCardInfo
import kotlinx.coroutines.delay
import java.util.*

//Stateful version
@OptIn(ExperimentalPermissionsApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val quake by viewModel.quake.collectAsStateWithLifecycle(initialValue = null)
    val location by viewModel.locationHelper.locationStateFlow.collectAsStateWithLifecycle(initialValue  = null)
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    DetailScreen(modifier = modifier, quake = quake, location = location, permission = locationPermissionState.status.isGranted)
}

//Stateless version
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    quake: Quake?,
    location: Location?,
    permission: Boolean
) {
    Box(modifier = modifier.fillMaxSize()) {

        Map(
            modifier = Modifier.fillMaxSize(),
            quakeM = quake,
            location = location,
            permissionEnable = permission
        )
    }
}


@Composable
private fun Map(
    modifier: Modifier = Modifier,
    quakeM: Quake?,
    location: Location?,
    permissionEnable: Boolean
) {
    val context = LocalContext.current

    val density = LocalDensity.current
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = permissionEnable)
    }
    val properties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = permissionEnable,
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
                .align(Alignment.BottomCenter), enter = slideInVertically {
                with(density) { -50.dp.roundToPx() }
            } + expandVertically(
                expandFrom = Alignment.Top
            ) + fadeIn(
                initialAlpha = 0.25f
            ) , exit = slideOutVertically() + shrinkVertically() + fadeOut() ) {
            quakeM?.let { quake ->
                MapCardInfo(
                    modifier = Modifier
                        .fillMaxSize(), currentLocation = location, quakeM = quake
                )

            }
        }

    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetailScreenPreview() {
    val location = Location("service Provider")
    location.longitude = 43.5
    location.latitude = 24.4
    DetailScreen(quake =  Quake(
        id = "0",
        title = "Japan",
        time = Date().time - 90000,
        "https://www.google.com/",
        5.2,
        LatLng(0.0, 43.4),
        46.5
    ), location = location, permission = true)
}