package com.orbitalsonic.languagetranslator

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.orbitalsonic.languagetranslator.databinding.ActivitySecondBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private val fromLanguage = "en"
    private val toLanguage = "hi"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listener()
    }

    private fun listener() {

//        Long Text Translation
        binding.btnTranslate.setOnClickListener {
            val text = binding.etTranslate.text.toString()
            if (text.isNotEmpty()) {
                 TranslationTasks(text, toLanguage, fromLanguage) {
                    binding.tvTranslated.text = it
                }

            } else {
                Toast.makeText(this, "Enter Text To Translate First", Toast.LENGTH_SHORT).show()
            }
        }
    }
}