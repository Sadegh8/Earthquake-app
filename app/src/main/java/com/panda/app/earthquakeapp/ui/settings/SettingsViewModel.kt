package com.panda.app.earthquakeapp.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panda.app.earthquakeapp.data.common.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    var darkTheme: Boolean? by mutableStateOf(null)
        private set

    init {
        viewModelScope.launch {
            userPreferencesRepository.readValue(booleanPreferencesKey("darkTheme")) {
                darkTheme = this
            }
        }
    }

    fun changeDarkTheme(theme: Boolean) {
        darkTheme = theme
        viewModelScope.launch {
            userPreferencesRepository.storeValue(booleanPreferencesKey("darkTheme"), theme)
        }
    }
}