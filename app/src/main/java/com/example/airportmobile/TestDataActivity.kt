package com.example.airportmobile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.airportmobile.database.AppDatabase
import com.example.airportmobile.database.TestDataAdapter
import com.example.airportmobile.database.TestDataEntity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.Executors

class TestDataActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var generateDataButton: Button
    private lateinit var sendDataButton: Button
    private lateinit var testData: MutableList<TestDataEntity>
    private lateinit var adapter: TestDataAdapter
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_data)

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }

        // Inicializacija Room baze
        database = AppDatabase.getInstance(applicationContext)

        recyclerView = findViewById(R.id.recyclerViewTestData)
        generateDataButton = findViewById(R.id.buttonGenerateData)
        sendDataButton = findViewById(R.id.buttonSendData)

        testData = mutableListOf()
        adapter = TestDataAdapter(testData)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        generateDataButton.setOnClickListener {
            generateTestData()
        }

        sendDataButton.setOnClickListener {
            sendTestDataToRoom()
        }
    }

    private fun generateTestData() {
        val locations = listOf("Ljubljana", "Zagreb", "Bratislava", "Dunaj")
        val locationIds = listOf(
            "6656ef9d83f28aa76711bac3",
            "6666380c3d42466311c12b38",
            "3e98a06d5b712c31e089d703",
            "57503365c6efe496958d246d"
        )

        testData.clear()

        // Generiramo 10 primerov podatkov
        for (i in 1..10) {
            val index = (locations.indices).random()
            val testDataEntity = TestDataEntity(
                locationId = locationIds[index],
                locationName = locations[index],
                crowdNumber = (0..1000).random(),
                timestamp = System.currentTimeMillis().toString()
            )
            testData.add(testDataEntity)
        }

        adapter.notifyDataSetChanged()
        Toast.makeText(this, "Test data generated!", Toast.LENGTH_SHORT).show()
    }

    private fun sendTestDataToRoom() {
        val firestore = FirebaseFirestore.getInstance()

        Executors.newSingleThreadExecutor().execute {
            try {
                // Save to Room database
                database.testDataDao().insertAll(*testData.toTypedArray())

                // Save to Firebase Firestore
                for (data in testData) {
                    firestore.collection("test_data")
                        .add(data)
                        .addOnSuccessListener {
                            Log.d("Firebase", "Data sent to Firebase: $data")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firebase", "Failed to send data to Firebase: ${e.message}", e)
                        }
                }

                runOnUiThread {
                    Toast.makeText(
                        this@TestDataActivity,
                        "Data sent to Room database and Firebase successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@TestDataActivity,
                        "Failed to send data: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}