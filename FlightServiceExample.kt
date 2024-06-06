package org.example

import org.example.service.FlightService

fun main() {
    val flightService = FlightService()

    val flights = flightService.fetchFlights()

    flights.forEach { flight ->
        println("Flight ID: ${flight.id}")
        println("Flight Number: ${flight.flightNumber}")
        println("Start Destination: ${flight.startDestination}")
        println("End Destination: ${flight.endDestination}")
        println("Planned Departure Time: ${flight.timeDeparturePlanned}")
        println("Actual Departure Time: ${flight.timeDepartureActual}")
        println("Arrival Time: ${flight.arrivalTime}")
        println("Status: ${flight.status}")
        println()
    }
    println()
    println()

    // Generate dummy flights
    val dummyFlights = flightService.generateDummyFlights(5)
    println("\nDummy Flights Information:")
    println("- - -- - - - -  - --  -- - - - -- - - -- - - -- - - -- - - -- - - -- - -- ")
    dummyFlights.forEach { flight ->
        println("Flight ID: ${flight.id}")
        println("Flight Number: ${flight.flightNumber}")
        println("Start Destination: ${flight.startDestination}")
        println("End Destination: ${flight.endDestination}")
        println("Planned Departure Time: ${flight.timeDeparturePlanned}")
        println("Actual Departure Time: ${flight.timeDepartureActual}")
        println("Arrival Time: ${flight.arrivalTime}")
        println("Status: ${flight.status}")
        println()
    }

    // Get delays from FlightAware
    println()
    println()
    val YESTERDAY_URL = "https://www.flightaware.com/live/cancelled/yesterday/LJLJ";
    val TODAY_URL = "https://www.flightaware.com/live/cancelled/today/LJLJ";

    flightService.fetchDelays(YESTERDAY_URL);
    println()
    println()
    flightService.fetchDelays(TODAY_URL);
}
