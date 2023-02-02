package com.panda.app.earthquakeapp.ui.home

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
    snackbarHostState: SnackbarHostState? = null,
    viewModel: MainViewModel = hiltViewModel(),
    goUp: MutableState<Boolean>,
    onNavigate: (UiEvent.Navigate) -> Unit,

    ) {
    val state = viewModel.state
    MainScreen(
        modifier = modifier,
        quakeState = state,
        onNavigate = onNavigate,
        goUp = goUp,
        snackbarHostState = snackbarHostState
    )

}

//Stateless version
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState? = null,
    quakeState: QuakeState,
    goUp: MutableState<Boolean>,
    onNavigate: (UiEvent.Navigate) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = goUp.value) {
        if (goUp.value) {
            listState.animateScrollToItem(0)
            goUp.value = false
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
            items(quakeState.quakes) { quake ->
                QuakeItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(135.dp)
                        .animateItemPlacement()
                        .padding(start = 16.dp, end = 8.dp, bottom = 16.dp, top = 8.dp)
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
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
    ), onNavigate = {}, goUp = remember {
        mutableStateOf(false)
    }
    )
}