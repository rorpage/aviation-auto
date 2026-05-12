package com.rorpage.aviationauto

import androidx.car.app.Screen
import androidx.car.app.Session

class WeatherSession : Session() {
    override fun onCreateScreen(intent: android.content.Intent): Screen = WeatherScreen(carContext)
}
