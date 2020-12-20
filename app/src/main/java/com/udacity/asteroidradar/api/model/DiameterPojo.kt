package com.udacity.asteroidradar.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DiameterPojo(
    @Json(name = "estimated_diameter_min") val diameterMin: Double,
    @Json(name = "estimated_diameter_max") val diameterMax: Double
)