/*package com.example.airportmobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject

class TestDataActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var generateDataButton: Button
    private lateinit var sendDataButton: Button
    private lateinit var testData: MutableList<JSONObject>
    private lateinit var adapter: TestDataAdapter

    // Firebase Database Reference
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_data)

        // Inicializirajte Firebase
        FirebaseApp.initializeApp(this)

        // Pridobite referenco na Firebase Realtime Database
        database = FirebaseDatabase.getInstance().getReference("testData")

        // Inicializacija RecyclerView in ostalih komponent
        recyclerView = findViewById(R.id.recyclerViewTestData)
        generateDataButton = findViewById(R.id.buttonGenerateData)

        // Inicializirajte podatke in adapter
        testData = mutableListOf()
        adapter = TestDataAdapter(testData)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Dogodek za generiranje testnih podatkov
        generateDataButton.setOnClickListener {
            generateTestData()
        }
    }


    private fun generateTestData() {
        // Predefined locations
        val locations = listOf("Ljubljana", "Zagreb", "Bratislava", "Dunaj")

        // Clear existing data
        testData.clear()

        // Generate 10 random data entries
        for (i in 1..10) {
            val jsonObject = JSONObject().apply {
                put("locationName", locations.random()) // Random location
                put("crowdNumber", (0..1000).random()) // Random crowd number
                put("timestamp", System.currentTimeMillis()) // Current timestamp
            }
            testData.add(jsonObject)
        }

        // Notify adapter about data changes
        adapter.notifyDataSetChanged()
        Toast.makeText(this, "Test data generated!", Toast.LENGTH_SHORT).show()
    }

    private fun sendTestDataToFirebase() {
        for (item in testData) {
            val firebaseEntry = mapOf(
                "locationName" to item.optString("locationName"),
                "crowdNumber" to item.optInt("crowdNumber"),
                "timestamp" to item.optLong("timestamp")
            )

            // Push each item to Firebase under a unique key
            database.push().setValue(firebaseEntry)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data sent to Firebase successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to send data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}

class TestDataAdapter(private val data: List<JSONObject>) : RecyclerView.Adapter<TestDataAdapter.TestDataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return TestDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: TestDataViewHolder, position: Int) {
        val item = data[position]
        holder.location.text = "Location: ${item.optString("locationName")}" // Use optString to prevent crashes
        holder.crowdNumber.text = "Crowd: ${item.optInt("crowdNumber")}" // Use optInt for safety
        holder.timestamp.text = "Timestamp: ${item.optLong("timestamp")}" // Use optLong for safety
    }

    override fun getItemCount(): Int = data.size

    class TestDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val location: TextView = itemView.findViewById(R.id.textViewLocation)
        val crowdNumber: TextView = itemView.findViewById(R.id.textViewCrowdNumber)
        val timestamp: TextView = itemView.findViewById(R.id.textViewTimestamp)
    }
}
 */

package com.example.airportmobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class TestDataActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var generateDataButton: Button
    private lateinit var testData: MutableList<JSONObject>
    private lateinit var adapter: TestDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_data)

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewTestData)
        generateDataButton = findViewById(R.id.buttonGenerateData)

        // Initialize RecyclerView and Adapter
        testData = mutableListOf()
        adapter = TestDataAdapter(testData)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Set button click listener
        generateDataButton.setOnClickListener {
            generateTestData()
        }
    }

    private fun generateTestData() {
        // Predefined locations
        val locations = listOf("Ljubljana", "Zagreb", "Bratislava", "Dunaj")

        // Clear existing data
        testData.clear()

        // Generate 10 random data entries
        for (i in 1..10) {
            val jsonObject = JSONObject().apply {
                put("locationName", locations.random()) // Random location
                put("crowdNumber", (0..1000).random()) // Random crowd number
                put("timestamp", System.currentTimeMillis()) // Current timestamp
            }
            testData.add(jsonObject)
        }

        // Notify adapter about data changes
        adapter.notifyDataSetChanged()
        Toast.makeText(this, "Test data generated!", Toast.LENGTH_SHORT).show()
    }
}

class TestDataAdapter(private val data: List<JSONObject>) : RecyclerView.Adapter<TestDataAdapter.TestDataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return TestDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: TestDataViewHolder, position: Int) {
        val item = data[position]
        holder.location.text = "Location: ${item.optString("locationName")}" // Use optString to prevent crashes
        holder.crowdNumber.text = "Crowd: ${item.optInt("crowdNumber")}" // Use optInt for safety
        holder.timestamp.text = "Timestamp: ${item.optLong("timestamp")}" // Use optLong for safety
    }

    override fun getItemCount(): Int = data.size

    class TestDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val location: TextView = itemView.findViewById(R.id.textViewLocation)
        val crowdNumber: TextView = itemView.findViewById(R.id.textViewCrowdNumber)
        val timestamp: TextView = itemView.findViewById(R.id.textViewTimestamp)
    }
}
