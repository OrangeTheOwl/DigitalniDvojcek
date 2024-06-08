package model

data class Airport(
    val id: String,
    val name: String,
    val location: String,
    val gates: ArrayList<String>
)