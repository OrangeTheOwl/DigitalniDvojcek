package model

import com.google.gson.annotations.SerializedName


data class Location(
    @SerializedName("_id") val id: String,
    val address: String? = null,
    val city: String? = null,
    val postalCode: String? = null
)