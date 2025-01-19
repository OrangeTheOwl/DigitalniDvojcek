package com.example.airportmobile

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.airportmobile.databinding.ActivityAddphotoBinding
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.*

class AddPhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddphotoBinding
    private lateinit var previewView: PreviewView
    private var imageCapture: ImageCapture? = null
    private var imagePath: String? = null
    private var selectedLocation: String? = null
    private var selectedFrequency: String? = null
    private val handler = Handler(Looper.getMainLooper())
    private var isPostingContinuously = false
    private var isBackCamera = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddphotoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        previewView = binding.previewView
        binding.buttonOpenCamera.isEnabled = true
        binding.buttonOpenCamera.isVisible = true
        binding.buttonTakePhoto.isEnabled = false
        binding.buttonTakePhoto.isVisible = false
        binding.buttonPost.isEnabled = false
        binding.buttonPost.isVisible = false
        binding.buttonReset.isEnabled = false
        binding.buttonReset.isVisible = false

        // Location spinner setup
        val locations = arrayOf("Ljubljana", "Bratislava", "Dunaj", "Zagreb")
        val locationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locations)
        binding.spinner.adapter = locationAdapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedLocation = locations[position]
                Log.d("SelectedLocation", "Selected: $selectedLocation")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedLocation = null
                Log.d("SelectedLocation", "Nothing selected")
            }
        }

        // Frequency spinner setup
        val frequencies = arrayOf("No Frequency", "10 Seconds", "2 Minutes")
        val frequencyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, frequencies)
        binding.spinnerFrequency.adapter = frequencyAdapter

        binding.spinnerFrequency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedFrequency = frequencies[position]
                if (selectedFrequency == "No Frequency") {
                    stopContinuousPosting()
                }
                Log.d("SelectedFrequency", "Selected: $selectedFrequency")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedFrequency = "No Frequency"
                stopContinuousPosting()
                Log.d("SelectedFrequency", "No frequency selected")
            }
        }

        binding.buttonOpenCamera.setOnClickListener {
            requestCameraPermission()
            binding.buttonOpenCamera.isEnabled = false
            binding.buttonOpenCamera.isVisible = false
            binding.buttonTakePhoto.isEnabled = true
            binding.buttonTakePhoto.isVisible = true
            binding.buttonPost.isEnabled = false
            binding.buttonPost.isVisible = false
        }

        binding.buttonTakePhoto.setOnClickListener {
            captureImage()
        }

        binding.buttonPost.setOnClickListener {
            if (selectedLocation != null) {
                if (selectedFrequency == "No Frequency") {
                    addNewEntryToJson(selectedLocation!!)
                    Toast.makeText(this, "New entry added to JSON file", Toast.LENGTH_SHORT).show()
                } else {
                    startContinuousPosting()
                }
            } else {
                Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonReset.setOnClickListener {
            binding.imageViewCapturedPhoto.isVisible = false
            binding.previewView.isVisible = true
            binding.buttonTakePhoto.isVisible = true
            binding.buttonTakePhoto.isEnabled = true
            binding.buttonPost.isEnabled = false
            binding.buttonPost.isVisible = false
            binding.buttonReset.isEnabled = false
            binding.buttonReset.isVisible = false
        }

        binding.buttonSwitchCamera.setOnClickListener {
            isBackCamera = !isBackCamera
            openCamera()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopContinuousPosting() // Ensure posting stops when activity is destroyed
    }

    override fun onBackPressed() {
        stopContinuousPosting() // Ensure posting stops when user navigates back
        super.onBackPressed()
    }

    private fun startContinuousPosting() {
        isPostingContinuously = true
        val interval = when (selectedFrequency) {
            "10 Seconds" -> 10_000L
            "2 Minutes" -> 120_000L
            else -> 0L
        }

        handler.post(object : Runnable {
            override fun run() {
                if (isPostingContinuously && selectedLocation != null) {
                    addNewEntryToJson(selectedLocation!!)
                    Toast.makeText(this@AddPhotoActivity, "New entry added to JSON file", Toast.LENGTH_SHORT).show()
                    handler.postDelayed(this, interval)
                }
            }
        })
    }

    private fun stopContinuousPosting() {
        isPostingContinuously = false
        handler.removeCallbacksAndMessages(null)
        Log.d("ContinuousPosting", "Stopped continuous posting")
    }

    private val cameraPermissionRequestCode = 100
    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                cameraPermissionRequestCode
            )
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = if (isBackCamera) {
                CameraSelector.DEFAULT_BACK_CAMERA
            } else {
                CameraSelector.DEFAULT_FRONT_CAMERA
            }
            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9) // Adjust aspect ratio as needed
                .build()
            val imageCaptureInstance = ImageCapture.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9) // Ensure consistency
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCaptureInstance
                )
                preview.setSurfaceProvider(previewView.surfaceProvider)
                imageCapture = imageCaptureInstance
            } catch (exception: Exception) {
                Log.e("CameraError", "Error binding camera lifecycle", exception)
            }
        }, ContextCompat.getMainExecutor(this))
    }


    private fun captureImage() {
        val file = File(externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        imageCapture?.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    imagePath = file.absolutePath
                    binding.imageViewCapturedPhoto.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
                    binding.imageViewCapturedPhoto.isVisible = true
                    binding.previewView.isVisible = false
                    binding.buttonTakePhoto.isVisible = false
                    binding.buttonPost.isEnabled = true
                    binding.buttonPost.isVisible = true
                    binding.buttonReset.isEnabled = true
                    binding.buttonReset.isVisible = true
                    Toast.makeText(this@AddPhotoActivity, "Image saved: $imagePath", Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("ImageCapture", "Error capturing image", exception)
                    Toast.makeText(this@AddPhotoActivity, "Error capturing image", Toast.LENGTH_SHORT).show()
                }
            }) ?: run {
            Toast.makeText(this, "Camera not initialized", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addNewEntryToJson(location: String) {
        try {
            val assetManager = assets
            val jsonString = assetManager.open("crowdData.json").bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)

            // Create a new entry
            val newEntry = JSONObject()
            newEntry.put("locationName", location)
            newEntry.put("crowdNumber", (0..1000).random())
            newEntry.put("timestamp", System.currentTimeMillis())

            jsonArray.put(newEntry)

            // Write back to the file
            val file = File(filesDir, "crowdData.json")
            val fos = FileOutputStream(file)
            val writer = OutputStreamWriter(fos)
            writer.write(jsonArray.toString())
            writer.close()
            fos.close()

            Log.d("JSONUpdate", "New entry added: $newEntry to $filesDir")
        } catch (e: Exception) {
            Log.e("JSONError", "Error updating JSON file", e)
        }
    }
}
