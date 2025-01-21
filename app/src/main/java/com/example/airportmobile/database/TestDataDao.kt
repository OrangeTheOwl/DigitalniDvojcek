package com.example.airportmobile.database

import com.example.airportmobile.database.TestDataEntity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query



@Dao
interface TestDataDao {

    @Query("SELECT * FROM test_data")
    fun getAll(): List<TestDataEntity>

    @Insert
    fun insertAll(vararg testData: TestDataEntity)

    @Query("DELETE FROM test_data")
    fun deleteAll()

    @Dao
    interface AirportMarkerDao {
        @Insert
        fun insertAll(vararg markers: AirportMarkerEntity)

        @Query("SELECT * FROM airport_marker")
        fun getAll(): List<AirportMarkerEntity>
    }

    @Dao
    interface TrafficMarkerDao {
        @Insert
        fun insertAll(vararg markers: TrafficMarkerEntity)

        @Query("SELECT * FROM traffic_marker")
        fun getAll(): List<TrafficMarkerEntity>
    }

    @Dao
    interface CustomMarkerDao {
        @Insert
        fun insert(marker: CustomMarkerEntity)

        @Query("SELECT * FROM custom_marker")
        fun getAll(): List<CustomMarkerEntity>
    }

    @Dao
    interface ExtremeEventDao {
        @Insert
        fun insert(event: ExtremeEventEntity)

        @Query("SELECT * FROM extreme_event")
        fun getAll(): List<ExtremeEventEntity>
    }


}


