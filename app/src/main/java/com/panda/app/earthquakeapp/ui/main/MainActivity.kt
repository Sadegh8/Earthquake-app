package com.panda.app.earthquakeapp.ui.main

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.panda.app.earthquakeapp.utils.TopBar
import com.panda.app.earthquakeapp.ui.theme.EarthquakeAppTheme
import com.panda.app.earthquakeapp.utils.AppNavigation
import com.panda.app.earthquakeapp.utils.BottomNav
import com.panda.app.earthquakeapp.utils.Routes
import com.panda.app.earthquakeapp.utils.location.GpsUtils
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainActivityViewModel

    @OptIn(
        ExperimentalPermissionsApi::class, ExperimentalMaterial3WindowSizeClassApi::class,
        ExperimentalMaterial3Api::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass

            val context = LocalContext.current

            val dark by viewModel.darkTheme.collectAsState(
                initial = isSystemInDarkTheme()
            )
            var askPermission by remember {
                mutableStateOf(true)
            }
            val locationPermissionState = rememberPermissionState(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            val lifecycleOwner = LocalLifecycleOwner.current

            EarthquakeAppTheme(darkTheme = dark) {

                DisposableEffect(key1 = lifecycleOwner, effect = {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            if (askPermission) {
                                locationPermissionState.launchPermissionRequest()
                                askPermission = false
                            }

                            GpsUtils(context).turnGPSOn(object : GpsUtils.OnGpsListener {
                                override fun gpsStatus(isGPSEnable: Boolean) {
                                }
                            })
                        }

                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                })

//                if (locationPermissionState.status.isGranted) {
//                    viewModel.initLocation()
//                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberAnimatedNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
                    val goUp = rememberSaveable { (mutableStateOf(false)) }

                    // Control BottomBar
                    when (navBackStackEntry?.destination?.route) {
                        Routes.MAIN, Routes.MAP -> {
                            // Show BottomBar
                            bottomBarState.value = true
                        }
                        else -> {
                            // Hide BottomBar
                            bottomBarState.value = false
                        }
                    }


                    val snackbarHostState = remember { SnackbarHostState() }


                    Scaffold(
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        bottomBar = {
                            BottomNav(navController = navController, bottomBarState, goUp)
                        },
                        topBar = {
                            TopBar(
                                Modifier,
                                navController,
                                bottomBarState,
                                navBackStackEntry,
                                )
                        },

                        ) { padding ->

                        AppNavigation(
                            modifier = Modifier.fillMaxSize(),
                            navController = navController,
                            padding = padding,
                            snackbarHostState = snackbarHostState,
                            viewModel = viewModel,
                            goUp = goUp

                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EarthquakeAppTheme {
        Greeting("Android")
    }
}