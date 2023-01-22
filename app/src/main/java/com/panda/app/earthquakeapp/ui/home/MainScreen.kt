package com.panda.app.earthquakeapp.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.panda.app.earthquakeapp.ui.home.components.QuakeItem
import com.panda.app.earthquakeapp.ui.util.preview.DevicePreview
import com.panda.app.earthquakeapp.utils.Routes
import com.panda.app.earthquakeapp.utils.UiEvent
import com.panda.app.earthquakeapp.utils.Utils


//Stateful version
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState? = null,
    viewModel: MainViewModel = hiltViewModel(),
    onNavigate: (UiEvent.Navigate) -> Unit,

    ) {
    val state = viewModel.state
    MainScreen(
        modifier = modifier,
        quakeState = state,
        onNavigate = onNavigate,
        scaffoldState = scaffoldState
    )

}

//Stateless version
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState? = null,
    quakeState: QuakeState,
    onNavigate: (UiEvent.Navigate) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize(), state = rememberLazyListState()) {
            items(quakeState.quakes) { quake ->
                QuakeItem(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp, bottom = 16.dp, top = 8.dp)
                        .fillMaxWidth()
                        .height(135.dp)
                        .animateItemPlacement()
                        .clickable {
                            onNavigate(
                                UiEvent.Navigate(
                                    Routes.DETAILS + "?quakeId=${
                                        quake.id
                                    }"
                                )
                            )
                        }, quake = quake
                )

            }
        }

        if (quakeState.isLoading) {
            CircularProgressIndicator(
                modifier =
                Modifier.align(if (quakeState.quakes.isEmpty()) Alignment.Center else Alignment.TopCenter)
            )
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//DevicePreview shows three devices in same time phone
@DevicePreview
@Composable
fun MainScreenPreview() {
    MainScreen(quakeState = QuakeState(
        quakes = Utils.fakeListQuake
    ), onNavigate = {})
}