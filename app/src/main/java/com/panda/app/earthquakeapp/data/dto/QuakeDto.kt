package com.panda.app.earthquakeapp.data.dto

//Earthquake api data transfer object
data class QuakeDto(
    val features: List<Feature>,
    val metadata: Metadata,
    val type: String
)

