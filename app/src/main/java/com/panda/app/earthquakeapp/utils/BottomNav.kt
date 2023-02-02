package com.panda.app.earthquakeapp.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.panda.app.earthquakeapp.ui.theme.Purple500


@Composable
fun BottomNav(navController: NavController, bottomBarState: MutableState<Boolean>, goUp: MutableState<Boolean> ) {
    val isLightTheme = isSystemInDarkTheme()

    val items = remember {
        listOf(
            BottomNavItem.Main,
            BottomNavItem.Map
        )
    }
    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = stringResource(id = item.title),
                            )
                        },
                        label = { Text(text = stringResource(id = item.title)) },
                        selected = currentRoute == item.screen_route,
                        onClick = {
                            if (currentRoute == Routes.MAIN) {
                                goUp.value = true
                            }
                            navController.navigate(item.screen_route) {

                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    )

}