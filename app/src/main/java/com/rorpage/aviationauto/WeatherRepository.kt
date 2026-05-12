package com.rorpage.aviationauto

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.Locale

class WeatherRepository(
    private val client: OkHttpClient = OkHttpClient(),
    private val weatherEndpoint: String = DEFAULT_WEATHER_ENDPOINT
) {
    fun fetchCurrentWeather(latitude: Double, longitude: Double, apiKey: String): String {
        require(apiKey.isNotBlank()) { "OpenWeatherMap API key is missing." }

        val url = weatherEndpoint
            .toHttpUrl()
            .newBuilder()
            .addQueryParameter("lat", latitude.toString())
            .addQueryParameter("lon", longitude.toString())
            .addQueryParameter("appid", apiKey)
            .addQueryParameter("units", "metric")
            .build()

        val request = Request.Builder().url(url).build()

        client.newCall(request).execute().use { response ->
            check(response.isSuccessful) { "Weather lookup failed: HTTP ${response.code}" }

            val body = requireNotNull(response.body?.string()) { "Weather response body is empty." }
            val payload = JSONObject(body)
            val city = payload.optString("name", "Current location")
            val description = payload
                .optJSONArray("weather")
                ?.optJSONObject(0)
                ?.optString("description", "No description")
                ?: "No description"
            val temp = payload
                .optJSONObject("main")
                ?.optDouble("temp", Double.NaN)

            return if (temp == null || temp.isNaN()) {
                "$city: $description"
            } else {
                "$city: ${"%.1f".format(Locale.US, temp)}°C, $description"
            }
        }
    }

    companion object {
        const val DEFAULT_WEATHER_ENDPOINT = "https://api.openweathermap.org/data/2.5/weather"
    }
}
