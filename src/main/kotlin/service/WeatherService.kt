package service

import model.WeatherInfo
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import util.generateRandomHexString
import java.util.concurrent.TimeUnit

class WeatherService {
    fun fetchWeathervents(): MutableList<WeatherInfo>? {
        // Set up Chrome WebDriver
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe") //!Popravi si lokacijo svojega chrome driverja!
        val options = ChromeOptions()
        options.addArguments("--headless") // Run in headless mode
        val driver: WebDriver = ChromeDriver(options)

        return try {
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS)
            driver.get("https://vreme.arso.gov.si/napoved/Letali%C5%A1%C4%8De%20Jo%C5%BEeta%20Pu%C4%8Dnika%20Ljubljana/graf")

            // Wait for the events to load
            Thread.sleep(1) // Adjust the sleep time as needed

            val weatherInfo: MutableList<WeatherInfo> = mutableListOf()
            val eventElements: List<WebElement> = driver.findElements(By.className("current-weather"))

            eventElements.forEach{
                val stringData : List<String> = it.text.split("\n")
                val weatherEvent = WeatherInfo(
                    id = generateRandomHexString(24),
                    location = "6656ef9d83f28aa76711bac3",
                    status = stringData[0].trim(),
                    humidity = stringData[8].split(" ")[0].trim().toInt(),
                    humidityStatus = stringData[7].trim(),
                    windSpeed = stringData[5].split(" ")[0].trim().toInt(),
                    windStatus = stringData[4].trim(),
                    temperature = stringData[1].trim().toInt()
                )
                weatherInfo.add(weatherEvent)
            }
            return weatherInfo
        } catch (e: Exception) {
            println("An error occurred: ${e.message}")
            return null
        } finally {
            driver.quit()
        }
    }
}

fun main() {
    val weatherServiceInfo = WeatherService()
    val weatherEvents = weatherServiceInfo.fetchWeathervents()
    println("- - - - - - - - -  - --  -  --  - -")
    weatherEvents?.forEach {
        println(it.toString())
    }
    println("- - - - - - - - -  - --  -  --  - -")
}
