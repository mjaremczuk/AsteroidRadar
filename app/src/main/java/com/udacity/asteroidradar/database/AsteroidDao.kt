package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.database.entities.AsteroidEntity
import java.util.*

@Dao
interface AsteroidDao {
    @Query("select * from asteroidentity ORDER BY closeApproachDate ASC")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroidEntities: AsteroidEntity)

    @Query("DELETE from asteroidentity WHERE closeApproachDate < :date")
    fun deleteAsteroidsBefore(date: Date)

    @Query("select * from asteroidentity WHERE closeApproachDate BETWEEN :start AND :end ORDER BY closeApproachDate ASC")
    fun getAsteroidsBetween(start: Date, end: Date): LiveData<List<AsteroidEntity>>
}

@Database(entities = [AsteroidEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroid"
            ).build()
        }
    }
    return INSTANCE
}
