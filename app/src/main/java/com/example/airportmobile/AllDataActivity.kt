package com.example.airportmobile

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.airportmobile.databinding.ActivityAlldataBinding
import com.example.airportmobile.databinding.ActivityMainBinding

class AllDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlldataBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAlldataBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.progressBarLjubljana.setProgress(100);
        binding.progressBarZagreb.setProgress(200);
        binding.progressBarBratislava.setProgress(300);
        binding.progressBarDunaj.setProgress(400);

    }
}