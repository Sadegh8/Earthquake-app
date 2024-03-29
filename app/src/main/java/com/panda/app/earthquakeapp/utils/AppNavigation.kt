package com.panda.app.earthquakeapp.utils

import androidx.compose.animation.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.panda.app.earthquakeapp.ui.main.MainActivityViewModel
import com.panda.app.earthquakeapp.ui.detail.DetailScreen
import com.panda.app.earthquakeapp.ui.map.MapScreen
import com.panda.app.earthquakeapp.ui.settings.SettingsScreen
import com.panda.app.earthquakeapp.ui.home.MainScreen


@ExperimentalAnimationApi
@Composable
internal fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    padding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    viewModel: MainActivityViewModel,
    goUp: MutableState<Boolean>
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Routes.MAIN,
        enterTransition = { defaultEarthquakeEnterTransition(initialState) },
        exitTransition = { defaultEarthquakeExitTransition(targetState) },
        popEnterTransition = { defaultEarthquakePopEnterTransition(initialState) },
        popExitTransition = { defaultEarthquakePopExitTransition(targetState) },
        modifier = modifier
    ) {
        addMain(
            navController,
            padding = padding,
            snackbarHostState = snackbarHostState, goUp)
        addMap(navController, padding = padding, snackbarHostState = snackbarHostState)
        addSettings(viewModel = viewModel, modifier = modifier.padding(padding))
        addDetail()
    }
}


@ExperimentalAnimationApi
private fun NavGraphBuilder.addMain(
    navController: NavController,
    padding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    goUp: MutableState<Boolean>
) {
    composable(
        route = Routes.MAIN
    )
    {
        MainScreen(
            onNavigate = {
                navController.navigate(it.route)
            },
            modifier = Modifier.padding(padding),
            snackbarHostState = snackbarHostState,
            goUp =  goUp
        )
    }

}


@ExperimentalAnimationApi
private fun NavGraphBuilder.addMap(
    navController: NavController,
    padding: PaddingValues,
    snackbarHostState: SnackbarHostState
) {
    composable(
        route = BottomNavItem.Map.screen_route
    )
    {
        MapScreen(
            onNavigate = {
                navController.navigate(it.route)
            },
            modifier = Modifier.padding(padding),
            snackbarHostState = snackbarHostState
        )
    }
}


@ExperimentalAnimationApi
private fun NavGraphBuilder.addSettings(viewModel: MainActivityViewModel, modifier: Modifier) {
    composable(
        route = Routes.SETTINGS
    )
    {
        SettingsScreen(modifier = modifier,onTheme = {
            viewModel.changeTheme(it.dark)
        })
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addDetail() {
    composable(
        route = Routes.DETAILS + "?quakeId={quakeId}",
        arguments = listOf(
            navArgument("quakeId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    )
    {
        DetailScreen()
    }
}


@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultEarthquakeEnterTransition(
    initial: NavBackStackEntry
): EnterTransition {
    val initialNavGraph = initial.destination
    if (initialNavGraph.route == Routes.MAIN || initialNavGraph.route == Routes.MAP) {
        return fadeIn()
    }
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.Start)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultEarthquakeExitTransition(
    target: NavBackStackEntry
): ExitTransition {
    val targetNavGraph = target.destination
    if (targetNavGraph.route == Routes.MAIN || targetNavGraph.route == Routes.MAP) {
        return fadeOut()
    }
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)
}

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultEarthquakePopEnterTransition(
    initial: NavBackStackEntry
): EnterTransition {
    val initialNavGraph = initial.destination
    if (initialNavGraph.route == Routes.MAIN || initialNavGraph.route == Routes.MAP) {
        return fadeIn()
    }
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.End)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultEarthquakePopExitTransition(
    target: NavBackStackEntry
): ExitTransition {
    val targetNavGraph = target.destination
    if (targetNavGraph.route == Routes.MAIN || targetNavGraph.route == Routes.MAP) {
        return fadeOut()
    }
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
}