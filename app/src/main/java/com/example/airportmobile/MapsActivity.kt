package com.example.airportmobile

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.airportmobile.database.AppDatabase
import com.example.airportmobile.database.CustomMarkerEntity
import com.example.airportmobile.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var database: AppDatabase

    private val locationPermissionRequestCode = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = AppDatabase.getInstance(applicationContext)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Set button click listeners
        binding.buttonMyLocation.setOnClickListener {
            moveToMyLocation()
        }

        binding.buttonAddPost.setOnClickListener {
            addMarkerAtCurrentCameraPosition()
        }

        binding.buttonSearch.setOnClickListener {
            searchLocation()
        }

        binding.buttonSettings.setOnClickListener {
            showSettingsDialog()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkLocationPermission()

        val sloveniaCenter = LatLng(46.1512, 14.9955)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sloveniaCenter, 7f))

        addAirportMarkers()
        addTrafficMarkers()
        //loadCustomMarkers()
        loadCustomMarkersFromFirebase()
        loadExtremeEventMarkers()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            enableUserLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionRequestCode
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableUserLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            Toast.makeText(this, "Please grant location permission", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun moveToMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val userLocation = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                    Toast.makeText(this, "Moved to your location", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Please grant location permission", Toast.LENGTH_SHORT).show()
            checkLocationPermission()
        }
    }

    private fun addMarkerAtCurrentCameraPosition() {
        val currentLatLng = mMap.cameraPosition.target
        showAddMarkerDialog(currentLatLng)
    }

    private fun searchLocation() {
        val location = binding.searchLocation.text.toString()
        if (location.isEmpty()) {
            Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show()
            return
        }

        val geocoder = Geocoder(this, Locale.getDefault())
        val addressList = geocoder.getFromLocationName(location, 1)

        if (addressList.isNullOrEmpty()) {
            Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
        } else {
            val address = addressList[0]
            val latLng = LatLng(address.latitude, address.longitude)
            mMap.addMarker(MarkerOptions().position(latLng).title(location))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
        }
    }

    private fun addAirportMarkers() {
        Thread {
            try {
                val airportMarkers = database.airportMarkerDao().getAll()
                val markers = mutableListOf<com.google.android.gms.maps.model.Marker>()
                runOnUiThread {
                    for (marker in airportMarkers) {
                        val mapMarker = mMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(marker.latitude, marker.longitude))
                                .title(marker.name)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                        )
                        if (mapMarker != null) {
                            markers.add(mapMarker)
                        }
                    }

                    runOnUiThread {
                        markersMap["airport"] = markers
                        updateMarkerVisibility()
                    }
                }
            } catch (e: Exception) {
                Log.e("MapsActivity", "Error fetching airport markers: ${e.message}")
            }
        }.start()
    }

    private fun addTrafficMarkers() {
        Thread {
            try {
                val trafficMarkers = database.trafficMarkerDao().getAll()
                val geocoder = Geocoder(this, Locale.getDefault())

                val markers = mutableListOf<com.google.android.gms.maps.model.Marker>()
                for (marker in trafficMarkers) {
                    val addressList = geocoder.getFromLocationName(marker.location, 1)
                    if (!addressList.isNullOrEmpty()) {
                        val address = addressList[0]
                        val latLng = LatLng(address.latitude, address.longitude)

                        // Add marker on the main thread
                        runOnUiThread {
                            val mapMarker = mMap.addMarker(
                                MarkerOptions()
                                    .position(latLng)
                                    .title(marker.status)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            )
                            if (mapMarker != null) {
                                markers.add(mapMarker)
                            }
                        }
                    }
                }

                runOnUiThread {
                    markersMap["traffic"] = markers
                    updateMarkerVisibility()
                }
            } catch (e: Exception) {
                Log.e("MapsActivity", "Error fetching traffic markers: ${e.message}", e)
            }
        }.start()
    }

    private val markersMap = mutableMapOf<String, MutableList<com.google.android.gms.maps.model.Marker>>()
    private var showAirportMarkers = true
    private var showTrafficMarkers = true
    private var showCustomMarkers = true
    private var showExtremeEventMarkers = true

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Marker Visibility")

        val options = arrayOf("Airports", "Traffic", "Custom", "Extreme Events")
        val checkedItems = booleanArrayOf(
            showAirportMarkers,
            showTrafficMarkers,
            showCustomMarkers,
            showExtremeEventMarkers
        )

        builder.setMultiChoiceItems(options, checkedItems) { _, which, isChecked ->
            when (which) {
                0 -> showAirportMarkers = isChecked
                1 -> showTrafficMarkers = isChecked
                2 -> showCustomMarkers = isChecked
                3 -> showExtremeEventMarkers = isChecked
            }
        }

        builder.setPositiveButton("Apply") { _, _ ->
            updateMarkerVisibility()
        }

        builder.setNegativeButton("Cancel", null)

        builder.create().show()
    }

    private fun updateMarkerVisibility() {
        markersMap["airport"]?.forEach { it.isVisible = showAirportMarkers }
        markersMap["traffic"]?.forEach { it.isVisible = showTrafficMarkers }
        markersMap["custom"]?.forEach { it.isVisible = showCustomMarkers }
        markersMap["extreme"]?.forEach { it.isVisible = showExtremeEventMarkers }
    }



    private fun showAddMarkerDialog(latLng: LatLng) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Marker")

        val input = EditText(this)
        input.hint = "Enter marker name"
        builder.setView(input)

        builder.setPositiveButton("Add") { _, _ ->
            val markerName = input.text.toString()
            if (markerName.isNotEmpty()) {
                saveMarkerToDatabase(latLng, markerName)
            } else {
                Toast.makeText(this, "Marker name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }


    private fun saveMarkerToDatabase(latLng: LatLng, name: String) {
        Thread {
            try {
                val marker = CustomMarkerEntity(
                    latitude = latLng.latitude,
                    longitude = latLng.longitude,
                    name = name
                )
                database.customMarkerDao().insert(marker)

                saveMarkerToFirebase(marker)

                runOnUiThread {
                    mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(name)
                    )
                    Toast.makeText(this, "Marker added successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("MapsActivity", "Error saving marker: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this, "Failed to save marker", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun saveMarkerToFirebase(marker: CustomMarkerEntity) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("custom_markers")
            .add(marker)
            .addOnSuccessListener {
                Log.d("Firebase", "Marker sent to Firebase: $marker")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Failed to send marker to Firebase: ${e.message}", e)
            }
    }

    private fun loadCustomMarkersFromFirebase() {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("custom_markers")
            .get()
            .addOnSuccessListener { result ->
                val markers = mutableListOf<com.google.android.gms.maps.model.Marker>()
                for (document in result) {
                    val marker = document.toObject(CustomMarkerEntity::class.java)
                    val latLng = LatLng(marker.latitude, marker.longitude)

                    val mapMarker = mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(marker.name)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    )
                    if (mapMarker != null) {
                        markers.add(mapMarker)
                    }
                }

                markersMap["custom"] = markers
                updateMarkerVisibility()
            }
            .addOnFailureListener { e ->
                Log.e("MapsActivity", "Failed to load custom markers from Firebase: ${e.message}", e)
            }
    }

    private fun loadExtremeEventMarkers() {
        Thread {
            try {
                val extremeEvents = database.extremeEventDao().getAll()
                val markers = mutableListOf<com.google.android.gms.maps.model.Marker>()
                val geocoder = Geocoder(this, Locale.getDefault())

                for (event in extremeEvents) {
                    val addressList = geocoder.getFromLocationName(event.location, 1)
                    if (!addressList.isNullOrEmpty()) {
                        val address = addressList[0]
                        val latLng = LatLng(address.latitude, address.longitude)
                        val mapMarker = mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(event.status)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        )
                        if (mapMarker != null) {
                            markers.add(mapMarker)
                        }
                    }
                }

                runOnUiThread {
                    markersMap["extreme"] = markers
                    updateMarkerVisibility()
                }
            } catch (e: Exception) {
                Log.e("MapsActivity", "Error loading extreme event markers: ${e.message}")
            }
        }.start()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
