package com.panda.app.earthquakeapp.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.panda.app.earthquakeapp.ui.home.components.QuakeItem
import com.panda.app.earthquakeapp.utils.Routes
import com.panda.app.earthquakeapp.utils.UiEvent

@OptIn(ExperimentalFoundationApi::class, ExperimentalLifecycleComposeApi::class)

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState? = null,
    viewModel: MainViewModel = hiltViewModel(),
    onNavigate: (UiEvent.Navigate) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle(initialValue = QuakeState())

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.quakes) { quake ->
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

        if (state.isLoading ) {
            CircularProgressIndicator(modifier =
            Modifier.align(if (state.quakes.isEmpty()) Alignment.Center else Alignment.TopCenter))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen(onNavigate = {})
}