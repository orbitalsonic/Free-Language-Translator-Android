package com.orbitalsonic.languagetranslator

import android.util.Log
import kotlinx.coroutines.*

class TranslationTasks(
    private var text: String,
    private var toLang: String?,
    private var fromLang: String?,
    private var resultCallback: (translation: String) -> Unit,
) {
    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("TranslatorTag", "$exception")
    }
    init {
        CoroutineScope(Dispatchers.IO+handler).launch {
            try {
                val translated = TranslationConnection.translateHttpURLConnection(
                    text,
                    toLang,
                    fromLang
                )

                withContext(Dispatchers.Main) {
                    resultCallback(translated)
                }
            } catch (ignored: Exception) {
            }
        }
    }
}