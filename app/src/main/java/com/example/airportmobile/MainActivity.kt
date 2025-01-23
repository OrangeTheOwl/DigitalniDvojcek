package com.example.airportmobile


import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.airportmobile.database.AirportMarkerEntity
import com.example.airportmobile.database.AppDatabase
import com.example.airportmobile.database.TestDataEntity
import com.example.airportmobile.database.TrafficMarkerEntity
import com.example.airportmobile.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Inicializiraj interno bazo
        database = AppDatabase.getInstance(applicationContext)

        // Inicializiraj podatke
        Thread {
            val testDao = database.testDataDao()
            testDao.deleteAll()
            val existingData = testDao.getAll()
            populateInitialData()

            if (existingData.isEmpty()) {
                val testDataList = listOf(
                    TestDataEntity(
                        locationId = "6656ef9d83f28aa76711bac3",
                        locationName = "Ljubljana",
                        crowdNumber = 500,
                        timestamp = "2025-01-20T12:00:00Z"
                    ),
                    TestDataEntity(
                        locationId = "6666380c3d42466311c12b38",
                        locationName = "Zagreb",
                        crowdNumber = 300,
                        timestamp = "2025-01-20T12:00:00Z"
                    ),
                    TestDataEntity(
                        locationId = "3e98a06d5b712c31e089d703",
                        locationName = "Bratislava",
                        crowdNumber = 260,
                        timestamp = "2025-01-20T12:00:00Z"
                    ),
                    TestDataEntity(
                        locationId = "57503365c6efe496958d246d",
                        locationName = "Dunaj",
                        crowdNumber = 980,
                        timestamp = "2025-01-20T12:00:00Z"
                    )
                )
                testDao.insertAll(*testDataList.toTypedArray())
            }
        }.start()

        binding.buttonGoToPhoto.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    AddPhotoActivity::class.java
                )
            )
        }

        binding.buttonGoToAllData.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    AllDataActivity::class.java
                )
            )
        }

        binding.buttonGoToMap.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    MapsActivity::class.java
                )
            )
        }

        binding.buttonGoToTestData.setOnClickListener {
            val intent = Intent(this, TestDataActivity::class.java)
            startActivity(intent)
        }

        binding.buttonGoToSensors.setOnClickListener {
            startActivity(Intent(this, SensorsActivity::class.java))
        }
    }

    private fun populateInitialData() {
        Thread {
            val airportDao = database.airportMarkerDao()
            val trafficDao = database.trafficMarkerDao()

            if (airportDao.getAll().isEmpty()) {
                val airports = listOf(
                    AirportMarkerEntity(latitude = 46.2231, longitude = 14.4576, name = "Letališče Jožeta Pučnika (Ljubljana)"),
                    AirportMarkerEntity(latitude = 45.8919, longitude = 15.5215, name = "Letališče Zagreb (Hrvaška)"),
                    AirportMarkerEntity(latitude = 48.1103, longitude = 16.5697, name = "Letališče Dunaj (Avstrija)"),
                    AirportMarkerEntity(latitude = 47.4333, longitude = 19.2611, name = "Letališče Budimpešta (Madžarska)")
                )
                airportDao.insertAll(*airports.toTypedArray())
            }

            if (trafficDao.getAll().isEmpty()) {
                val trafficMarkers = listOf(
                    TrafficMarkerEntity(location = "A1, Ljubljana - vzhodna obvoznica", status = "zastoj"),
                    TrafficMarkerEntity(location = "R3-702, Dravograd - Trbonje", status = "dela"),
                    TrafficMarkerEntity(location = "H3, Ljubljana - severna obvoznica", status = "dela"),
                    TrafficMarkerEntity(location = "A1, Koper - Ljubljana", status = "zastoj"),
                    TrafficMarkerEntity(location = "A2, Ljubljana - Obrežje", status = "območje zastojev")
                )
                trafficDao.insertAll(*trafficMarkers.toTypedArray())
            }
        }.start()
    }
}

