package com.example.airportmobile.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "test_data")
data class TestDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val locationId: String,
    val locationName: String,
    val crowdNumber: Int,
    val timestamp: String
)

@Entity(tableName = "airport_marker")
data class AirportMarkerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val latitude: Double,
    val longitude: Double,
    val name: String
)

@Entity(tableName = "traffic_marker")
data class TrafficMarkerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val location: String,
    val status: String
)

@Entity(tableName = "custom_marker")
data class CustomMarkerEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var name: String = ""
) {
    constructor() : this(0, 0.0, 0.0, "")
}

@Entity(tableName = "extreme_event")
data class ExtremeEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val location: String,
    val status: String,
    val timestamp: Long
)


