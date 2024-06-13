package util

import io.github.serpro69.kfaker.Faker
import io.github.serpro69.kfaker.faker
import model.Flight
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

    fun generateRandomHexString(length: Int): String {
        val allowedChars = ('0'..'9') + ('a'..'f')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun generateAirportData(): Map<String, String> {
        val faker = Faker()
        return mapOf(
            "Name" to faker.company.name(),
            "Location" to generateRandomHexString(24),
            "Gates" to generateRandomHexString(24)
        )
    }

fun generateFlightData(): Map<String, String?> {
    val faker = Faker()
    val departurePlanned = getRandomFutureDate()
    val arrivalPlanned = addRandomTime(departurePlanned)
    val departureExact = departurePlanned
    val arrivalExact = arrivalPlanned
    var departureChangeTime = faker.random.nextInt(0, 120) // Change in departure time in minutes
    val status = listOf("PRIJAVA", "NOVI ČAS", "ODLETEL", "BREZ STATUSA").random()
    val destination = getRandomCityIndex()
    val airport = generateRandomHexString(24)
    val gate = getRandomGate()
    val airline = faker.company.name()
    val flightNumber = faker.company.name().substring(0,2) + faker.random.nextInt(1000, 9999).toString()

    departureExact.minutes = faker.random.nextInt(1, 59)
    departureChangeTime = calcTimeDiff(departurePlanned.hours.toString() + ":" + departurePlanned.minutes.toString(),departureExact.hours.toString() + ":" + departureExact.minutes.toString())

    return mapOf(
        "arrivalPlanned" to arrivalPlanned.toString(),
        "arrivalExact" to arrivalExact.toString(),
        "departurePlanned" to departurePlanned.toString(),
        "departureExact" to departureExact.toString(),
        "departureChangeTime" to departureChangeTime.toString(),
        "status" to status,
        "destination" to destination,
        "airport" to airport,
        "gate" to gate,
        "airline" to airline,
        "flightNumber" to flightNumber
    )
}

        fun generateGateData(): Map<String, String> {
            val faker = Faker()
            return mapOf(
                "Label" to faker.random.randomString(3, numericalChars = true)
            )
        }



    fun generateCustomCode(): String {
        val letters = listOf('A', 'B', 'C', 'D')  // Črke za vaš vzorec
        val numbers = (1..5).toList()  // Številke za vaš vzorec

        val letter = letters.random()  // Naključna črka iz seznama
        val number = numbers.random()  // Naključna številka iz seznama


        return "$letter$number"
    }

    fun generateLocationData(): Map<String, String> {
        val faker = Faker()
        return mapOf(
            "Address" to faker.address.streetAddress(),
            "City" to faker.address.city(),
            "Postal Code" to faker.address.postcode()
        )
    }

    fun generateTrafficInfoData(): Map<String, String> {
        val faker = Faker()
        return mapOf(
            "Delay" to faker.random.nextInt(1,120).toString(),
            "Status" to listOf("brez zastojev", "zastoj", "nesreča", "delo na cesti", "", "območje zastojev", "popolna zapora", "promet preusmerjen skozi počivališče", "dela", "poplave").random()
        )
    }
    fun generateWeatherInfoData(): Map<String, String> {
        val faker = Faker()
        return mapOf(
            "Location" to faker.address.city(),
            "Temperature" to faker.random.nextInt(-7, 35).toString(),
            "Humidity" to faker.random.nextInt(24, 53).toString(),
            "Humidity Status" to listOf("Nizek", "Visok", "Normalen", "").random(),
            "Wind Speed" to faker.random.nextInt(0, 49).toString(),
            "Wind Direction" to listOf("šibek JZ", "šibek SZ", "močan SV", "šibek SV", "močan S", "šibek J").random(),
            "Status" to listOf("sončno", "oblačno", "dež", "megla", "deževno", "dežuje").random()
        )
    }

    fun getRandomCityIndex(): String {
        val realCities = listOf(
            // USA
            "New York", "Los Angeles", "Chicago", "Houston", "Phoenix",
            "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose",
            "Austin", "Jacksonville", "Fort Worth", "Columbus", "Charlotte",
            "San Francisco", "Indianapolis", "Seattle", "Denver", "Washington",

            // Canada
            "Toronto", "Vancouver", "Montreal",

            // UK
            "London", "Manchester", "Birmingham",

            // Germany
            "Berlin", "Hamburg", "Munich",

            // France
            "Paris", "Marseille", "Lyon",

            // Italy
            "Rome", "Milan", "Naples",

            // Spain
            "Madrid", "Barcelona", "Valencia",

            // Australia
            "Sydney", "Melbourne", "Brisbane",

            // Japan
            "Tokyo", "Osaka", "Nagoya",

            // China
            "Beijing", "Shanghai", "Guangzhou",

            // India
            "Mumbai", "Delhi", "Bangalore",

            // Brazil
            "São Paulo", "Rio de Janeiro", "Brasília",

            // Mexico
            "Mexico City", "Guadalajara", "Monterrey",

            // Russia
            "Moscow", "Saint Petersburg", "Novosibirsk",

            // South Korea
            "Seoul", "Busan", "Incheon",

            // South Africa
            "Johannesburg", "Cape Town", "Durban",

            // Argentina
            "Buenos Aires", "Córdoba", "Rosario",

            // Turkey
            "Istanbul", "Ankara", "Izmir",

            // Egypt
            "Cairo", "Alexandria", "Giza",

            // Nigeria
            "Lagos", "Abuja", "Ibadan",

            // Indonesia
            "Jakarta", "Surabaya", "Bandung",

            // Saudi Arabia
            "Riyadh", "Jeddah", "Mecca",

            // Thailand
            "Bangkok", "Chiang Mai", "Pattaya",

            // Vietnam
            "Hanoi", "Ho Chi Minh City", "Da Nang",

            // Philippines
            "Manila", "Cebu", "Davao",

            // Malaysia
            "Kuala Lumpur", "Penang", "Johor Bahru"
        )


        val faker = Faker()

        return realCities[faker.random.nextInt(0, 94)]
    }

fun getRandomGate(): String {
    val gateIds = listOf(
        "6654c5cd252ca156a851bcfe", "6654c5d2252ca156a851bd00", "6654c5d5252ca156a851bd02", "6654c5da252ca156a851bd04",
        "666414f7e0e4ecbba70f3314", "6664155ee0e4ecbba70f3315", "66641566e0e4ecbba70f3316", "6664156ee0e4ecbba70f3317",
        "666415b3e0e4ecbba70f3318", "666415bae0e4ecbba70f3319", "666415c0e0e4ecbba70f331a", "666629f23d42466311c12b1f",
        "66662b953d42466311c12b22", "66662bee3d42466311c12b25", "66662bf53d42466311c12b27"
    )



    val faker = Faker()

    return gateIds[faker.random.nextInt(0, 13)]
}

    fun getRandomFutureDate(): Date {
        val calendar = Calendar.getInstance()
        val currentDate = calendar.timeInMillis
        val maxFutureMillis = currentDate + (7 * 24 * 60 * 60 * 1000) // 7 dni v milisekundah
        var random : Random = Random()
        val randomMillis = random.nextLong(currentDate, maxFutureMillis)

        calendar.timeInMillis = randomMillis

        return calendar.time
    }

    fun addRandomTime(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        var random : Random = Random()
        // Naključno določimo število ur in minut med 1,5 in 5
        val randomHours = random.nextDouble(1.5, 5.0).toInt()
        val randomMinutes = random.nextInt(0, 60)

        // Dodamo naključno število ur in minut k trenutnemu datumu
        calendar.add(Calendar.HOUR_OF_DAY, randomHours)
        calendar.add(Calendar.MINUTE, randomMinutes)

        return calendar.time
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