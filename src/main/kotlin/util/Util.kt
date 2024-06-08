package util

import java.text.SimpleDateFormat
import java.util.*

    fun parseDate(dateString: String, timeString: String): Date {
        val monthMap = mapOf(
            "jan." to "01",
            "feb." to "02",
            "mar." to "03",
            "apr." to "04",
            "maj." to "05",
            "jun." to "06",
            "jul." to "07",
            "avg." to "08",
            "sep." to "09",
            "okt." to "10",
            "nov." to "11",
            "dec." to "12"
        )

        val parts = dateString.split(" ")
        val day = parts[0].replace(".", "")
        val month = monthMap[parts[1]]

        val numericDateString = "$day.$month."

        val dateFormat = SimpleDateFormat("dd.MM.", Locale("sl", "SI"))
        val timeFormat = SimpleDateFormat("HH:mm", Locale("sl", "SI"))

        // Parsing date and time strings to Date objects
        val parsedDate = dateFormat.parse(numericDateString)
        val parsedTime = timeFormat.parse(timeString)

        // Getting the current year
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        // Combining date and time
        val calendar = Calendar.getInstance()
        calendar.time = parsedDate
        calendar.set(Calendar.YEAR, currentYear)
        calendar.set(Calendar.HOUR_OF_DAY, parsedTime.hours)
        calendar.set(Calendar.MINUTE, parsedTime.minutes)
        calendar.set(Calendar.SECOND, 0)

        return calendar.time
    }

    fun calcTimeDiff(timePlanned: String, timeExact: String): Int {
        val (hourP, minuteP) = timePlanned.split(":").map { it.toInt() }
        val (hourE, minuteE) = timeExact.split(":").map { it.toInt() }

        val planned = hourP * 60 + minuteP
        val exact = hourE * 60 + minuteE

        return exact - planned
    }

    fun generateRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

/*fun main() {
    val dateString = "10. jun."
    val timeString = "19:40"
    val dateString2 = "11. jun."
    val timeString2 = "05:05"
    val dateString3 = "9. jun."
    val timeString3 = "20:30"




    val formattedDateTime = DateFormater.parseDate(dateString, timeString)
    val formattedDateTime2 = DateFormater.parseDate(dateString2, timeString2)
    val formattedDateTime3 = DateFormater.parseDate(dateString3, timeString3)
    println(formattedDateTime) // Output: YYYY.mm.DDTHH:MM:00
    println(formattedDateTime2)
    println(formattedDateTime3)
}*/

/*fun main() {
    val cas1 = "10:05"
    val cas2 = "10:12"
    val cas3 = "10:10"
    val cas4 = "10:07"

    val razlika1 = calcTimeDiff(cas1, cas2)
    val razlika2 = calcTimeDiff(cas3, cas4)

    println("Razlika med $cas1 in $cas2 = $razlika1")
    println("Razlika med $cas3 in $cas4 = $razlika2")
}*/

/*fun main() {
    val randomString = generateRandomString(24)
    val randomString2 = generateRandomString(24)
    val randomString3 = generateRandomString(24)
    val randomString4 = generateRandomString(24)
    println(randomString)
    println(randomString2)
    println(randomString3)
    println(randomString4)
}*/