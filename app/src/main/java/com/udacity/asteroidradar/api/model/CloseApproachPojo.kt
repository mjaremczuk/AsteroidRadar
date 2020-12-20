package com.udacity.asteroidradar.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CloseApproachPojo(
    @Json(name = "relative_velocity") val relativeVelocityPojo: RelativeVelocityPojo,
    @Json(name = "miss_distance") val missDistancePojo: MissDistancePojo
)