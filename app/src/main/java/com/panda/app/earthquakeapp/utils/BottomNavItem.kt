package com.panda.app.earthquakeapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector
import com.panda.app.earthquakeapp.R

sealed class BottomNavItem(var title: Int, var icon: ImageVector, var screen_route: String) {
    object Main : BottomNavItem(R.string.latest, Icons.Default.Home, Routes.MAIN)
    object Map: BottomNavItem(R.string.nav_map,Icons.Default.Place, Routes.MAP)
}

