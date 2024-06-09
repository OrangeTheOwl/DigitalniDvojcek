package service

import model.TrafficInfo
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import util.generateRandomString
import java.util.concurrent.TimeUnit

class TrafficInfoService {
    fun fetchTrafficEvents(): MutableList<TrafficInfo>? {
        // Set up Chrome WebDriver
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe") //!Popravi si lokacijo svojega chrome driverja!
        val options = ChromeOptions()
        options.addArguments("--headless") // Run in headless mode
        val driver: WebDriver = ChromeDriver(options)

        return try {
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS)
            driver.get("https://www.promet.si/sl/dogodki")

            // Wait for the events to load
            Thread.sleep(1) // Adjust the sleep time as needed

            val trafficEvents: MutableList<TrafficInfo> = mutableListOf()
            val eventElements: List<WebElement> = driver.findElements(By.className("moj-promet-item"))

            eventElements.forEach{
                val cardData: List<WebElement> = it.findElements(By.className("p-2"))

                var stringData : List<String> = ArrayList()
                cardData.forEach{data->
                    stringData = (data.text.split("\n"))
                }
                val trafficEvent = TrafficInfo(
                    id = generateRandomString(24),
                    location = stringData[0],
                    delay = if (stringData.size == 3) {
                        stringData[2].substring(1, stringData[2].length).split(" ")[0].toInt()
                    } else {
                        0
                    },
                    status = stringData[1]
                )
                trafficEvents.add(trafficEvent)
            }

            return trafficEvents
        } catch (e: Exception) {
            println("An error occurred: ${e.message}")
            return null
        } finally {
            driver.quit()
        }
    }
}

/*fun main() {
    val trafficInfoService = TrafficInfoService()
    val trafficEvents = trafficInfoService.fetchTrafficEvents()

    println("Number of traffic events: ${trafficEvents?.size}")
    trafficEvents?.forEach {
        println("------------------------------------")
        println("id: ${it.id}")
        println("status: ${it.status}")
        println("location: ${it.location}")
        println("delay: ${it.delay}")
        println("------------------------------------")
    }
}*/
