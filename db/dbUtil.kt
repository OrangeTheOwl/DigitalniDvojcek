import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import model.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

fun fetchGates(): List<Gate>? {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("http://localhost:3210/gate")
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        val responseData = response.body?.string()

        return responseData?.let { parseGates(it) }
    }
}

fun parseGates(jsonData: String): List<Gate> {
    val type = object : TypeToken<List<Gate>>() {}.type
    return Gson().fromJson(jsonData, type)
}

fun addGate(gate: Gate) {
    val client = OkHttpClient()
    val gson = Gson()
    val json = gson.toJson(gate)
    val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    val request = Request.Builder()
        .url("http://localhost:3210/gate")
        .post(requestBody)
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
    }
}

fun fetchLocations(): List<Location>? {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("http://localhost:3210/location")
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        val responseData = response.body?.string()

        return responseData?.let { parseLocations(it) }
    }
}

fun parseLocations(jsonData: String): List<Location> {
    val type = object : TypeToken<List<Location>>() {}.type
    return Gson().fromJson(jsonData, type)
}

fun addLocation(location: Location) {
    val client = OkHttpClient()
    val gson = Gson()
    val json = gson.toJson(location)
    val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    val request = Request.Builder()
        .url("http://localhost:3210/location")
        .post(requestBody)
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
    }
}

fun addAirport(airport: Airport) {
    val client = OkHttpClient()
    val gson = Gson()
    val json = gson.toJson(airport)
    val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    val request = Request.Builder()
        .url("http://localhost:3210/airport")
        .post(requestBody)
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
    }
}

fun fetchAirports(): List<Airport>? {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("http://localhost:3210/airport")
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        val responseData = response.body?.string()

        return responseData?.let { parseAirports(it) }
    }
}

fun parseAirports(jsonData: String): List<Airport> {
    val type = object : TypeToken<List<Airport>>() {}.type
    return Gson().fromJson(jsonData, type)
}

fun addFlight(flight: Flight) {
    val client = OkHttpClient()
    val gson = Gson()
    val json = gson.toJson(flight)
    val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    val request = Request.Builder()
        .url("http://localhost:3210/flight")
        .post(requestBody)
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
    }
}

fun fetchFlights(): List<Flight>? {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("http://localhost:3210/flight/to/simple")
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        val responseData = response.body?.string()

        return responseData?.let { parseFlights(it) }
    }
}

fun parseFlights(jsonData: String): List<Flight> {
    val type = object : TypeToken<List<Flight>>() {}.type
    return Gson().fromJson(jsonData, type)
}

// Fetch TrafficInfo from the database
fun fetchTrafficInfo(): List<TrafficInfo>? {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("http://localhost:3210/trafficinfo/to/simple") // Adjust the URL to your API endpoint
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        val responseData = response.body?.string()

        return responseData?.let { parseTrafficInfo(it) }
    }
}

// Parse the JSON response to a list of TrafficInfo objects
fun parseTrafficInfo(jsonData: String): List<TrafficInfo> {
    val type = object : TypeToken<List<TrafficInfo>>() {}.type
    return Gson().fromJson(jsonData, type)
}

fun fetchGateIdByLabel(label: String): String? {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("http://localhost:3210/gate/label/$label")
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        val responseBody = response.body?.string()
        val responseData = responseBody?.substring(1, responseBody.length - 1)

        return responseData
    }
}

fun addTrafficInfo(trafficInfo: TrafficInfo) {
    val client = OkHttpClient()
    val gson = Gson()
    val json = gson.toJson(trafficInfo)
    val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    val request = Request.Builder()
        .url("http://localhost:3210/trafficinfo")  // Change URL as needed
        .post(requestBody)
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
    }
}

fun addWeatherInfo(weatherInfo: WeatherInfo) {
    val client = OkHttpClient()
    val gson = Gson()
    val json = gson.toJson(weatherInfo)
    val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    val request = Request.Builder()
        .url("http://localhost:3210/weathercondition") // Adjust the URL to your API endpoint
        .post(requestBody)
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
    }
}

// Fetch WeatherInfo from the database
fun fetchWeatherInfo(): List<WeatherInfo>? {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("http://localhost:3210/weathercondition/to/simple")
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        val responseData = response.body?.string()

        return responseData?.let { parseWeatherInfo(it) }
    }
}

// Parse the JSON response to a list of WeatherInfo objects
fun parseWeatherInfo(jsonData: String): List<WeatherInfo> {
    val type = object : TypeToken<List<WeatherInfo>>() {}.type
    return Gson().fromJson(jsonData, type)
}

// Primer uporabe
/*fun main() {
    val gates = fetchGates()
    gates?.forEach { gate ->
        println("Gate: ${gate.id}, Label: ${gate.label}")
    }
}*/
