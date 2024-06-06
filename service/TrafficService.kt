package org.example.service

import io.github.serpro69.kfaker.Faker
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.example.model.TrafficInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class TrafficService {
    private val trafficUrl = "https://www.promet.si/sl/dogodki"
    private val faker = Faker()

    fun fetchTrafficInfo(): List<TrafficInfo> {
        val trafficDocument: Document = Jsoup.connect(trafficUrl).get()
        val trafficItems = trafficDocument.select(".newsItem") // Adjust selector to match actual HTML structure

        val trafficInfos = mutableListOf<TrafficInfo>()
        trafficItems.forEach { item ->
            val description = item.select(".newsItemDescription").text().trim()
            val location = item.select(".newsItemLocation").text().trim()
            val dateStr = item.select(".newsItemDate").text().trim()
            val type = item.select(".newsItemType").text().trim()

            if (description.isNotBlank() && location.isNotBlank() && dateStr.isNotBlank() && type.isNotBlank()) {
                val date = parseTimestamp(dateStr)
                trafficInfos.add(
                    TrafficInfo(
                        id = generateId(),
                        description = description,
                        location = location,
                        date = date ?: LocalDateTime.now(), // Fallback to current time if parsing fails
                        type = type
                    )
                )
            }
        }
        return trafficInfos
    }

    fun generateDummyTrafficInfos(count: Int): List<TrafficInfo> {
        val trafficInfos = mutableListOf<TrafficInfo>()
        repeat(count) {
            trafficInfos.add(
                TrafficInfo(
                    id = UUID.randomUUID().toString(),
                    description = faker.lorem.toString(),
                    location = faker.address.city(),
                    date = LocalDateTime.now().plusDays(faker.random.nextLong()),
                    type = "Roadwork"
                )
            )
        }
        return trafficInfos
    }


    private fun generateId(): String {
        return UUID.randomUUID().toString()
    }

    private fun parseTimestamp(text: String): LocalDateTime? {
        val formatter = DateTimeFormatter.ofPattern("d. MMM yyyy HH:mm", Locale("sl", "SI"))
        return try {
            LocalDateTime.parse(text, formatter)
        } catch (e: Exception) {
            println("Failed to parse date: $text")
            null
        }
    }
}
