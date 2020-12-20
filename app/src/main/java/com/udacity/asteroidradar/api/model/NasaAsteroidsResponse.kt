package com.udacity.asteroidradar.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.database.entities.AsteroidEntity
import java.text.SimpleDateFormat
import java.util.*

@JsonClass(generateAdapter = true)
data class NasaAsteroidsResponse(
    @Json(name = "near_earth_objects") val asteroids: Map<String, List<AsteroidPojo>>
)

fun NasaAsteroidsResponse.toAsteroidEntity(): List<AsteroidEntity> {
    val formatter = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
    return this.asteroids.toList()
        .map {
            val date = it.first
            val formattedDate = formatter.parse(date)
            it.second.map { asteroid ->
                AsteroidEntity(
                    id = asteroid.id,
                    codename = asteroid.name,
                    closeApproachDate = formattedDate!!,
                    absoluteMagnitude = asteroid.absoluteMagnitude,
                    estimatedDiameter = asteroid.estimatedDiameter.diameterInKilometers.diameterMax,
                    relativeVelocity = asteroid.closeApproachData.first().relativeVelocityPojo.kmPerSec,
                    distanceFromEarth = asteroid.closeApproachData.first().missDistancePojo.astronomicalDistance,
                    isPotentiallyHazardous = asteroid.isPotentiallyHazardous
                )
            }
        }
        .flatten()
}