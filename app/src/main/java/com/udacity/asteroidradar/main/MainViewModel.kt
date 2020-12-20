package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfTheDay
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)

    val asteroids: LiveData<List<Asteroid>> = repository.asteroids

    val pictureOfTheDay: LiveData<PictureOfTheDay> = repository.pictureOfTheDay

    private val _navigateToDetails = MutableLiveData<Asteroid?>()

    val navigateToDetails: LiveData<Asteroid?>
        get() = _navigateToDetails

    init {
        viewModelScope.launch {
            repository.refreshAsteroids()
            repository.refreshPictureOfTheDay()
        }
    }

    fun filterAsteroids(range: AsteroidRange) {
        viewModelScope.launch {
            repository.filterAsteroids(range)
        }
    }

    fun displayDetails(asteroid: Asteroid) {
        _navigateToDetails.value = asteroid
    }

    fun navigateToDetailComplete() {
        _navigateToDetails.value = null
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}