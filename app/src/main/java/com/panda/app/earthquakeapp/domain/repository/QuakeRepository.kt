package com.panda.app.earthquakeapp.domain.repository

import com.panda.app.earthquakeapp.domain.model.Quake

interface QuakeRepository {

    suspend fun getQuakes(): List<Quake>
    suspend fun refreshQuakes()
}