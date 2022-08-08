package com.panda.app.earthquakeapp.utils

import androidx.navigation.NavArgs
import java.time.Duration

sealed class UiEvent {
    object PopBackStack: UiEvent()
    data class Navigate(val route: String): UiEvent()
    data class ShowSnackBar(
        val message: String,
        val action: String? = null
    ): UiEvent()
    data class ShowToast(
        val message: String,
        val duration: Int
    ): UiEvent()
    data class ChangeTheme(val dark: Boolean): UiEvent()
    data class ChangeLanguage(val language: String): UiEvent()

}
