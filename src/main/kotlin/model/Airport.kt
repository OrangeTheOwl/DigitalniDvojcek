package model

import com.google.gson.annotations.SerializedName

data class Airport(
    @SerializedName("_id") val id: String? = null,
    val name: String? = null,
    val location: Location? = null,
    val gates: List<Gate>? = null
)