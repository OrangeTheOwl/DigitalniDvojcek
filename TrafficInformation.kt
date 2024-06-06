package org.example.model

import java.time.LocalDateTime

data class TrafficInfo(
    val id: String,
    val description: String,
    val location: String,
    val date: LocalDateTime,
    val type: String
)
