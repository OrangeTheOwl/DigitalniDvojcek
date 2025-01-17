package com.example.airportmobile

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.airportmobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.buttonGoToPhoto.setOnClickListener(){
            startActivity(
                Intent(
                    this@MainActivity,
                    AddPhotoActivity::class.java
                )
            )
        }

        binding.buttonGoToAllData.setOnClickListener(){
            startActivity(
                Intent(
                    this@MainActivity,
                    AllDataActivity::class.java
                )
            )
        }
    }
}