package com.udacity.asteroidradar.main


//sealed class AsteroidRange {
//
//    data class Today(val date: String): AsteroidRange()
//    data class Week(val start: String, val end: String): AsteroidRange()
//    object Saved : AsteroidRange()
//}
enum class AsteroidRange {
    TODAY,
    WEEK,
    SAVED
}