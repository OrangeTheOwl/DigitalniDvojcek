package model

import com.google.gson.annotations.SerializedName


data class Gate(
    @SerializedName("_id") val id: String? = null,
    val label: String
)