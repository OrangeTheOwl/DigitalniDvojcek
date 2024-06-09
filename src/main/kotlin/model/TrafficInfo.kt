package model

data class TrafficInfo (
    val id: String,
    val location: String,
    val delay: Int,
    val status: String
){
    override fun toString(): String {
        return "TrafficInfo(id='$id',\n" +
                "location='$location',\n" +
                "delay=$delay,\n" +
                "status='$status')"
    }
}