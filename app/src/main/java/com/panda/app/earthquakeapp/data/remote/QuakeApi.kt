package com.panda.app.earthquakeapp.data.remote

import com.panda.app.earthquakeapp.data.dto.QuakeDto
import retrofit2.http.GET

//Get the latest earthquakes from USGS
interface QuakeApi {
    @GET("/fdsnws/event/1/query?format=geojson&limit=200")
    suspend fun getQuakes(): QuakeDto

}

