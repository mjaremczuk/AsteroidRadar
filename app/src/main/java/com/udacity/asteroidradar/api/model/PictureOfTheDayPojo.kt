package com.udacity.asteroidradar.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.PictureOfTheDay

@JsonClass(generateAdapter = true)
class PictureOfTheDayPojo(
    val title: String,
    @Json(name = "media_type") val mediaType: String,
    val url: String
)

fun PictureOfTheDayPojo.toDomainModel() =
    PictureOfTheDay(
        mediaType,
        title,
        url
    )
