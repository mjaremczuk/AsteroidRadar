package com.udacity.asteroidradar.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EstimatedDiameterPojo(
    @Json(name = "kilometers") val diameterInKilometers: DiameterPojo
)