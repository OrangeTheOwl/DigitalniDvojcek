package com.example.airportmobile

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.airportmobile.database.AppDatabase
import com.example.airportmobile.database.TestDataEntity
import com.example.airportmobile.databinding.ActivityAddphotoBinding
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.UUID
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AddPhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddphotoBinding
    private lateinit var previewView: PreviewView
    private var imageCapture: ImageCapture? = null
    private var imagePath: String? = null
    private var selectedLocation: String? = null
    private var isBackCamera = true
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddphotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        previewView = binding.previewView
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Initialize Room database
        database = AppDatabase.getInstance(applicationContext)

        // Location spinner setup
        val locations = arrayOf("Ljubljana", "Bratislava", "Dunaj", "Zagreb")
        val locationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locations)
        binding.spinner.adapter = locationAdapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedLocation = locations[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedLocation = null
            }
        }

        binding.buttonOpenCamera.setOnClickListener {
            requestCameraPermission()
        }

        binding.buttonTakePhoto.setOnClickListener {
            captureImage()
        }

        binding.buttonPost.setOnClickListener {
            if (imagePath != null && selectedLocation != null) {
                uploadImageToFlaskServer(imagePath!!)
            } else {
                Toast.makeText(this, "Please capture an image and select a location", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonSwitchCamera.setOnClickListener {
            isBackCamera = !isBackCamera
            openCamera()
        }

        binding.buttonReset.setOnClickListener {
            resetCameraView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                100
            )
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = if (isBackCamera) CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

                // Change button visibility when the camera is opened
                binding.buttonOpenCamera.isVisible = false
                binding.buttonTakePhoto.isVisible = true
                binding.buttonSwitchCamera.isVisible = true
                binding.buttonPost.isVisible = false
                binding.buttonReset.isVisible = false
            } catch (e: Exception) {
                Log.e("CameraError", "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun captureImage() {
        val photoFile = File(externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture?.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    imagePath = photoFile.absolutePath
                    binding.imageViewCapturedPhoto.setImageBitmap(BitmapFactory.decodeFile(imagePath))
                    binding.imageViewCapturedPhoto.isVisible = true
                    binding.previewView.isVisible = false
                    Toast.makeText(this@AddPhotoActivity, "Image saved: $imagePath", Toast.LENGTH_SHORT).show()

                    // Enable the "Post" button
                    binding.buttonPost.isVisible = true
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("ImageCapture", "Image capture failed", exception)
                }
            })
    }

    private fun resetCameraView() {
        binding.imageViewCapturedPhoto.isVisible = false
        binding.previewView.isVisible = true

        // Change button visibility to take photo mode
        binding.buttonTakePhoto.isVisible = true
        binding.buttonSwitchCamera.isVisible = true
        binding.buttonPost.isVisible = false
        binding.buttonReset.isVisible = false
    }

    private fun uploadImageToFlaskServer(imagePath: String) {
        val file = File(imagePath)
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name, file.asRequestBody("image/jpeg".toMediaTypeOrNull()))
            .build()

        val request = Request.Builder()
            .url("http://172.20.10.3:5000/predict") // Replace with your server's IP
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("UploadError", "Failed to send image to server: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@AddPhotoActivity, "Failed to connect: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d("ServerResponse", "Response from server: $responseBody")

                    // Parse JSON response
                    val jsonResponse = JSONObject(responseBody ?: "")
                    val predictedPeople = jsonResponse.optInt("predicted_people", -1)

                    // Save data to Room and Firebase
                    saveData(predictedPeople)

                    runOnUiThread {
                        Toast.makeText(this@AddPhotoActivity, "Predicted people: $predictedPeople", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("ServerError", "Server error: ${response.code}")
                    runOnUiThread {
                        Toast.makeText(this@AddPhotoActivity, "Server error: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun saveData(predictedPeople: Int) {
        val newEntry = TestDataEntity(
            locationId = UUID.randomUUID().toString(),
            locationName = selectedLocation ?: "Unknown",
            crowdNumber = predictedPeople,
            timestamp = System.currentTimeMillis().toString()
        )

        // Save to Room
        Thread {
            database.testDataDao().insertAll(newEntry)
            Log.d("RoomDatabase", "New entry saved to Room: $newEntry")
        }.start()

        // Save to Firebase
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("test_data")
            .add(newEntry)
            .addOnSuccessListener {
                Log.d("Firebase", "New entry saved to Firebase: $newEntry")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Failed to save data to Firebase: ${e.message}", e)
            }
    }
}
