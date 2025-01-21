package com.example.airportmobile.database

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray

class ApiService {
    private val client = OkHttpClient()

    fun sendTestDataToApi(testData: List<TestDataEntity>) {
        val url = "https://your-api-endpoint.com/submit-test-data" // Zamenjaj z API URL

        val jsonArray = JSONArray()
        testData.forEach { data ->
            val jsonObject = org.json.JSONObject()
            jsonObject.put("locationId", data.locationId)
            jsonObject.put("locationName", data.locationName)
            jsonObject.put("crowdNumber", data.crowdNumber)
            jsonObject.put("timestamp", data.timestamp)
            jsonArray.put(jsonObject)
        }

        val requestBody = jsonArray.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                println("Data sent successfully")
            } else {
                println("Failed to send data: ${response.code}")
            }
        }
    }
}
