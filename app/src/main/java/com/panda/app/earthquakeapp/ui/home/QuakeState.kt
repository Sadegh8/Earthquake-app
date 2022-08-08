package com.panda.app.earthquakeapp.ui.home

import com.panda.app.earthquakeapp.domain.model.Quake


data class QuakeState(
    val isLoading: Boolean = false,
    val quakes: List<Quake> = emptyList(),
    val error: String = ""
)

