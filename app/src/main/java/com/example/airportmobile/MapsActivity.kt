package com.example.airportmobile

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.airportmobile.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionRequestCode = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        binding.buttonHome.setOnClickListener {
            finish()
        }

        binding.buttonSearch.setOnClickListener {
            searchLocation()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkLocationPermission()

        // Add a default marker
        val defaultLocation = LatLng(46.0, 15.0)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))
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
        mMap.addMarker(
            MarkerOptions()
                .position(currentLatLng)
                .title("New Marker")
        )
        Toast.makeText(this, "Marker added at $currentLatLng", Toast.LENGTH_SHORT).show()
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
