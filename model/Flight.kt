package org.example.model

import java.time.LocalDateTime

data class Flight(
    val id: String,
    val flightNumber: String,
    val startDestination: String,
    val endDestination: String,
    val timeDeparturePlanned: String?,
    val timeDepartureActual: String?,
    val arrivalTime: LocalDateTime?,
    val status: String
)
