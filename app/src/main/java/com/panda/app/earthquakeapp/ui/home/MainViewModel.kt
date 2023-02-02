package com.panda.app.earthquakeapp.ui.home


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
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
) : ViewModel() {

    var state by mutableStateOf(QuakeState())
        private set

    val d = Snapshot.takeSnapshot { }
    init {
        viewModelScope.launch {
            getQuakes()
        }
    }

    private suspend fun getQuakes() {
        quakesUseCase().onEach { result ->
            state = when (result) {
                is Resource.Success -> {
                    (QuakeState(quakes = result.data ?: emptyList()))
                }
                is Resource.Error -> {
                    (QuakeState(
                        error = result.message ?: "An unexpected error occurred"
                    ))
                }
                is Resource.Loading -> {
                    state.copy(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)

    }
}