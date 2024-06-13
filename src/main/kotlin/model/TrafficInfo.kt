package model

import com.google.gson.annotations.SerializedName
import java.util.*

data class TrafficInfo (
    @SerializedName("_id") val id: String? = null,
    val location: String,
    val delay: Int,
    val status: String,
    val time : Date
){
    override fun toString(): String {
        return "TrafficInfo(id='$id',\n" +
                "location='$location',\n" +
                "delay=$delay,\n" +
                "status='$status'\n" +
                "time='$time')"
     }
}