package com.example.airportmobile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.airportmobile.database.AppDatabase
import com.example.airportmobile.database.ExtremeEventEntity
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log10

class SensorsActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var accelerometerSensor: Sensor? = null
    private var mediaRecorder: MediaRecorder? = null
    private lateinit var lightTextView: TextView
    private lateinit var accelerometerTextView: TextView
    private lateinit var microphoneTextView: TextView

    private val audioPermissionRequestCode = 101

    private var lastAccelerometerValues = FloatArray(3)
    private val accelerometerThreshold = 0.2f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensors)

        lightTextView = findViewById(R.id.textViewLight)
        accelerometerTextView = findViewById(R.id.textViewAccelerometer)
        microphoneTextView = findViewById(R.id.textViewMicrophone)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        requestAudioPermission()
    }

    override fun onResume() {
        super.onResume()
        lightSensor?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        accelerometerSensor?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        if (hasAudioPermission()) {
            setupMicrophone()
            startMicrophone()
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        stopMicrophone()
    }

    private var isTurbulenceDetected = false
    private var turbulenceStartTime: Long = 0

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            Sensor.TYPE_LIGHT -> {
                val lightLevel = event.values[0]
                lightTextView.text = "Light Level: $lightLevel lx"
            }
            Sensor.TYPE_ACCELEROMETER -> {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val deltaX = Math.abs(lastAccelerometerValues[0] - x)
                val deltaY = Math.abs(lastAccelerometerValues[1] - y)
                val deltaZ = Math.abs(lastAccelerometerValues[2] - z)

                if (deltaX > accelerometerThreshold || deltaY > accelerometerThreshold || deltaZ > accelerometerThreshold) {
                    accelerometerTextView.text = "Accelerometer: x=$x, y=$y, z=$z"
                    lastAccelerometerValues[0] = x
                    lastAccelerometerValues[1] = y
                    lastAccelerometerValues[2] = z

                    // Logika za turbulence
                    if (!isTurbulenceDetected) {
                        isTurbulenceDetected = true
                        turbulenceStartTime = System.currentTimeMillis()
                    } else {
                        val currentTime = System.currentTimeMillis()
                        // Opozorilo za turbolenco
                        if (currentTime - turbulenceStartTime >= 3000) {
                            Toast.makeText(this, "Zaznana turbulenca! Prosim, ostanite varni.", Toast.LENGTH_LONG).show()
                            turbulenceStartTime = currentTime
                            saveExtremeEvent("Turbolenca")
                        }
                    }
                } else {
                    // Ponastavi, če ni več turbulence
                    isTurbulenceDetected = false
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // No implementation needed for this example
    }


    private fun saveExtremeEvent(status: String) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    val geocoder = Geocoder(this)
                    val addressList = geocoder.getFromLocation(latitude, longitude, 1)
                    val locationName = if (!addressList.isNullOrEmpty()) {
                        addressList[0].getAddressLine(0)
                    } else {
                        "Unknown Location"
                    }

                    val event = ExtremeEventEntity(
                        location = locationName,
                        status = status,
                        timestamp = System.currentTimeMillis()
                    )

                    Thread {
                        val database = AppDatabase.getInstance(applicationContext)
                        database.extremeEventDao().insert(event)
                        Log.d("SensorsActivity", "Extreme event saved: $event")

                        saveExtremeEventToFirebase(event)
                    }.start()
                } else {
                    Toast.makeText(this, "Unable to get location for the event.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Location permission not granted.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveExtremeEventToFirebase(event: ExtremeEventEntity) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("extreme_events")
            .add(event)
            .addOnSuccessListener {
                Log.d("Firebase", "Extreme event sent to Firebase: $event")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Failed to send extreme event to Firebase: ${e.message}", e)
            }
    }



    private fun requestAudioPermission() {
        if (!hasAudioPermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                audioPermissionRequestCode
            )
        }
    }

    private fun hasAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupMicrophone() {
        try {
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(createTempFile("temp_audio", ".3gp", cacheDir).absolutePath)
            }
        } catch (e: Exception) {
            Log.e("SensorsActivity", "Error setting up microphone", e)
            Toast.makeText(this@SensorsActivity, "Error setting up microphone", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startMicrophone() {
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            updateMicrophone()
        } catch (e: Exception) {
            Log.e("SensorsActivity", "Error starting microphone", e)
            Toast.makeText(this, "Error accessing microphone", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopMicrophone() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
        } catch (e: Exception) {
            Log.e("SensorsActivity", "Error stopping microphone", e)
        }
    }

    private fun updateMicrophone() {
        try {
            val amplitude = mediaRecorder?.maxAmplitude ?: 0
            val decibels = if (amplitude > 0) 20 * log10(amplitude.toDouble()) else 0.0
            microphoneTextView.text = "Microphone Level: %.2f dB".format(decibels)

            // Opozorilo pri visoki ravni hrupa (npr. nad 85 dB)
            if (decibels > 85) {
                Toast.makeText(this, "Visoka raven hrupa zaznana! Poiščite tišjo lokacijo.", Toast.LENGTH_LONG).show()
                saveExtremeEvent("Močan zvok")
            }

            // Posodobitev vsako sekundo
            microphoneTextView.postDelayed({ updateMicrophone() }, 1000)
        } catch (e: Exception) {
            Log.e("SensorsActivity", "Error updating microphone", e)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == audioPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMicrophone()
                startMicrophone()
            } else {
                Toast.makeText(this, "Audio permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
