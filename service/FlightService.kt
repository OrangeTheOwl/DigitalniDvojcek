package org.example.service

import io.github.serpro69.kfaker.Faker
import org.example.model.Flight
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class FlightService {
    private val flightsUrl = "https://www.lju-airport.si/sl/leti/odhodi-in-prihodi/"
    private val faker = Faker()

    private val delayUrls = listOf(
        "https://www.flightaware.com/live/cancelled/yesterday/LJLJ",
        "https://www.flightaware.com/live/cancelled/today/LJLJ"
    )

    fun fetchFlights(): List<Flight> {
        return try {
            val flightsDocument = Jsoup.connect(flightsUrl).get()
            val flightsTable = flightsDocument.select("div.quick-flights-table-list")

            val flights = mutableListOf<Flight>()
            flightsTable.select("div.quick-flights-item").forEach { row ->
                val date = row.select("p.date").text().trim()
                val plannedTime = row.select("p.planned .schedule-time").text().trim()
                val actualTime = row.select("p.expected .schedule-time").text().trim()
                val destination = row.select("p.destination").text().trim()
                val flightNumber = row.select("p.carrier span").text().trim()
                val status = row.select("p.status").text().trim()

                if (date.isNotBlank() && plannedTime.isNotBlank() && destination.isNotBlank() && flightNumber.isNotBlank()) {
                    flights.add(
                        Flight(
                            id = generateId(),
                            flightNumber = flightNumber,
                            startDestination = "Ljubljana",
                            endDestination = destination,
                            timeDeparturePlanned = ("$date $plannedTime"),
                            timeDepartureActual = if (actualTime.isEmpty()) null else ("$date $actualTime"),
                            arrivalTime = null, // čas prihoda ni prisoten na strani
                            status = status
                        )
                    )
                }
            }
            flights
        } catch (e: Exception) {
            println("Error fetching flights: ${e.message}")
            emptyList()
        }
    }

    fun generateDummyFlights(count: Int): List<Flight> {
        val flights = mutableListOf<Flight>()
        repeat(count) {
            flights.add(
                Flight(
                    id = UUID.randomUUID().toString(),
                    flightNumber = faker.random.nextInt(1000, 9999).toString(),
                    startDestination = faker.address.city(),
                    endDestination = faker.address.city(),
                    timeDeparturePlanned = LocalDateTime.now().toString(),
                    timeDepartureActual = null,
                    arrivalTime = null,
                    status = "Scheduled"
                )
            )
        }
        return flights
    }

    fun fetchDelays(url: String) {
        try {
            val document: Document = Jsoup.connect(url).get()


            // Update the selectors to match the correct HTML structure
            var totalDelays = getElementText(document, "Total delays today at Brnik (Ljubljana):")
            var usDelays = getElementText(
                document,
                "Total delays within, into, or out of the United States today at Brnik (Ljubljana):"
            )
            var totalCancellations = getElementText(document, "Total cancellations today at Brnik (Ljubljana):")
            var usCancellations = getElementText(
                document,
                "Total cancellations within, into, or out of the United States today at Brnik (Ljubljana):"
            )

            if (totalDelays == null) {
                totalDelays = getElementText(document, "Total delays yesterday at Brnik (Ljubljana):")
            }
            if (usDelays == null) {
                usDelays = getElementText(
                    document,
                    "Total delays within, into, or out of the United States yesterday at Brnik (Ljubljana):"
                )
            }
            if (totalCancellations == null) {
                totalCancellations = getElementText(document, "Total cancellations yesterday at Brnik (Ljubljana):")
            }
            if (usCancellations == null) {
                usCancellations = getElementText(
                    document,
                    "Total cancellations within, into, or out of the United States yesterday at Brnik (Ljubljana):"
                )
            }

            println("Total delays: " + (totalDelays ?: "Not found"))
            println("Total delays within, into, or out of the United States: " + (usDelays ?: "Not found"))
            println("Total cancellations: " + (totalCancellations ?: "Not found"))
            println(
                "Total cancellations within, into, or out of the United States: " + (usCancellations
                    ?: "Not found")
            )
        } catch (e: IOException) {
            System.err.println("Error fetching delays from " + url + ": " + e.message)
        }
    }

    private fun getElementText(document: Document, searchText: String): String? {
        for (element in document.select("div[id='flightPageSummary'] div")) {
            if (element.text().contains(searchText)) {
                return element.text().replace(searchText, "").trim()
            }
        }
        return null
    }

    private fun generateId(): String {
        return UUID.randomUUID().toString()
    }

    private fun parseTimestamp(text: String): LocalDateTime? {
        val formatter = DateTimeFormatter.ofPattern("d. MMM HH:mm", Locale("sl", "SI"))
        val fullFormatter = DateTimeFormatter.ofPattern("d. MMM yyyy HH:mm", Locale("sl", "SI"))
        return try {
            // Parse without year
            LocalDateTime.parse(text, formatter)
        } catch (e: Exception) {
            try {
                // Append the current year and parse
                val now = LocalDateTime.now()
                val currentYear = now.year
                LocalDateTime.parse("$text $currentYear", fullFormatter)
            } catch (e: Exception) {
                println("Failed to parse date: $text")
                null
            }
        }
    }
}
