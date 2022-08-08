package com.panda.app.earthquakeapp.utils

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.panda.app.earthquakeapp.R

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    bottomBarState: MutableState<Boolean>,
    navBackStackEntry: NavBackStackEntry?,

) {
    val context = LocalContext.current

    TopAppBar(
        modifier = modifier,
        title =
        {
            if (bottomBarState.value) {
                Text(
                    text = stringResource(R.string.app_name),
                )

            } else {
                navBackStackEntry?.destination?.route?.let {
                    Text(
                        text = getTitleByRoute(context, it)
                    )
                }
            }
        },
        navigationIcon = {
            if (!bottomBarState.value) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Close"
                    )
                }
            }
        }, actions = {
            if (bottomBarState.value) {
                IconButton(onClick = {
                    navController.navigate(Routes.SETTINGS)
                }) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = stringResource(R.string.settings)
                    )
                }


            }

        },
        elevation = 0.dp,
        backgroundColor = Color.Transparent
    )


}

fun getTitleByRoute(context: Context, route: String): String {
    return when (route) {
        Routes.SETTINGS -> context.getString(R.string.settings)

        else -> context.getString(R.string.app_name)
    }
}