package com.panda.app.earthquakeapp.ui.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panda.app.earthquakeapp.data.common.Resource
import com.panda.app.earthquakeapp.domain.use_case.GetQuakesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val quakesUseCase: GetQuakesUseCase
): ViewModel() {

    private val _state: MutableStateFlow<QuakeState> = MutableStateFlow(QuakeState())
    val state: StateFlow<QuakeState>
        get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getQuakes()
        }
    }

    private suspend fun getQuakes() {
        quakesUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.emit(QuakeState(quakes = result.data ?: emptyList()))
                }
                is Resource.Error -> {
                    _state.emit( QuakeState(
                        error = result.message ?: "An unexpected error occurred"
                    ))
                }
                is Resource.Loading -> {
                    _state.emit(_state.value.copy(isLoading = true))
                }
            }
        }.launchIn(viewModelScope)

    }
}