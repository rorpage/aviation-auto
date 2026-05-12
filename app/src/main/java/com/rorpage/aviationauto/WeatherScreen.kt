package com.rorpage.aviationauto

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.MessageTemplate
import androidx.car.app.model.ParkedOnlyOnClickListener
import androidx.car.app.model.Template
import kotlin.concurrent.thread

class WeatherScreen(carContext: CarContext) : Screen(carContext) {
    @Volatile
    private var statusText: String = "Loading weather..."

    @Volatile
    private var loaded = false

    private val weatherRepository = WeatherRepository()

    override fun onGetTemplate(): Template {
        if (!loaded) {
            loaded = true
            thread(start = true, name = "weather-loader") {
                statusText = runCatching {
                    val location = getLastKnownLocation(carContext)
                    weatherRepository.fetchCurrentWeather(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY
                    )
                }.getOrElse { error ->
                    "Unable to load weather: ${error.message ?: "unknown error"}"
                }
                invalidate()
            }
        }

        return MessageTemplate.Builder(statusText)
            .setTitle("Current Weather")
            .addAction(
                Action.Builder()
                    .setTitle("Refresh")
                    .setOnClickListener(ParkedOnlyOnClickListener.create {
                        loaded = false
                        statusText = "Loading weather..."
                        invalidate()
                    })
                    .build()
            )
            .setHeaderAction(Action.APP_ICON)
            .build()
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(context: Context): Location {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
        for (provider in providers) {
            val location = runCatching { locationManager.getLastKnownLocation(provider) }.getOrNull()
            if (location != null) {
                return location
            }
        }
        error("No last known location available yet.")
    }
}
