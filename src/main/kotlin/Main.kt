import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.*
import okio.IOException
import service.FlightService
import service.TrafficInfoService
import service.WeatherService
import util.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess


@Composable
fun MyApp(onCloseRequest: () -> Unit) {
    val tables = listOf("airports", "flights", "gates", "locations", "trafficinfos", "weatherconditions", "generate")
    var selectedTable by remember { mutableStateOf(tables.first()) }
    var isSidebarCollapsed by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxSize()) {
                if (!isSidebarCollapsed) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(200.dp)
                            .background(Color(0xFF365E32))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Tabele",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        tables.forEach { table ->
                            Text(
                                text = table.capitalize(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable { selectedTable = table }
                                    .background(
                                        if (selectedTable == table) Color(0xFFFD9B63) else Color.Transparent,
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .padding(12.dp),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                        Button(
                            onClick = { showInfoDialog = true },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))
                        ) {
                            Text(
                                text = "Info",
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f)) // Push the collapse button to the bottom
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Collapse Sidebar",
                            tint = Color.White,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { isSidebarCollapsed = true }
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(50.dp)
                            .background(Color(0xFFFD9B63))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Expand Sidebar",
                            tint = Color.White,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { isSidebarCollapsed = false }
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = selectedTable.capitalize(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF365E32)
                        )
                        IconButton(onClick = onCloseRequest) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Application"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TableContent(table = selectedTable)
                }
            }
        }
    }

    if (showInfoDialog) {
        InfoDialog(onDismissRequest = { showInfoDialog = false })
    }
}

@Composable
fun SideBarExpanded(tables: List<String>, selectedTable: String, onSelectTable: (String) -> Unit, onShowDialog: () -> Unit, onCollapse: () -> Unit) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .fillMaxHeight()
            .background(Color(0xFF365E32))  // Updated background color
            .padding(16.dp)
    ) {
        Text("Tables", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF81A263))  // Updated text color
        Spacer(modifier = Modifier.height(16.dp))
        tables.forEach { table ->
            Text(
                text = table.replaceFirstChar { it.uppercase() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onSelectTable(table) }
                    .background(
                        if (selectedTable == table) Color(0xFFE7D37F) else Color.Transparent,  // Updated selected table background
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = {
                when (selectedTable) {
                    "airports" -> generateAirportData()
                    "flights" -> generateFlightData()
                    "gates" -> generateGateData()
                    "locations" -> generateLocationData()
                    "trafficinfos" -> generateTrafficInfoData()
                    "weatherconditions" -> generateWeatherInfoData()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))  // Updated button color
        ) {
            Text("Generate")
        }
        Button(
            onClick = onShowDialog,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))  // Updated button color
        ) {
            Text("Generate")
        }
        Button(
            onClick = onShowDialog,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))  // Updated button color
        ) {
            Text("Info")
        }
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Collapse Sidebar",
            tint = Color(0xFF81A263),  // Updated icon tint
            modifier = Modifier
                .size(24.dp)
                .clickable { onCollapse() }
                .align(Alignment.End)
        )
    }
}



@Composable
fun SideBarCollapsed(onExpand: () -> Unit) {
    Column(
        modifier = Modifier
            .width(50.dp)
            .fillMaxHeight()
            .background(Color(0xFF365E32))  // Updated background color
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Expand Sidebar",
            tint = Color(0xFF81A263),  // Updated icon tint
            modifier = Modifier
                .size(24.dp)
                .clickable { onExpand() }
        )
    }
}

@Composable
fun RightContentPanel(selectedTable: String, modifier: Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = selectedTable.replaceFirstChar { it.uppercase() }, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF81A263))  // Updated text color
        Spacer(modifier = Modifier.height(16.dp))
        TableContent(table = selectedTable)
    }
}

@Composable
fun TableContent(table: String) {
    Column {
        when (table) {
            "airports" -> AirportForm()
            "flights" -> FlightForm()
            "gates" -> GateForm()
            "locations" -> LocationForm()
            "trafficinfos" -> TrafficInfoForm()
            "weatherconditions" -> WeatherInfoForm()
            "generate" -> GenerateDataForm(listOf("airports", "flights", "gates", "locations", "trafficinfos", "weatherconditions"))
            else -> Text("Select a table", fontSize = 18.sp)
        }
    }
}

@Composable
fun AirportForm() {
    var name by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf<Location?>(null) }
    var selectedGate by remember { mutableStateOf<Pair<String, String>?>(null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var airports = remember { mutableStateOf<List<Airport>>(emptyList()) }

    val gateOptions by produceState(initialValue = emptyList<Pair<String, String>>()) {
        scope.launch {
            value = withContext(Dispatchers.IO) {
                fetchGates()?.map { it.id.toString() to it.label } ?: emptyList()

            }
        }
    }

    val locationOptions by produceState(initialValue = emptyList<Location>()) {
        scope.launch {
            value = withContext(Dispatchers.IO) {
                fetchLocations() ?: emptyList()
            }
        }
    }

    Column {
        Text("Name", fontWeight = FontWeight.Bold)
        TextField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Text("Location", fontWeight = FontWeight.Bold)
        var expandedLocation by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Button(onClick = { expandedLocation = true }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))) {
                Text(text = (selectedLocation?.address ?: "Select Location"), color = Color.White)
            }
            DropdownMenu(expanded = expandedLocation, onDismissRequest = { expandedLocation = false }) {
                locationOptions.forEach { location ->
                    DropdownMenuItem(onClick = {
                        selectedLocation = location
                        expandedLocation = false
                    }) {
                        Text(text = location.address!! + " " + location.city!!)
                    }
                }
            }
        }

        Text("Gates", fontWeight = FontWeight.Bold)
        var expandedGate by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Button(onClick = { expandedGate = true }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))) {
                Text(text = (selectedGate?.second ?: "Select gate"), color = Color.White)
            }
            DropdownMenu(expanded = expandedGate, onDismissRequest = { expandedGate = false }) {
                gateOptions.forEach { gate ->
                    DropdownMenuItem(onClick = {
                        selectedGate = gate
                        expandedGate = false
                    }) {
                        Text(text = gate.second)
                    }
                }
            }
        }

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Button(onClick = {
                val data = generateAirportData()
                name = data["Name"]!!
            },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63)),
                modifier = Modifier.padding(end = 8.dp)) {
                Text(text = "Generator", color = Color.White)
            }

            Button(onClick = {
                airports.value = emptyList()
                if (name.isNotEmpty() && selectedLocation != null && selectedGate != null) {
                    val airport = Airport(
                        name = name,
                        location = selectedLocation!!,
                        gates = listOf(Gate(id = selectedGate!!.first, label = selectedGate!!.second))
                    )
                    scope.launch {
                        try {
                            withContext(Dispatchers.IO) {
                                addAirport(airport)
                            }
                            snackbarHostState.showSnackbar("Airport added successfully")
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Failed to add airport: ${e.message}")
                        }
                    }
                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar("Please fill all fields correctly")
                    }
                }
            },
                modifier = Modifier.padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))) {
                Text(text = "Dodaj v bazo", color = Color.White)
            }

            // New button to fetch and display airports from the database
            Button(
                onClick = {
                    airports.value = emptyList()
                    scope.launch {
                        try {
                            val fetchedAirports = withContext(Dispatchers.IO) {
                                fetchAirports()
                            }
                            airports.value = fetchedAirports ?: emptyList()
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Failed to fetch airports: ${e.message}")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))
            ) {
                Text(text = "Podatki v bazi", color = Color.White)
            }
        }

        // Display the list of airports fetched from the database
        if (airports.value.isNotEmpty()) {
            Text("Airports in Database", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
            LazyColumn {
                items(airports.value) { airport ->
                    AirportItem(airport)
                }
            }
        } else {
            Text("No airports available", modifier = Modifier.padding(16.dp))
        }
    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        SnackbarHost(hostState = snackbarHostState)
    }
}

@Composable
fun AirportItem(airport: Airport) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded },
        elevation = 4.dp
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Place, // Prilagodite ikono po potrebi
                    contentDescription = "Airport Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                airport.name?.let {
                    Text(text = it)
                } ?: run {
                    Text(text = "ni podatka")
                }
            }

            // Podrobne informacije, prikazane, ko je 'expanded' true
            if (expanded) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    Text(
                        text = "Lokacija:",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    airport.location?.let { location ->
                        Column(
                            modifier = Modifier.padding(start = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("- id: ${location.id}", style = MaterialTheme.typography.body2)
                            Text("- address: ${location.address}", style = MaterialTheme.typography.body2)
                            Text("- city: ${location.city}", style = MaterialTheme.typography.body2)
                            Text("- postalCode: ${location.postalCode}", style = MaterialTheme.typography.body2)
                        }
                    } ?: run {
                        Text(text = "ni podatka", style = MaterialTheme.typography.body2)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    airport.gates?.let { gates ->
                        Text(
                            text = "Vrata:",
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Column(
                            modifier = Modifier.padding(start = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            gates.forEach { gate ->
                                Text("- id: ${gate.id}", style = MaterialTheme.typography.body2)
                                Text("- label: ${gate.label}", style = MaterialTheme.typography.body2)
                            }
                        }
                    } ?: run {
                        Text(text = "ni podatka", style = MaterialTheme.typography.body2)
                    }
                }
            }
        }
    }
}

@Composable
fun FlightForm() {
    var arrivalPlanned by remember { mutableStateOf("") }
    var departurePlanned by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var gate by remember { mutableStateOf("") }
    var airline by remember { mutableStateOf("") }
    var flightNumber by remember { mutableStateOf("") }
    var flights by remember { mutableStateOf<List<Flight>?>(null) }
    val flightService: FlightService = FlightService()
    var isLoading by remember { mutableStateOf(false) }
    var isAdding by remember { mutableStateOf(false) }
    var canInsertIntoDB by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState() // Dodano
    val listState = rememberLazyListState()

    val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

    Column {
        Text("Arrival Planned", fontWeight = FontWeight.Bold)
        TextField(value = arrivalPlanned, onValueChange = { arrivalPlanned = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Text("Departure Planned", fontWeight = FontWeight.Bold)
        TextField(value = departurePlanned, onValueChange = { departurePlanned = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Text("Destination", fontWeight = FontWeight.Bold)
        TextField(value = destination, onValueChange = { destination = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Text("Gate", fontWeight = FontWeight.Bold)
        TextField(value = gate, onValueChange = { gate = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Text("Airline", fontWeight = FontWeight.Bold)
        TextField(value = airline, onValueChange = { airline = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Text("Flight Number", fontWeight = FontWeight.Bold)
        TextField(value = flightNumber, onValueChange = { flightNumber = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Button(
                onClick = {
                    val data = generateFlightData()
                    arrivalPlanned = data["arrivalPlanned"] ?: ""
                    departurePlanned = data["departurePlanned"] ?: ""
                    destination = data["destination"] ?: ""
                    gate = data["gate"] ?: ""
                    airline = data["airline"] ?: ""
                    flightNumber = data["flightNumber"] ?: ""
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63)),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = "Generator", color = Color.White)
            }

            Button(
                onClick = {
                    scope.launch {
                        try {
                            val arrivalDate = if (arrivalPlanned.isNotBlank()) dateFormat.parse(arrivalPlanned) else null
                            val departureDate = if (departurePlanned.isNotBlank()) dateFormat.parse(departurePlanned) else null

                            val calendar = Calendar.getInstance()
                            val arrivalExactDate = arrivalDate?.let {
                                calendar.time = it
                                calendar.add(Calendar.MINUTE, 30)
                                calendar.time
                            } ?: Date()
                            val flight = Flight(
                                flightNumber = flightNumber,
                                destination = destination,
                                gate = gate,
                                airline = airline,
                                arrivalPlanned = arrivalDate,
                                departurePlanned = departureDate,
                                arrivalExact = arrivalExactDate,
                                departureExact = departureDate ?: Date(),
                                changeTime = calcTimeDiff(arrivalDate?.hours.toString() + ":" + arrivalDate?.minutes.toString(),arrivalExactDate.hours.toString() + ":" + arrivalExactDate.minutes.toString()),
                                airport = "6656ff7a99a43e1ec00e8357",
                                status = "BREZ STATUSA"
                            )
                            addFlight(flight)
                            snackbarHostState.showSnackbar("Flight added successfully")
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Failed to add flight: ${e.message}")
                        }
                    }
                },
                modifier = Modifier.padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))
            ) {
                Text(text = "Dodaj v bazo", color = Color.White)
            }

            Button(
                onClick = {
                    flights = null
                    isLoading = true
                    scope.launch {
                        try {
                            flights = withContext(Dispatchers.IO) {
                                flightService.fetchArrival(true)
                            }

                            if(flights?.isEmpty() == false){
                                canInsertIntoDB = true
                            }else {
                                canInsertIntoDB = false
                            }
                            isLoading = false
                        } catch (e: Exception) {
                            isLoading = false
                            snackbarHostState.showSnackbar("Failed to fetch flights: ${e.message}")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63)),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = "Scrapper", color = Color.White)
            }

            Button(
                onClick = {
                    isAdding = true
                    scope.launch {
                        try {
                            flights?.forEach { flight ->
                                if (flight.gate.isEmpty()){
                                    flight.gate = getRandomGate()
                                }

                                val date : Date = addRandomTime(flight.departurePlanned!!)
                                flight.arrivalPlanned = date
                                flight.arrivalExact = date
                                addFlight(flight)
                            }
                            isAdding = false
                            flights = null
                            canInsertIntoDB = false
                            snackbarHostState.showSnackbar("All flights added successfully")
                            // Remove all FlightItems after successful insertion

                        } catch (e: Exception) {
                            isAdding = false
                            snackbarHostState.showSnackbar("Failed to add flights: ${e.message}")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63)),
                modifier = Modifier.padding(end = 8.dp),
                enabled = canInsertIntoDB,
            ) {
                Text(text = "Pošlji vse v bazo", color = Color.White)
            }
            // New button to fetch and display airports from the database
            Button(
                onClick = {
                    flights = null
                    canInsertIntoDB = false
                    scope.launch {
                        try {
                            val fetchedFlights = withContext(Dispatchers.IO) {
                                fetchFlights()
                            }
                            flights = fetchedFlights ?: emptyList()

                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Failed to fetch flights: ${e.message}")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))
            ) {
                Text(text = "Podatki v bazi", color = Color.White)
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Pridobivanje podatkov...")
                }
            }
        }

        if (isAdding) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Pošiljanje podatkov...")
                }
            }
        }

        flights?.let { flightList ->
            LazyColumn(
                modifier = Modifier.weight(1f),
                state = scrollState // Dodano
            ) {
                items(flightList) { flight ->
                    FlightItem(flight)
                }
            }
        } ?: Text("No flights available", modifier = Modifier.padding(16.dp))
    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        SnackbarHost(hostState = snackbarHostState)
    }
}



@Composable
fun FlightItem(flight: Flight) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.clickable { expanded = !expanded }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info, // Ikona letala
                    contentDescription = "Flight Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Flight ${flight.flightNumber} - ${flight.airline}")
            }

            // Podrobni podatki se prikažejo, ko je 'expanded' true
            if (expanded) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    Text("Arrival Planned: ${flight.arrivalPlanned}")
                    Text("Arrival Exact: ${flight.arrivalExact}")
                    Text("Departure Planned: ${flight.departurePlanned}")
                    Text("Departure Exact: ${flight.departureExact}")
                    Text("Departure Change Time: ${flight.changeTime}")
                    Text("Status: ${flight.status}")
                    Text("Destination: ${flight.destination}")
                    Text("Airport: ${flight.airport}")
                    Text("Gate: ${flight.gate}")
                    Text("Airline: ${flight.airline}")
                    Text("Flight Number: ${flight.flightNumber}")
                }
            }
        }
    }
}

@Composable
fun GateForm() {
    var label by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var gates by remember { mutableStateOf<List<Gate>?>(null) }

    Column {
        Text("Label", fontWeight = FontWeight.Bold)
        TextField(value = label, onValueChange = { label = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Button(
                onClick = {
                    val data = generateGateData()
                    label = data["Label"]!!
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63)),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = "Generator", color = Color.White)
            }

            Button(
                onClick = {
                    scope.launch {
                        try {
                            withContext(Dispatchers.IO) {
                                addGate(Gate(label = label))
                            }
                            snackbarHostState.showSnackbar(
                                message = "Gate successfully added",
                                actionLabel = "OK",
                                duration = SnackbarDuration.Short
                            )
                        } catch (e: IOException) {
                            snackbarHostState.showSnackbar(
                                message = "Error adding gate: ${e.message}",
                                actionLabel = "Retry",
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                    }
                },
                modifier = Modifier.padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))
            ) {
                Text(text = "Dodaj v bazo", color = Color.White)
            }
            Button(
                onClick = {
                    gates = null
                    scope.launch {
                        try {
                            val fetchedGates = withContext(Dispatchers.IO) {
                                fetchGates()
                            }
                            gates = fetchedGates ?: emptyList()

                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Failed to fetch gates: ${e.message}")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))
            ) {
                Text(text = "Podatki v bazi", color = Color.White)
            }
        }
        // Display the list of gates fetched from the database
        if (gates?.isEmpty() == false) {
            Text("Gates in database", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
            LazyColumn {
                items(gates!!) { gate ->
                    GateItem(gate)
                }
            }
        } else {
            Text("No gates available", modifier = Modifier.padding(16.dp))
        }
    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        CustomSnackbarHost(hostState = snackbarHostState)
    }
}

@Composable
fun GateItem(gate: Gate) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded },
        elevation = 4.dp
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Place, // Adjust icon as needed
                    contentDescription = "Gate Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                gate.label?.let {
                    Text(text = it)
                } ?: run {
                    Text(text = "ni podatka")
                }
            }

            // Detailed information displayed when 'expanded' is true
            if (expanded) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    Text(
                        text = "Podrobni podatki:",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Display gate id and label
                    Column(
                        modifier = Modifier.padding(start = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("- id: ${gate.id ?: "ni podatka"}", style = MaterialTheme.typography.body2)
                        Text("- label: ${gate.label}", style = MaterialTheme.typography.body2)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomSnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(hostState = hostState) { data ->
        // Custom snackbar layout
        Snackbar(
            snackbarData = data,
            backgroundColor = Color(0xFF323232),
            contentColor = Color.White,
            actionColor = Color.Yellow
        )
    }
}

@Composable
fun GenerateDataForm(tables: List<String>) {
    var selectedTable by remember { mutableStateOf(tables.first()) } // Default to the first table in the list
    var number by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) } // State to manage DropdownMenu visibility
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

    Column {
        Text("Table", fontWeight = FontWeight.Bold)
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text(selectedTable, modifier = Modifier.fillMaxWidth().clickable { expanded = true })
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                tables.forEach { table ->
                    DropdownMenuItem(onClick = {
                        selectedTable = table
                        expanded = false
                    }) {
                        Text(table)
                    }
                }
            }
        }

        Text("Number", fontWeight = FontWeight.Bold)
        TextField(
            value = number,
            onValueChange = { if (it.all { char -> char.isDigit() }) number = it }, // Allow only numeric input
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Button(
                onClick = {
                    scope.launch {
                        val count = number.toIntOrNull() ?: 0
                        if (count > 0) {
                            try {
                                for (i in 1..count) {
                                    when (selectedTable) {
                                        "locations" -> {
                                            val data = generateLocationData()
                                            val location = Location(
                                                id = generateRandomHexString(24),
                                                address = data["Address"] ?: "",
                                                city = data["City"] ?: "",
                                                postalCode = data["Postal Code"] ?: ""
                                            )
                                            addLocation(location)
                                        }
                                        "weatherconditions" -> {
                                            val data = generateWeatherInfoData()
                                            val weatherInfo = WeatherInfo(
                                                id = generateRandomHexString(24),
                                                location = data["Location"] ?: "",
                                                temperature = data["Temperature"]?.toIntOrNull() ?: 0,
                                                humidity = data["Humidity"]?.toIntOrNull() ?: 0,
                                                humidityStatus = data["Humidity Status"] ?: "",
                                                windSpeed = data["Wind Speed"]?.toIntOrNull() ?: 0,
                                                windStatus = data["Wind Direction"] ?: "",
                                                status = data["Status"] ?: ""
                                            )
                                            addWeatherInfo(weatherInfo)
                                        }
                                        "trafficinfos" -> {
                                            val data = generateTrafficInfoData()
                                            val trafficInfo = TrafficInfo(
                                                id = generateRandomHexString(24),
                                                time = Date(),
                                                location = listOf("6656ef9d83f28aa76711bac3", "6656efc783f28aa76711bac5", "6666380c3d42466311c12b38", "666638133d42466311c12b3a", "a7b11fd139e49cfbbfdafbff").random(),
                                                delay = listOf(10,2,13,30,53,20,0,19).random(),
                                                status = data["Status"] ?: ""
                                            )
                                            addTrafficInfo(trafficInfo)
                                        }
                                        "gates" -> {
                                            val data = generateGateData()
                                            val gate = Gate(
                                                id = generateRandomHexString(24),
                                                label = data["Label"] ?: ""
                                            )
                                            addGate(gate)
                                        }
                                        "airports" -> {
                                            val data = generateAirportData()
                                            val airport = Airport(
                                                id = generateRandomHexString(24),
                                                name = data["Name"] ?: "",
                                            )
                                            addAirport(airport)
                                        }
                                        "flights" -> {
                                            val data = generateFlightData()
                                            val arrivalPlannedData = dateFormat.parse(data["arrivalPlanned"])
                                            val arrivalExactData = dateFormat.parse(data["arrivalExact"])
                                            val flight = Flight(
                                                id = generateRandomHexString(24),
                                                arrivalPlanned = arrivalPlannedData,
                                                arrivalExact = arrivalExactData,
                                                destination = data["destination"] ?: "",
                                                gate = data["gate"] ?: "",
                                                airline = data["airline"] ?: "",
                                                flightNumber = data["flightNumber"] ?: "",
                                                airport = "6656ff7a99a43e1ec00e8357",
                                                status = "BREZ STATUSA",
                                                changeTime = calcTimeDiff(arrivalPlannedData?.hours.toString() + ":" + arrivalPlannedData?.minutes.toString(),arrivalExactData.hours.toString() + ":" + arrivalExactData.minutes.toString()),
                                                )
                                            addFlight(flight)
                                        }
                                    }
                                }
                                snackbarHostState.showSnackbar("Data added successfully for $selectedTable")
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar("Failed to add data: ${e.message}")
                            }
                        } else {
                            snackbarHostState.showSnackbar("Please enter a valid number")
                        }
                    }
                },
                modifier = Modifier.padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))
            ) {
                Text(text = "Add to Database", color = Color.White)
            }
        }
    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        SnackbarHost(hostState = snackbarHostState)
    }
}

@Composable
fun LocationForm() {
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var locations by remember { mutableStateOf<List<Location>?>(null) }

    Column {
        Text("Address", fontWeight = FontWeight.Bold)
        TextField(
            value = address,
            onValueChange = { address = it },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Text("City", fontWeight = FontWeight.Bold)
        TextField(
            value = city,
            onValueChange = { city = it },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Text("Postal Code", fontWeight = FontWeight.Bold)
        TextField(
            value = postalCode,
            onValueChange = { postalCode = it },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Button(
                onClick = {
                    val data = generateLocationData()
                    address = data["Address"] ?: ""
                    city = data["City"] ?: ""
                    postalCode = data["Postal Code"] ?: ""
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63)),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = "Generator", color = Color.White)
            }

            Button(
                onClick = {
                    scope.launch {
                        try {
                            addLocation(Location(generateRandomHexString(24), address, city, postalCode))
                            snackbarHostState.showSnackbar(
                                message = "Location successfully added",
                                actionLabel = "OK",
                                duration = SnackbarDuration.Short
                            )
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar(
                                message = "Error adding location: ${e.message}",
                                actionLabel = "Retry",
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63)),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = "Dodaj v bazo", color = Color.White)
            }
            Button(
                onClick = {
                    locations = null
                    scope.launch {
                        try {
                            val fetchedLocations = withContext(Dispatchers.IO) {
                                fetchLocations()
                            }
                            locations = fetchedLocations ?: emptyList()

                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Failed to fetch gates: ${e.message}")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))
            ) {
                Text(text = "Podatki v bazi", color = Color.White)
            }
        }
        // Display the list of locations fetched from the database
        if (locations?.isEmpty() == false) {
            Text("Gates in database", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
            LazyColumn {
                items(locations!!) { location ->
                    LocationItem(location)
                }
            }
        } else {
            Text("No locations available", modifier = Modifier.padding(16.dp))
        }
    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        SnackbarHost(hostState = snackbarHostState)
    }
}

@Composable
fun LocationItem(location: Location) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded },
        elevation = 4.dp
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Place, // Adjust icon as needed
                    contentDescription = "Location Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Lokacija: " + location.address, fontWeight = FontWeight.Bold)
            }

            // Detailed information displayed when 'expanded' is true
            if (expanded) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    Column(
                        modifier = Modifier.padding(start = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("- id: ${location.id}", style = MaterialTheme.typography.body2)
                        Text("- address: ${location.address ?: "ni podatka"}", style = MaterialTheme.typography.body2)
                        Text("- city: ${location.city ?: "ni podatka"}", style = MaterialTheme.typography.body2)
                        Text("- postalCode: ${location.postalCode ?: "ni podatka"}", style = MaterialTheme.typography.body2)
                    }
                }
            }
        }
    }
}

@Composable
fun TrafficInfoForm() {
    var location by remember { mutableStateOf<Location?>(null) }
    var delay by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val trafficInfoService: TrafficInfoService = TrafficInfoService()
    var trafficEvents by remember { mutableStateOf<List<TrafficInfo>?>(null) } // To store nullable TrafficInfo objects
    var isLoading by remember { mutableStateOf(false) } // State for loading animation
    var canInsertIntoDB by remember { mutableStateOf(false) }

    // Fetch locations asynchronously
    val locations by produceState(initialValue = emptyList<Location>()) {
        value = withContext(Dispatchers.IO) {
            fetchLocations() ?: emptyList()
        }
    }

    Column {
        Text("Location", fontWeight = FontWeight.Bold)
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Button(onClick = { expanded = true }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))) {
                Text(location?.address ?: "Select Location",  color = Color.White)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                locations.forEach { loc ->
                    DropdownMenuItem(onClick = {
                        location = loc
                        expanded = false
                    }) {
                        Text(text = loc.address!!)
                    }
                }
            }
        }

        Text("Delay", fontWeight = FontWeight.Bold)
        TextField(value = delay, onValueChange = { delay = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Text("Status", fontWeight = FontWeight.Bold)
        TextField(value = status, onValueChange = { status = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Button(onClick = {
                // Presumed function to generate mock data
                val data = generateTrafficInfoData()
                location = Location(id = generateRandomHexString(24), data["Location"])
                delay = data["Delay"]!!
                status = data["Status"]!!
            },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63)),
                modifier = Modifier.padding(end = 8.dp)) {
                Text("Generate", color = Color.White)
            }

            Button(onClick = {
                scope.launch {
                    if (location != null && delay.isNotBlank() && status.isNotBlank()) {
                        val delayInt = delay.toIntOrNull() ?: 0 // Safely convert string to int
                        val locationId = location!!.id.takeUnless { it.isBlank() } ?: generateRandomHexString(24)
                        val trafficInfo = TrafficInfo(
                            time = Date(),
                            location = locationId,
                            delay = delayInt,
                            status = status
                        )
                        try {
                            addTrafficInfo(trafficInfo)
                            delay = ""
                            status = ""
                            location = null
                            snackbarHostState.showSnackbar("Traffic info added successfully")
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Failed to add traffic info: ${e.message}")
                        }
                    } else {
                        snackbarHostState.showSnackbar("Please fill all fields correctly")
                    }
                }
            },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63)),
                modifier = Modifier.padding(end = 8.dp)) {
                Text("Dodaj v bazo", color = Color.White)
            }

            // Scrapper button restored
            Button(onClick = {
                trafficEvents = null
                isLoading = true // Start loading animation
                scope.launch {
                    trafficEvents = withContext(Dispatchers.IO) {
                        trafficInfoService.fetchTrafficEvents() // Fetch and store new traffic events
                    }

                    if(trafficEvents?.isEmpty() == false){
                        canInsertIntoDB = true
                    }else {
                        canInsertIntoDB = false
                    }

                    isLoading = false // Stop loading animation
                }
            },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63)),
                modifier = Modifier.padding(end = 8.dp)) {
                Text("Scrapper", color = Color.White)
            }

            // New button for sending all to database
            Button(
                onClick = {
                    scope.launch {
                        try {
                            if(trafficEvents?.isNullOrEmpty() == true){
                                snackbarHostState.showSnackbar("No traffic events to add")
                            }else{
                                trafficEvents?.forEach { trafficEv ->
                                    addTrafficInfo(trafficEv)
                                }
                                trafficEvents = null
                                canInsertIntoDB = false
                                snackbarHostState.showSnackbar("All traffic events added successfully")
                            }
                        } catch (e: Exception) {
                            trafficEvents = null
                            canInsertIntoDB = false
                            snackbarHostState.showSnackbar("Failed to add traffic events: ${e.message}")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63)),
                modifier = Modifier.padding(end = 8.dp),
                enabled = canInsertIntoDB
            ) {
                Text(text = "Pošlji vse v bazo", color = Color.White)
            }
            Button(
                onClick = {
                    trafficEvents = null
                    canInsertIntoDB = false
                    scope.launch {
                        try {
                            val fetchedTrafficInfo = withContext(Dispatchers.IO) {
                                fetchTrafficInfo()
                            }
                            trafficEvents = fetchedTrafficInfo ?: emptyList()

                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Failed to fetch flights: ${e.message}")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))
            ) {
                Text(text = "Podatki v bazi", color = Color.White)
            }
        }

        // Show loading indicator if loading
        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Pridobivanje podatkov...")
                }
            }
        }

        trafficEvents?.let { trafficList ->
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(trafficList) { event ->
                    TrafficEventItem(event)
                }
            }
        } ?: Text("No traffic info available", modifier = Modifier.padding(16.dp))
    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        SnackbarHost(hostState = snackbarHostState)
    }
}



@Composable
fun TrafficEventItem(event: TrafficInfo) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded },
        elevation = 4.dp
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info, // Example icon
                    contentDescription = "Location Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Traffic at ${event.location}")
            }

            // Detailed information displayed when 'expanded' is true
            if (expanded) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    Text("Location: ${event.location}", style = MaterialTheme.typography.subtitle1)
                    Text("Delay: ${event.delay}", style = MaterialTheme.typography.body1)
                    Text("Status: ${event.status}", style = MaterialTheme.typography.body2)
                }
            }
        }
    }
}

@Composable
fun WeatherInfoForm() {
    var location by remember { mutableStateOf<Location?>(null) }
    var temperature by remember { mutableStateOf("") }
    var humidity by remember { mutableStateOf("") }
    var humidityStatus by remember { mutableStateOf("") }
    var windSpeed by remember { mutableStateOf("") }
    var windDirection by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var weatherEvents by remember { mutableStateOf<List<WeatherInfo>?>(null) }
    val weatherInfoService: WeatherService = WeatherService() // Assuming this is your service instance
    var locations by remember { mutableStateOf(listOf<String>()) }
    val trafficInfoService: TrafficInfoService = TrafficInfoService() // Assuming this is your service instance
    var isLoading by remember { mutableStateOf(false) } // State for loading animation
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var canInsertIntoDB by remember { mutableStateOf(false) }

    // Fetch locations asynchronously
    val locationsDropDown by produceState(initialValue = emptyList<Location>()) {
        value = withContext(Dispatchers.IO) {
            fetchLocations() ?: emptyList()
        }
    }

    Column {
        Text("Location", fontWeight = FontWeight.Bold)
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Button(onClick = { expanded = true }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))) {
                Text(location?.address ?: "Select Location", color = Color.White)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                locationsDropDown.forEach { loc ->
                    DropdownMenuItem(onClick = {
                        location = loc
                        expanded = false
                    }) {
                        Text(text = loc.address!!)
                    }
                }
            }
        }

        Text("Temperature", fontWeight = FontWeight.Bold)
        TextField(value = temperature, onValueChange = { temperature = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Text("Humidity", fontWeight = FontWeight.Bold)
        TextField(value = humidity, onValueChange = { humidity = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Text("Humidity Status", fontWeight = FontWeight.Bold)
        TextField(value = humidityStatus, onValueChange = { humidityStatus = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Text("Wind Speed", fontWeight = FontWeight.Bold)
        TextField(value = windSpeed, onValueChange = { windSpeed = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Text("Wind Direction", fontWeight = FontWeight.Bold)
        TextField(value = windDirection, onValueChange = { windDirection = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Text("Status", fontWeight = FontWeight.Bold)
        TextField(value = status, onValueChange = { status = it }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Button(
                onClick = {
                    val data = generateWeatherInfoData()
                    temperature = data["Temperature"]!!
                    humidity = data["Humidity"]!!
                    humidityStatus = data["Humidity Status"]!!
                    windSpeed = data["Wind Speed"]!!
                    windDirection = data["Wind Direction"]!!
                    status = data["Status"]!!
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63)),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = "Generator", color = Color.White)
            }

            Button(
                onClick = {
                    scope.launch {
                        if (location != null && temperature.isNotBlank() && humidity.isNotBlank() && humidityStatus.isNotBlank() && windSpeed.isNotBlank() && windDirection.isNotBlank() && status.isNotBlank()) {
                            val temperatureInt = temperature.toIntOrNull() ?: 0 // Safely convert string to int
                            val humidityInt = humidity.toIntOrNull() ?: 0 // Safely convert string to int
                            val windSpeedInt = windSpeed.toIntOrNull() ?: 0 // Safely convert string to int
                            val weatherInfo = WeatherInfo(
                                id = generateRandomHexString(24),
                                location = location!!.id,
                                temperature = temperatureInt,
                                humidity = humidityInt,
                                humidityStatus = humidityStatus,
                                windSpeed = windSpeedInt,
                                windStatus = windDirection,
                                status = status
                            )
                            try {
                                addWeatherInfo(weatherInfo)
                                temperature = ""
                                humidity = ""
                                humidityStatus = ""
                                windSpeed = ""
                                windDirection = ""
                                status = ""
                                location = null
                                snackbarHostState.showSnackbar("Weather info added successfully")
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar("Failed to add weather info: ${e.message}")
                            }
                        } else {
                            snackbarHostState.showSnackbar("Please fill all fields correctly")
                        }
                    }
                },
                modifier = Modifier.padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))
            ) {
                Text(text = "Dodaj v bazo", color = Color.White)
            }

            // Scrapper button
            Button(
                onClick = {
                    weatherEvents = null
                    isLoading = true // Start loading animation
                    scope.launch {
                        weatherEvents = withContext(Dispatchers.IO) {
                            weatherInfoService.fetchWeathervents() // Fetch and store new weather events
                        }
                        isLoading = false // Stop loading animation
                        if (weatherEvents?.isEmpty() == false) {
                            canInsertIntoDB = true
                        } else {
                            canInsertIntoDB = false
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63)),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = "Scrapper", color = Color.White)
            }

            // New button for sending all to database
            Button(
                onClick = {
                    scope.launch {
                        try {
                            if (weatherEvents?.isNullOrEmpty() == true) {
                                snackbarHostState.showSnackbar("No weather events to add")
                            } else {
                                weatherEvents?.forEach { weatherEv ->
                                    addWeatherInfo(weatherEv)
                                }
                                weatherEvents = null
                                canInsertIntoDB = false
                                snackbarHostState.showSnackbar("All weather events added successfully")
                            }
                        } catch (e: Exception) {
                            weatherEvents = null
                            canInsertIntoDB = false
                            snackbarHostState.showSnackbar("Failed to add weather events: ${e.message}")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63)),
                modifier = Modifier.padding(end = 8.dp),
                enabled = canInsertIntoDB
            ) {
                Text(text = "Pošlji vse v bazo", color = Color.White)
            }

            Button(
                onClick = {
                    weatherEvents = null
                    canInsertIntoDB = false
                    scope.launch {
                        try {
                            val fetchedWeatherInfo = withContext(Dispatchers.IO) {
                                fetchWeatherInfo()
                            }
                            weatherEvents = fetchedWeatherInfo ?: emptyList()
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Failed to fetch weather info: ${e.message}")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFD9B63))
            ) {
                Text(text = "Podatki v bazi", color = Color.White)
            }
        }

        // Show loading indicator if loading
        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Pridobivanje podatkov...")
                }
            }
        }

        // Display the weather events
        weatherEvents?.let { weatherList ->
            LazyColumn {
                items(weatherList) { weatherInfo ->
                    WeatherEventItem(weatherInfo)
                }
            }
        } ?: Text("No weather info available", modifier = Modifier.padding(16.dp))
    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        SnackbarHost(hostState = snackbarHostState)
    }
}

@Composable
fun WeatherEventItem(weatherInfo: WeatherInfo) {
    var expanded by remember { mutableStateOf(false) }  // State to manage the expansion

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded },  // Make the card clickable to toggle expansion
        elevation = 4.dp
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info, // Assuming you're using a general info icon
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Weather in ${weatherInfo.location}")
            }

            // Detailed information displayed when 'expanded' is true
            if (expanded) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    Text("Location: ${weatherInfo.location}", style = MaterialTheme.typography.subtitle1)
                    Text("Temperature: ${weatherInfo.temperature}", style = MaterialTheme.typography.body1)
                    Text("Humidity: ${weatherInfo.humidity}", style = MaterialTheme.typography.body2)
                    Text("Wind Speed: ${weatherInfo.windSpeed}", style = MaterialTheme.typography.body2)
                    Text("Wind Direction: ${weatherInfo.windStatus}", style = MaterialTheme.typography.body2)
                    Text("Status: ${weatherInfo.status}", style = MaterialTheme.typography.body2)
                }
            }
        }
    }
}

@Composable
fun InfoDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Info", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("O aplikaciji")
            }
        },
        text = {
            Column {
                Text("Aplikacija AirInsight (verzija 1.0) omogoča upravljanje in pregledovanje podatkov v različnih tabelah:")
                Spacer(modifier = Modifier.height(8.dp))
                Text("- Letališča, Leti, Vrata, Lokacije, prometne informacije, vremenski pogoji\n\nOmogoča pa tudi generiranje testnih podatkov in web scraping.")
                Spacer(modifier = Modifier.height(16.dp))
                Text("Funkcionalnosti aplikacije:")
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Scrapper", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Scrapper: Izluščimo potrebne podatke iz spletne strani in jih prikažemo")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Dodaj v bazo", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Dodaj v bazo: Ročno dodajanje podatkov v bazo")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Pošlji v bazo", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pošlji vse v bazo: Pošlje vse pridobljene podatke iz Scrapper-ja v bazo")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Build, contentDescription = "Generator", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generator: Generiraj naključne podatke za testiranje")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onDismissRequest() },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF81A263))
            ) {
                Text("Zapri")
            }
        },
        backgroundColor = Color(0xFFFD9B63), // Deep purple background for dialog
        contentColor = Color.White // White text in dialog
    )
}

@Composable
fun DropdownMenuExample(locations: List<String>, selectedLocation: String, onLocationSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded }) {
        TextField(
            value = selectedLocation,
            onValueChange = { /* ReadOnly */ },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown"
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            locations.forEach { location ->
                DropdownMenuItem(onClick = {
                    onLocationSelected(location)
                    expanded = false
                }) {
                    Text(text = location)
                }
            }
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "airInsight v1.0",
        state = androidx.compose.ui.window.WindowState(placement = androidx.compose.ui.window.WindowPlacement.Fullscreen)
    ) {
        MyApp(onCloseRequest = ::exitApplication)
    }
}
