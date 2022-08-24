package com.orbitalsonic.languagetranslator

import android.text.TextUtils
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object TranslationConnection {

    private const val charset = "UTF-8"

    fun getShortText(msg: String): String? {
        var text = msg
        try {
            if (text.length > 180) {
                text = text.substring(0, 180)
                text = text.substring(0, text.lastIndexOf(' '))
            }
        } catch (ignored: Exception) {
        }
        try {
            return URLEncoder.encode(text, "UTF-8")
        } catch (ignored: Exception) {
        }
        return text
    }

    private fun getTextHttpURLConnection(url: String): String {
        var connection: HttpURLConnection? = null

        /*StringBuilder class can be used when you want to modify a string without creating a new object.
         For example, using the StringBuilder class can boost performance when concatenating many strings
          together in a loop.*/

        val response = StringBuilder()
        try {
            val string = "UTF-8"

            /*
            * A URL (Uniform Resource Locator) is a unique identifier used to locate a resource on the Internet.
            *  It is also referred to as a web address. End users use URLs by typing them directly into the address
            * bar of a browser or by clicking a hyperlink found on a webpage, bookmark list, in an email or from another application.
            * */


//            HttpURLConnection class can retrieve information of any HTTP URL such as header information, status code, response code etc.

//            buffer reader  used to read input from external system. BufferReader is used to read text from character streams while
//            buffering the characters for efficient reading of characters.

            connection = URL(url).openConnection() as HttpURLConnection
            connection.setRequestProperty("Accept-Charset", "UTF-8")
            connection.addRequestProperty(
                "User-Agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)"
            )
            val `in` = BufferedReader(
                InputStreamReader(
                    connection.inputStream, string
                )
            )
            while (true) {
                val inputLine = `in`.readLine()
                if (inputLine == null) {
                    `in`.close()
                    return response.toString()
                }
                response.append(inputLine)
            }
        } catch (e: java.lang.Exception) {
        } finally {
            connection?.disconnect()
        }
        return response.toString()
    }

    fun translateHttpURLConnection(
        to_translate: String?,
        to_language: String?,
        from_language: String?,
    ): String {
        try {

            /*
            * URL encoding converts characters into a format that can be transmitted over the Internet.
            * URLs can only be sent over the Internet using the ASCII character-set. Since URLs often
            * contain characters outside the ASCII set, the URL has to be converted into a valid ASCII format.
            * */

            val hl = URLEncoder.encode(to_language, charset)
            val sl = URLEncoder.encode(from_language, charset)
            val q = URLEncoder.encode(to_translate, charset)
            try {
                val sb = java.lang.StringBuilder()
                var text =
                    getTextHttpURLConnection(
                        String.format(
                            "https://translate.google.com/translate_a/single?&client=gtx&sl=%s&tl=%s&q=%s&dt=t",
                            sl,
                            hl,
                            q
                        )
                    )
//                Returns true if the string is null or 0-length.
                /*
                 TextUtils is simply a set of utility functions to do operations on String objects.
                   Let's take an example which I usually see in the projects, it's a simple example
                    like we all do, the string null check and empty check.*/

                if (TextUtils.isEmpty(text)) {
                    text =
                        getTextHttpURLConnection(
                            String.format(
                                "https://clients4.google.com/translate_a/t?client=dict-chrome-ex&sl=%s&tl=%s&q=%s&dt=t",
                                sl,
                                hl,
                                q
                            )
                        )
                    if (TextUtils.isEmpty(text)) {
                        text =
                            getTextHttpURLConnection(
                                String.format(
                                    "https://translate.google.com/m?sl=%s&tl=%s&q=%s",
                                    sl,
                                    hl,
                                    q
                                )
                            )
                        if (TextUtils.isEmpty(text)) {
                            sb.append(translateURLConnection(sl, hl, q))
                        } else {
                            sb.append(getTranslationData(text))
                        }
                    } else {

                        /*
                        The kotlin JSON is one of the default methods for parsing the data between the server and client.
                         It is minimal, textual and a subset of JavaScript.  It looks like the alternative to XML parsing.
                        The JSON object have contains keys and value pairs which is similar to the map collections.
                        * It is commonly used for transmitting data in web applications
                        */


                        val jsonObject = JSONObject(text)
                        if (jsonObject.has("sentences")) {
                            val jsonArray = jsonObject.getJSONArray("sentences")
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject1 = jsonArray.getJSONObject(i)
                                if (jsonObject1 != null && jsonObject1.has("trans")) {
                                    sb.append(jsonObject1.getString("trans"))
                                }
                            }
                        }
                    }
                } else {
                    /*
                    * JSON stands for JavaScript Object Notation.It is an independent data exchange format and is the best alternative for XML.
                    * Android provides four different classes to manipulate JSON data. These classes are JSONArray,JSONObject,JSONStringer
                    * and JSONTokenizer.
                    * */

                    val jSONArray = JSONArray(text).getJSONArray(0)
                    for (i in 0 until jSONArray.length()) {
                        val string = jSONArray.getJSONArray(i).getString(0)
                        if (!TextUtils.isEmpty(string) && string != "null") {
                            sb.append(string)
                        }
                    }
                }
                return sb.toString()
            } catch (ignored: java.lang.Exception) {
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun translateURLConnection(sl: String?, hl: String?, q: String?): String {
        try {
            try {
                val sb = java.lang.StringBuilder()
                var text: String? =
                    getTextUrlConnection(
                        String.format(
                            "https://translate.google.com/translate_a/single?&client=gtx&sl=%s&tl=%s&q=%s&dt=t",
                            sl,
                            hl,
                            q
                        )
                    )
                if (TextUtils.isEmpty(text)) {
                    text =
                        getTextUrlConnection(
                            String.format(
                                "https://clients4.google.com/translate_a/t?client=dict-chrome-ex&sl=%s&tl=%s&q=%s&dt=t",
                                sl,
                                hl,
                                q
                            )
                        )
                    if (TextUtils.isEmpty(text)) {
                        text =
                            getTextUrlConnection(
                                String.format(
                                    "https://translate.google.com/m?sl=%s&tl=%s&q=%s",
                                    sl,
                                    hl,
                                    q
                                )
                            )
                        if (TextUtils.isEmpty(text)) {
                            Log.d("ct_TAG", "translateURLConnection: ")
                        } else {
                            sb.append(getTranslationData(text))
                        }
                    } else {
                        val jsonObject = JSONObject(text)
                        if (jsonObject.has("sentences")) {
                            val jsonArray = jsonObject.getJSONArray("sentences")
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject1 = jsonArray.getJSONObject(i)
                                if (jsonObject1 != null && jsonObject1.has("trans")) {
                                    sb.append(jsonObject1.getString("trans"))
                                }
                            }
                        }
                    }
                } else {
                    val jSONArray = JSONArray(text).getJSONArray(0)
                    for (i in 0 until jSONArray.length()) {
                        val string = jSONArray.getJSONArray(i).getString(0)
                        if (!TextUtils.isEmpty(string) && string != "null") {
                            sb.append(string)
                        }
                    }
                }
                return sb.toString()
            } catch (ignored: java.lang.Exception) {
            }
        } catch (ignored: java.lang.Exception) {
        }
        return ""
    }

    private fun getTextUrlConnection(url: String): String {
        try {
            val connection = URL(url).openConnection()
            connection.setRequestProperty("Accept-Charset", "UTF-8")
            connection.addRequestProperty(
                "User-Agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)"
            )
            val `in` = BufferedReader(InputStreamReader(connection.getInputStream(), charset))
            val response = java.lang.StringBuilder()
            while (true) {
                val inputLine = `in`.readLine()
                if (inputLine == null) {
                    `in`.close()
                    return response.toString()
                }
                response.append(inputLine)
            }
        } catch (e: java.lang.Exception) {
        }
        return ""
    }

    private fun getTranslationData(to_translate: String): String {
        try {

            var nativeText = "class=\"t0\">"
            val result =
                to_translate.substring(to_translate.indexOf(nativeText) + nativeText.length)
                    .split("<".toRegex()).toTypedArray()[0]
            return if (result == "html>") {
                nativeText = "class=\"result-container\">"
                to_translate.substring(to_translate.indexOf(nativeText) + nativeText.length)
                    .split("<".toRegex()).toTypedArray()[0] + "+" + ""
            } else {
                result
            }
        } catch (e: java.lang.Exception) {
        } catch (e: OutOfMemoryError) {
        }
        return ""
    }
}