package com.udacity.asteroidradar.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RelativeVelocityPojo(
    @Json(name = "kilometers_per_second") val kmPerSec: Double
)