package model

import com.google.gson.annotations.SerializedName

data class WeatherInfo (
    @SerializedName("_id") val id: String,
    val location: String,
    val temperature: Int,
    val humidity: Int,
    val humidityStatus : String,
    val windSpeed: Int,
    val windStatus : String,
    val status: String
){
    override fun toString(): String {
        return "ID: $id\n" +
                "Location: $location\n" +
                "Temperature: $temperature\n" +
                "Humidity: $humidity\n" +
                "Humidity Status: $humidityStatus\n" +
                "Wind Speed: $windSpeed\n" +
                "Wind Status: $windStatus\n" +
                "Status: $status"
    }
}