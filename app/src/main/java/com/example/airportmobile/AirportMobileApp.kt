package com.example.airportmobile

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class AirportMobileApp : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            FirebaseApp.initializeApp(this)
            Log.d("FirebaseInit", "Firebase successfully initialized.")
        } catch (e: Exception) {
            Log.e("FirebaseInit", "Failed to initialize Firebase: ${e.message}")
        }
    }
}
