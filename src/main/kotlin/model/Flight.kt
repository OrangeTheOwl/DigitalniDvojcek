package model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Flight(
    @SerializedName("_id") val id: String? = null,
    var arrivalPlanned: Date? = null,
    var arrivalExact: Date? = null,
    val departurePlanned: Date? = null,
    val departureExact: Date? = null,
    val changeTime: Int,
    val status: String,
    val destination: String,
    val airport: String,
    var gate: String,
    val airline: String,
    val flightNumber : String,
    val destinationLat: Double? = 0.0,
    val destinationLng: Double? = 0.0,
){
    override fun toString(): String {
        return "Flight(id='$id',\n" +
                "arrivalPlanned=$arrivalPlanned,\n" +
                "arrivalExact=$arrivalExact,\n" +
                "departurePlanned=$departurePlanned,\n" +
                "departureExact=$departureExact,\n" +
                "departureChangeTime=$changeTime,\n" +
                "status='$status',\n" +
                "destination='$destination',\n" +
                "airport='$airport',\n" +
                "gate='$gate',\n" +
                "airline='$airline',\n" +
                "flightNumber='$flightNumber'\n" +
                 "destinationLat='$destinationLat',\n" +
                "destinationLng='$destinationLng')"
    }
}