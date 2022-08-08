package com.panda.app.earthquakeapp.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panda.app.earthquakeapp.data.common.Resource
import com.panda.app.earthquakeapp.domain.use_case.GetQuakesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val quakesUseCase: GetQuakesUseCase
): ViewModel() {

    private val _state = mutableStateOf(QuakeState())
    val state: State<QuakeState> = _state

    init {
        viewModelScope.launch {
            getQuakes()
        }
    }

    private fun getQuakes() {
        quakesUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = QuakeState(quakes = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = QuakeState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    _state.value = QuakeState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}