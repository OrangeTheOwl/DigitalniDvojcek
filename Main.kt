import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.launch
import org.example.model.Flight
import org.example.model.TrafficInfo
import org.example.service.FlightService
import org.example.service.TrafficService
import androidx.compose.ui.window.rememberWindowState
import io.github.serpro69.kfaker.Faker

import java.util.*

/*fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "AirInsight"
    ) {
        val flightService = FlightService()
        val trafficService = TrafficService()
        val faker = Faker() // Ustvarite primer Fakerja tukaj

        val scope = rememberCoroutineScope()
        var flights by remember { mutableStateOf(listOf<Flight>()) }
        var trafficInfos by remember { mutableStateOf(listOf<TrafficInfo>()) }
        var generatedFlights by remember { mutableStateOf(listOf<Flight>()) }
        var generatedTrafficInfos by remember { mutableStateOf(listOf<TrafficInfo>()) }

        val windowState = rememberWindowState(width = 800.dp, height = 600.dp)

        Column(modifier = Modifier.padding(16.dp)) {
            Text("AirInsight", style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                scope.launch {
                    flights = flightService.fetchFlights()
                    trafficInfos = trafficService.fetchTrafficInfo()
                }
            }) {
                Text("Fetch Data")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                generatedFlights = flightService.generateDummyFlights(5) // Dodajte parametr faker tukaj
                generatedTrafficInfos = trafficService.generateDummyTrafficInfos(5)
            }) {
                Text("Generate Dummy Data")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Flights", style = MaterialTheme.typography.h5)
            flights.forEach { flight ->
                Text(flight.toString())
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Traffic Infos", style = MaterialTheme.typography.h5)
            trafficInfos.forEach { trafficInfo ->
                Text(trafficInfo.toString())
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Generated Flights", style = MaterialTheme.typography.h5)
            generatedFlights.forEach { flight ->
                Text(flight.toString())
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Generated Traffic Infos", style = MaterialTheme.typography.h5)
            generatedTrafficInfos.forEach { trafficInfo ->
                Text(trafficInfo.toString())
            }
        }
    }
}*/

