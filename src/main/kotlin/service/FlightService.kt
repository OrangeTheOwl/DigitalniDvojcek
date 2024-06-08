package service

import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.div
import it.skrape.selects.html5.img
import it.skrape.selects.html5.p
import model.Flight
import util.calcTimeDiff
import util.generateRandomString
import util.parseDate

class FlightService {
    fun fetchArrival(departure : Boolean): MutableList<Flight>? {
        try {
            var stringData : ArrayList<String>
            val flightData: MutableList<Flight> = mutableListOf()

            skrape(HttpFetcher) {
                request {
                    url = "https://www.lju-airport.si/sl/leti/odhodi-in-prihodi/"
                }

                response {
                    // Handle the response
                    println("HTTP Status Code: ${status { code }}")
                    println("HTTP Status Message: ${status { message }}")

                    htmlDocument {
                        div{
                            withClass = if(departure){
                                "js-tab.departures.selected"
                            }else {
                                "js-tab.arrivals.hidden"
                            }


                            div {
                                withClass = "quick-flights-item.js-search-row"
                                findAll {
                                    /*println("Found ${this.size} divs")*/
                                    forEach {

                                        stringData = ArrayList()
                                        it.p {
                                            findAll {
                                                forEach {data ->
                                                    try{
                                                        data.img {
                                                            withClass = "carrier-logo"
                                                            findFirst{
                                                                var altValue = this.attribute("alt") // get the value of the "alt" attribute
                                                                if(altValue.isBlank()){
                                                                    altValue = "Ni podatka"
                                                                }
                                                                stringData.add(altValue)
                                                            }

                                                        }

                                                    }catch (e : Exception){
                                                        stringData.add(data.text)
                                                    }
                                                }
                                            }
                                        }
                                        val date : String = stringData[0]
                                        val timePlanned : String = stringData.get(1).split(" ")[0]
                                        var timeExact : String = stringData[2]
                                        val gate : String = stringData[5]
                                        var status : String = stringData[7]

                                        /*println("FLIGHT DATA:")
                                        stringData.forEach{
                                            println(it)
                                        }
                                        println("END OF FLIGHT DATA:")*/

                                        if (timeExact.isEmpty()) {
                                            timeExact = timePlanned
                                        }

                                        if (status.isEmpty()) {
                                            status = "Brez Statusa"
                                        }

                                        val timeDiff = calcTimeDiff(timePlanned, timeExact)

                                        val flight = Flight(
                                            id = generateRandomString(24),
                                            arrivalPlanned = if (departure) null else parseDate(date, timePlanned),
                                            arrivalExact = if (departure) null else parseDate(date, timePlanned),
                                            departurePlanned = if (!departure) null else parseDate(date, timePlanned),
                                            departureExact = if (!departure) null else parseDate(date, timePlanned),
                                            airport = "6656ff7a99a43e1ec00e8357", //id od ljubljanskega letalisca
                                            departureChangeTime = timeDiff,
                                            destination = stringData[3].split(" ")[0],
                                            gate = gate,
                                            status = status,
                                            airline = stringData[4],
                                            flightNumber = stringData[3].split(" ")[1]
                                        )

                                        flightData.add(flight)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return flightData
        } catch (e: Exception) {
            println("An error occurred: ${e.message}")
            return null
        }
    }
}
/*fun main() {
    val flightService : FlightService = FlightService()

    val flightListDepartures : MutableList<Flight>? = flightService.fetchArrival(true)
    val flightListArrivals : MutableList<Flight>? = flightService.fetchArrival(false)

    println("Number of flights departure: " + flightListDepartures?.size)
    println("Number of flights arrival: " + flightListArrivals?.size)

    flightListDepartures?.forEach{
        println("------------------------------------")
        println(it.toString())
        println("------------------------------------")
    }
}*/
