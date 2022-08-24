package com.orbitalsonic.languagetranslator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.orbitalsonic.languagetranslator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listener()
    }

    private fun listener() {
        binding.btnFirst.setOnClickListener {
            startActivity(Intent(this,FirstActivity::class.java))
        }
        binding.btnSecond.setOnClickListener {
            startActivity(Intent(this,SecondActivity::class.java))
        }
    }
}