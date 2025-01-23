/*package com.example.airportmobile

import MongoDBConnection
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.airportmobile.databinding.ActivityAlldataBinding
import kotlinx.coroutines.runBlocking
import org.bson.Document

class AllDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlldataBinding
    private val mongoDBConnection = MongoDBConnection()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAlldataBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Zaženi asinhrono pridobivanje podatkov z runBlocking
        fetchDataFromDatabase()
    }

    private fun fetchDataFromDatabase() {
        runBlocking {
            try {
                // Pokliči funkcijo za pridobitev vseh podatkov
                val data = mongoDBConnection.getAllData()
                if (data.isNotEmpty()) {
                    // Posodobimo UI na podlagi pridobljenih podatkov
                    updateUI(data)
                } else {
                    Log.d("AllDataActivity", "DATABASE data: !!! No data found in collection.!!!")
                }
            } catch (e: Exception) {
                Log.e("AllDataActivity", "Napaka pri pridobivanju podatkov iz MongoDB", e)
                // Nastavi privzete vrednosti za ProgressBar ob napaki
                binding.progressBarLjubljana.progress = 0
                binding.progressBarZagreb.progress = 0
                binding.progressBarBratislava.progress = 0
                binding.progressBarDunaj.progress = 0
            }
        }
    }

    private fun updateUI(data: List<Document>) {
        try {
            // Izpiši vse pridobljene podatke v Logcat
            data.forEach { document ->
                Log.d("AllDataActivity", "Document: $document")
            }

            // Pridobimo podatke iz dokumentov za vsako mesto na podlagi locationId
            val ljubljanaData = data.find { it.getString("locationId") == "6656ef9d83f28aa76711bac3" }?.getString("crowdNumber")?.toIntOrNull() ?: 0
            val zagrebData = data.find { it.getString("locationId") == "6666380c3d42466311c12b38" }?.getString("crowdNumber")?.toIntOrNull() ?: 0
            val bratislavaData = data.find { it.getString("locationId") == "3e98a06d5b712c31e089d703" }?.getString("crowdNumber")?.toIntOrNull() ?: 0
            val dunajData = data.find { it.getString("locationId") == "57503365c6efe496958d246d" }?.getString("crowdNumber")?.toIntOrNull() ?: 0

            // Posodobimo ProgressBar za vsako mesto
            binding.progressBarLjubljana.progress = ljubljanaData
            binding.progressBarZagreb.progress = zagrebData
            binding.progressBarBratislava.progress = bratislavaData
            binding.progressBarDunaj.progress = dunajData
        } catch (e: Exception) {
            Log.e("AllDataActivity", "Napaka pri posodabljanju UI-ja", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Zapri povezavo z MongoDB, ko aktivnost preneha delovati
        mongoDBConnection.closeConnection()
    }
}*/
/*
package com.example.airportmobile

import MongoDBConnection
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.airportmobile.databinding.ActivityAlldataBinding
import kotlinx.coroutines.runBlocking
import org.bson.Document

class AllDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlldataBinding
    private val mongoDBConnection = MongoDBConnection() // Uporaba Kotlin razreda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAlldataBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Zaženi pridobivanje podatkov z runBlocking
        fetchDataFromDatabase()
    }

    private fun fetchDataFromDatabase() {
        runBlocking {
            try {
                // Pridobimo vse podatke iz MongoDB z uporabo razreda MongoDBConnection
                val data = mongoDBConnection.getAllData()
                if (data.isNotEmpty()) {
                    // Posodobimo UI na podlagi pridobljenih podatkov
                    updateUI(data)
                } else {
                    Log.d("AllDataActivity", "DATABASE data: !!! No data found in collection.!!!")
                    resetUI()
                }
            } catch (e: Exception) {
                Log.e("AllDataActivity", "Napaka pri pridobivanju podatkov iz MongoDB", e)
                resetUI()
            }
        }
    }

    private fun updateUI(data: List<Document>) {
        try {
            // Izpiši vse pridobljene podatke v Logcat
            data.forEach { document ->
                Log.d("AllDataActivity", "Document: $document")
            }

            // Pridobimo podatke iz dokumentov za vsako mesto na podlagi locationId
            val ljubljanaData = data.find { it.getString("locationId") == "6656ef9d83f28aa76711bac3" }?.getString("crowdNumber")?.toIntOrNull() ?: 0
            val zagrebData = data.find { it.getString("locationId") == "6666380c3d42466311c12b38" }?.getString("crowdNumber")?.toIntOrNull() ?: 0
            val bratislavaData = data.find { it.getString("locationId") == "3e98a06d5b712c31e089d703" }?.getString("crowdNumber")?.toIntOrNull() ?: 0
            val dunajData = data.find { it.getString("locationId") == "57503365c6efe496958d246d" }?.getString("crowdNumber")?.toIntOrNull() ?: 0

            // Posodobimo ProgressBar za vsako mesto
            binding.progressBarLjubljana.progress = ljubljanaData
            binding.progressBarZagreb.progress = zagrebData
            binding.progressBarBratislava.progress = bratislavaData
            binding.progressBarDunaj.progress = dunajData
        } catch (e: Exception) {
            Log.e("AllDataActivity", "Napaka pri posodabljanju UI-ja", e)
        }
    }

    private fun resetUI() {
        binding.progressBarLjubljana.progress = 0
        binding.progressBarZagreb.progress = 0
        binding.progressBarBratislava.progress = 0
        binding.progressBarDunaj.progress = 0
    }

    override fun onDestroy() {
        super.onDestroy()
        // Zapri povezavo z MongoDB, ko aktivnost preneha delovati
        mongoDBConnection.closeConnection()
    }
}
 */
package com.example.airportmobile

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.airportmobile.database.AppDatabase
import com.example.airportmobile.database.TestDataEntity
import com.example.airportmobile.databinding.ActivityAlldataBinding
import kotlinx.coroutines.launch
import java.time.Instant

class AllDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlldataBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAlldataBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        database = AppDatabase.getInstance(applicationContext)

        fetchDataFromDatabase()
    }

    private fun fetchDataFromDatabase() {
        Thread {
            try {
                val data = database.testDataDao().getAll()
                if (data.isNotEmpty()) {
                    runOnUiThread {
                        updateUI(data)
                    }
                } else {
                    Log.d("AllDataActivity", "No data found in the database.")
                    runOnUiThread {
                        resetUI()
                    }
                }
            } catch (e: Exception) {
                Log.e("AllDataActivity", "Napaka pri pridobivanju podatkov iz baze", e)
                runOnUiThread {
                    resetUI()
                }
            }
        }.start()
    }

    private fun updateUI(data: List<TestDataEntity>) {
        try {
            data.forEach { entity ->
                Log.d("AllDataActivity", "Entity: $entity")
            }

            // Funkcija za izračun povprečja in pridobitev zadnjega zapisa
            fun calculateStats(locationId: String): Pair<Int, Int> {
                val filteredData = data.filter { it.locationId == locationId }
                if (filteredData.isEmpty()) return Pair(0, 0)

                val average = filteredData.map { it.crowdNumber }.average().toInt()

                val latest = filteredData.maxByOrNull {
                    try {
                        Instant.parse(it.timestamp).toEpochMilli()
                    } catch (e: Exception) {
                        it.timestamp.toLongOrNull() ?: 0L
                    }
                }?.crowdNumber ?: 0

                return Pair(average, latest)
            }

            // Izračunaj in posodobi podatke za posamezna letališča
            val ljubljanaStats = calculateStats("6656ef9d83f28aa76711bac3")
            val zagrebStats = calculateStats("6666380c3d42466311c12b38")
            val bratislavaStats = calculateStats("3e98a06d5b712c31e089d703")
            val dunajStats = calculateStats("57503365c6efe496958d246d")

            // Posodobi ProgressBar z animacijo in zadnje znano število ljudi
            animateProgressBar(binding.progressBarLjubljana, ljubljanaStats.first)
            binding.textView14.text = ljubljanaStats.second.toString()

            animateProgressBar(binding.progressBarZagreb, zagrebStats.first)
            binding.textView12.text = zagrebStats.second.toString()

            animateProgressBar(binding.progressBarBratislava, bratislavaStats.first)
            binding.textView10.text = bratislavaStats.second.toString()

            animateProgressBar(binding.progressBarDunaj, dunajStats.first)
            binding.textView16.text = dunajStats.second.toString()

        } catch (e: Exception) {
            Log.e("AllDataActivity", "Napaka pri posodabljanju UI-ja", e)
        }
    }

    private fun resetUI() {
        animateProgressBar(binding.progressBarLjubljana, 0)
        animateProgressBar(binding.progressBarZagreb, 0)
        animateProgressBar(binding.progressBarBratislava, 0)
        animateProgressBar(binding.progressBarDunaj, 0)

        binding.textView14.text = "0"
        binding.textView12.text = "0"
        binding.textView10.text = "0"
        binding.textView16.text = "0"
    }

    private fun animateProgressBar(progressBar: ProgressBar, targetValue: Int) {
        ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, targetValue).apply {
            duration = 800
            start()
        }
    }
}





