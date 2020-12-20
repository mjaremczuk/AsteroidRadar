package com.udacity.asteroidradar.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class AsteroidEntity(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: Date,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

fun List<AsteroidEntity>.toDomainModel(): List<Asteroid> {
    val formatter = SimpleDateFormat(
        Constants.DATE_FORMAT,
        Locale.getDefault()
    )
    return map {
        Asteroid(
            it.id,
            it.codename,
            formatter.format(it.closeApproachDate),
            it.absoluteMagnitude,
            it.estimatedDiameter,
            it.relativeVelocity,
            it.distanceFromEarth,
            it.isPotentiallyHazardous
        )
    }
}