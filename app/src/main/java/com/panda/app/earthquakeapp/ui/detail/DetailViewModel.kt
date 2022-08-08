package com.panda.app.earthquakeapp.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panda.app.earthquakeapp.data.database.QuakeDatabase
import com.panda.app.earthquakeapp.data.database.toQuake
import com.panda.app.earthquakeapp.domain.model.Quake
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    quakeDatabase: QuakeDatabase
): ViewModel() {

    private val _quake : MutableStateFlow<Quake?> = MutableStateFlow(null)
    val quake: StateFlow<Quake?>
        get() = _quake.asStateFlow()

    init {
        //Used SavedStateHandle to get the quake ID
        val quakeArg = savedStateHandle.get<String?>("quakeId")
        quakeArg?.let {
            viewModelScope.launch {
                _quake.value = quakeDatabase.dao.getQuakeById(it).toQuake()
            }
        }
    }
}