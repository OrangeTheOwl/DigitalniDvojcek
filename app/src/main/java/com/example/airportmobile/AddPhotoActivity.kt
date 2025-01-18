package com.example.airportmobile

import android.R
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
import java.io.File


class AddPhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddphotoBinding
    private lateinit var previewView: PreviewView
    private var imagePath : String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddphotoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        previewView  = binding.previewView
        binding.buttonOpenCamera.isEnabled = true
        binding.buttonOpenCamera.isVisible = true
        binding.buttonTakePhoto.isEnabled = false
        binding.buttonTakePhoto.isVisible = false
        binding.buttonPost.isEnabled = false
        binding.buttonPost.isVisible = false

        val items = arrayOf("Ljubljana", "Bratislava", "Dunaj", "Zagreb")
        val adapter: ArrayAdapter<String?> = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, items)
        binding.spinner.adapter = adapter

        binding.buttonOpenCamera.setOnClickListener(){
            //setupLocation()
            //getLocation()
            requestCameraPermission()

            binding.buttonOpenCamera.isEnabled = false
            binding.buttonOpenCamera.isVisible = false
            binding.buttonTakePhoto.isEnabled = true
            binding.buttonTakePhoto.isVisible = true
            binding.buttonPost.isEnabled = false
            binding.buttonPost.isVisible = false
        }

        binding.buttonTakePhoto.setOnClickListener(){
            binding.buttonOpenCamera.isEnabled = false
            binding.buttonOpenCamera.isVisible = false
            binding.buttonTakePhoto.isEnabled = false
            binding.buttonTakePhoto.isVisible = false
            binding.buttonPost.isEnabled = true
            binding.buttonPost.isVisible = true
            captureImage()
        }

        /*binding.buttonPost.setOnClickListener(){
            if (imagePath != null && myLocation != null){
                postImage(imagePath!!, myLocation!!)
                var toast= Toast.makeText(this@AddPostActivity, "Number: " + app.data.size.toString(), Toast.LENGTH_SHORT) // in Activity
                toast.show()
            }
            else{
                var toast= Toast.makeText(this@AddPostActivity, "errorWithPost", Toast.LENGTH_SHORT) // in Activity
                toast.show()
            }

            val intent = Intent(
                this@AddPostActivity,
                MapsActivity::class.java
            )
            // FLAG_UPDATE_CURRENT specifies that if a previous
            // PendingIntent already exists, then the current one
            // will update it with the latest intent
            // 0 is the request code, using it later with the
            // same method again will get back the same pending
            // intent for future reference
            // intent passed here is to our afterNotification class
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)


            // checking if android version is greater than oreo(API 26) or not
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_notification_overlay)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_notification_overlay))
                    .setContentText("New location is here!!!!")
                    .setContentIntent(pendingIntent)
            } else {

                builder = Notification.Builder(this)
                    .setSmallIcon(R.drawable.ic_notification_overlay)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_notification_overlay))
                    .setContentText("New location is here!!!!")
                    .setContentIntent(pendingIntent)
            }
            notificationManager.notify(1234, builder.build())
            finish()
        }*/

    }
    private val cameraPermissionRequestCode = 100
    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                cameraPermissionRequestCode
            )
        } else {
            // Permission already granted
            openCamera()
        }
    }


    private fun openCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val preview = Preview.Builder().build()
            val imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
                preview.setSurfaceProvider(previewView.surfaceProvider)
            } catch (exception: Exception) {
                // Handle camera setup errors
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun captureImage() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val preview = Preview.Builder().build()
            val imageCapture = ImageCapture.Builder()
                .setTargetRotation(previewView.display.rotation)
                .build()

            try {
                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
                val file = File(externalMediaDirs.first(), System.currentTimeMillis().toString() + ".jpg")
                val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
                    object : ImageCapture.OnImageSavedCallback {

                        @SuppressLint("RestrictedApi")
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            // Image saved successfully
                            val toast = Toast.makeText(this@AddPhotoActivity, "saved", Toast.LENGTH_SHORT) // in Activity
                            //val toastLocation = Toast.makeText(this@AddPhotoActivity, "myLocation: "+myLocation.toString(), Toast.LENGTH_SHORT) // in Activity
                            Log.w("0", outputOptions.file.toString());
                            toast.show()
                            //toastLocation.show()
                            imagePath = outputOptions.file.toString()
                        }

                        override fun onError(exception: ImageCaptureException) {

                            // Handle image capture errors
                            val toast = Toast.makeText(this@AddPhotoActivity, exception.message, Toast.LENGTH_SHORT) // in Activity

                            Log.w("myApp", exception.message.toString());
                            toast.show()
                        }
                    })
            } catch (exception: Exception) {
                // Handle camera setup errors
            }
        }, ContextCompat.getMainExecutor(this))



    }

    private fun postImage(path: String, myLocation: String) {



       /* var distance = haversine_distance(app.currentLocation, myLocation)

        var score = (10000 / distance * 10).toInt()
        if (score < 1) score = 1

        var imageToAdd = PostedImage(path, app.currentLocation, myLocation, distance, score)

        app.data.add(imageToAdd)
        app.saveToFile()
        app.saveScoreAndNumOfPosts(score)
        var lat = Random.nextDouble(-64.0, 64.0);
        var lon = Random.nextDouble(-64.0, 64.0);
        app.updateLocation(lat,lon)*/

    }
}