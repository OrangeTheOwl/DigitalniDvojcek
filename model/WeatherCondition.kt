package org.example.model

import java.sql.Timestamp

data class WeatherCondition(
    val id: String,
    val timestamp: Timestamp,
    val location: String,
    val temperature: Double,
    val humidity: Double,
    val windSpeed: Double,
    val description: String
)

