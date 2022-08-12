package com.orbitalsonic.languagetranslator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.orbitalsonic.languagetranslator.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val fromLanguage = "en"
    private val toLanguage = "hi"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listener()
    }

    private fun listener() {

        binding.btnTranslate.setOnClickListener {
            val text = binding.etTranslate.text.toString()
            if (text.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {

                    /*
                    Jsoup is an open source Java library used mainly for extracting data from HTML.
                     It also allows you to manipulate and output HTML.It provides a very convenient API
                     for extracting and manipulating data, using the best of DOM, CSS, and jquery-like methods.

                     How do you use kotlin jsoup?
                     Using jsoup with Kotlin to parse HTML
                     Fetch HTML response from a URL.
                     Parse it and scrape information from it.
                     Dump it somewhere.
                     */
                    val doc = Jsoup.connect(
                        "https://translate.google.com/m?hl=en" +
                                "&sl=$fromLanguage" +
                                "&tl=$toLanguage" +
                                "&ie=UTF-8&prev=_m" +
                                "&q=$text"
                    )
                        .timeout(6000)
                        .get()

                    withContext(Dispatchers.Main) {
                        val element = doc.getElementsByClass("result-container")
                        val textIs: String
                        if (element.isNotEmpty()) {
                            textIs = element[0].text()
                            binding.tvTranslated.text = textIs
                        }
                    }
                }

            } else {
                Toast.makeText(this, "Enter Text To Translate First", Toast.LENGTH_SHORT).show()
            }
        }
    }
}