package com.panda.app.earthquakeapp.ui.map

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panda.app.earthquakeapp.data.QuakeMapItem
import com.panda.app.earthquakeapp.data.common.Resource
import com.panda.app.earthquakeapp.domain.model.toQuakeMap
import com.panda.app.earthquakeapp.domain.use_case.GetQuakesUseCase
import com.panda.app.earthquakeapp.ui.home.QuakeState
import com.panda.app.earthquakeapp.utils.location.LocationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val quakesUseCase: GetQuakesUseCase,
    val locationHelper: LocationHelper,
    ): ViewModel() {

    private val _state = mutableStateOf(QuakeState())

    private val _quakeMapItems: MutableStateFlow<List<QuakeMapItem>> = MutableStateFlow(emptyList())
    val quakeMapItems: StateFlow<List<QuakeMapItem>>
        get() = _quakeMapItems.asStateFlow()

    private var listItem: MutableList<QuakeMapItem> = emptyList<QuakeMapItem>().toMutableList()

    init {
        locationHelper.start()
        getQuakes()
    }


    private fun getQuakes() {
        quakesUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = QuakeState(quakes = result.data ?: emptyList())
                       _state.value.quakes.forEach {
                        listItem.add(it.toQuakeMap())
                    }
                    _quakeMapItems.emit(listItem)

                }
                is Resource.Error -> {
                    _state.value = QuakeState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }
                is Resource.Loading -> {
                    _state.value = QuakeState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        locationHelper.stop()
    }
}