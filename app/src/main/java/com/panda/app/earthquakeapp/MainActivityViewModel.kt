package com.panda.app.earthquakeapp

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panda.app.earthquakeapp.data.common.UserPreferencesRepository
import com.panda.app.earthquakeapp.utils.location.LocationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val context: Application,
    userPreferencesRepository: UserPreferencesRepository

): ViewModel() {
    private val _darkTheme = Channel<Boolean>()
    val darkTheme = _darkTheme.receiveAsFlow()

    var notSaveYet = true

    init {
        viewModelScope.launch {
            userPreferencesRepository.readValue(booleanPreferencesKey("darkTheme")) {
                viewModelScope.launch {
                    _darkTheme.send(this@readValue)
                }
            }
        }
    }


    fun changeTheme(dark: Boolean) {
        viewModelScope.launch {
            _darkTheme.send(dark)

        }
    }
}