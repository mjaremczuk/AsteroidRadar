package com.udacity.asteroidradar.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AsteroidPojo(
    val id: Long,
    val name: String,
    @Json(name = "close_approach_data") val closeApproachData: List<CloseApproachPojo>,
    @Json(name = "absolute_magnitude_h") val absoluteMagnitude: Double,
    @Json(name = "estimated_diameter") val estimatedDiameter: EstimatedDiameterPojo,
    @Json(name = "is_potentially_hazardous_asteroid") val isPotentiallyHazardous: Boolean
)

//id (Not for displaying but for using in db)
//absolute_magnitude
//estimated_diameter_max (Kilometers)
//is_potentially_hazardous_asteroid
//close_approach_data -> relative_velocity -> kilometers_per_second
//close_approach_data -> miss_distance -> astronomical