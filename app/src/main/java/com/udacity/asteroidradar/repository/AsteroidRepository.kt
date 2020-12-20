package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfTheDay
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.model.toAsteroidEntity
import com.udacity.asteroidradar.api.model.toDomainModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.entities.AsteroidEntity
import com.udacity.asteroidradar.database.entities.toDomainModel
import com.udacity.asteroidradar.main.AsteroidRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {

    private val _asteroids: MediatorLiveData<List<AsteroidEntity>> = MediatorLiveData()
    val asteroids: LiveData<List<Asteroid>> = Transformations.map(
        _asteroids
    ) { it.toDomainModel() }

    private val _pictureOfTheDay: MutableLiveData<PictureOfTheDay> = MutableLiveData()
    val pictureOfTheDay: LiveData<PictureOfTheDay>
        get() = _pictureOfTheDay

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val startDate = getNextSevenDaysFormattedDates().first()
            val endDate = getNextSevenDaysFormattedDates().last()
            try {
                val asteroids = Network.nasa.getAsteroids(startDate, endDate, BuildConfig.API_KEY)
                database.asteroidDao.insertAll(*asteroids.toAsteroidEntity().toTypedArray())
            } catch (ex: Exception) {
                _asteroids.value = emptyList()
                ex.printStackTrace()
            } finally {
                val asteroidSource = database.asteroidDao.getAsteroids()
                launch(Dispatchers.Main) {
                    _asteroids.addSource(asteroidSource) {
                        _asteroids.value = it
                        _asteroids.removeSource(asteroidSource)
                    }
                }
            }
        }
    }

    suspend fun filterAsteroids(range: AsteroidRange) {
        withContext(Dispatchers.IO) {
            val nextSevenDaysDate = getNextSevenDaysFormattedDates()
            val formatter = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
            val start = formatter.parse(nextSevenDaysDate.first())!!

            val source = when (range) {
                AsteroidRange.TODAY -> database.asteroidDao.getAsteroidsBetween(start, start)
                AsteroidRange.WEEK -> database.asteroidDao.getAsteroidsBetween(
                    start,
                    formatter.parse(nextSevenDaysDate.last())!!
                )
                AsteroidRange.SAVED -> database.asteroidDao.getAsteroids()
            }
            launch(Dispatchers.Main) {
                _asteroids.addSource(source) {
                    _asteroids.value = it
                    _asteroids.removeSource(source)
                }
            }
        }
    }

    suspend fun refreshPictureOfTheDay() {
        withContext(Dispatchers.IO) {
            try {
                val picture = Network.nasa.getImageOfTheDay(BuildConfig.API_KEY).toDomainModel()
                launch(Dispatchers.Main) {
                    _pictureOfTheDay.value = picture
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    suspend fun removeRecordsFromThePast() {
        withContext(Dispatchers.Default) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 1)
            database.asteroidDao.deleteAsteroidsBefore(calendar.time)
        }
    }
}