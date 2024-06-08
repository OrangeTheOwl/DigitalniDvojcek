package model

import java.util.Date

data class Flight(
    val id: String,
    val arrivalPlanned: Date? = null,
    val arrivalExact: Date? = null,
    val departurePlanned: Date? = null,
    val departureExact: Date? = null,
    val departureChangeTime: Int,
    val status: String,
    val destination: String,
    val airport: String,
    val gate: String,
    val airline: String,
    val flightNumber : String
){
    override fun toString(): String {
        return "Flight(id='$id',\n" +
                "arrivalPlanned=$arrivalPlanned,\n" +
                "arrivalExact=$arrivalExact,\n" +
                "departurePlanned=$departurePlanned,\n" +
                "departureExact=$departureExact,\n" +
                "departureChangeTime=$departureChangeTime,\n" +
                "status='$status',\n" +
                "destination='$destination',\n" +
                "airport='$airport',\n" +
                "gate='$gate',\n" +
                "airline='$airline',\n" +
                "flightNumber='$flightNumber')"
    }
}