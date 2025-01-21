package com.example.airportmobile.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.airportmobile.database.TestDataDao
import com.example.airportmobile.database.TestDataEntity


@Database(
    entities = [AirportMarkerEntity::class, TrafficMarkerEntity::class, TestDataEntity::class, CustomMarkerEntity::class, ExtremeEventEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun airportMarkerDao(): TestDataDao.AirportMarkerDao
    abstract fun trafficMarkerDao(): TestDataDao.TrafficMarkerDao
    abstract fun customMarkerDao(): TestDataDao.CustomMarkerDao
    abstract fun extremeEventDao(): TestDataDao.ExtremeEventDao
    abstract fun testDataDao(): TestDataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
