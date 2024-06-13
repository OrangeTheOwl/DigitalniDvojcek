package service

import fetchGateIdByLabel
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.div
import it.skrape.selects.html5.img
import it.skrape.selects.html5.p
import model.Airport
import model.Flight
import model.Gate
import model.Location
import util.calcTimeDiff
import util.generateRandomHexString
import util.getRandomGate
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
                                    forEach {

                                        stringData = ArrayList()
                                        it.p {
                                            findAll {
                                                forEach {data ->
                                                    try{
                                                        data.img {
                                                            withClass = "carrier-logo"
                                                            findFirst{
                                                                var altValue = this.attribute("alt")
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

                                        if (timeExact.isEmpty()) {
                                            timeExact = timePlanned
                                        }

                                        if (status.isEmpty()) {
                                            status = "Brez Statusa"
                                        }
                                        val gateId : String? = if(gate.trim().isEmpty()){
                                            getRandomGate()
                                        }else{
                                            fetchGateIdByLabel(gate)
                                        }



                                        val timeDiff = calcTimeDiff(timePlanned, timeExact)
                                        val destList = stringData[3].split(" ")
                                        val flight = Flight(
                                            id = generateRandomHexString(24),
                                            arrivalPlanned = if (departure) null else parseDate(date, timePlanned),
                                            arrivalExact = if (departure) null else parseDate(date, timePlanned),
                                            departurePlanned = if (!departure) null else parseDate(date, timePlanned),
                                            departureExact = if (!departure) null else parseDate(date, timePlanned),
                                            airport = "6656ff7a99a43e1ec00e8357",
                                            changeTime = timeDiff,
                                            destination = if (destList.size == 3) "${destList[0].trim()} ${destList[1].trim()}" else destList[0].trim() ,
                                            gate = gateId!!,
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
fun main() {
    val flightService : FlightService = FlightService()

    val flightListDepartures : MutableList<Flight>? = flightService.fetchArrival(true)

    println("Number of flights departure: " + flightListDepartures?.size)

    flightListDepartures?.forEach{
        println("------------------------------------")
        println(it.toString())
        println("------------------------------------")
    }
}
