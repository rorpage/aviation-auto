# aviation-auto

A basic Android Auto app that shows current weather for the device's current location using OpenWeatherMap.

## Setup

1. Create a `local.properties` file in the project root.
2. Add your OpenWeatherMap API key:

```properties
OPEN_WEATHER_MAP_API_KEY=your_api_key_here
```

## What is implemented

- Android Auto `CarAppService` entry point (`WeatherCarAppService`)
- Weather screen template with:
  - Current weather title
  - Background weather fetch on first render
  - Refresh button
- Current-location lookup via `LocationManager` last known location
- Current weather lookup from OpenWeatherMap current weather API

## Main files

- `app/src/main/java/com/rorpage/aviationauto/WeatherCarAppService.kt`
- `app/src/main/java/com/rorpage/aviationauto/WeatherSession.kt`
- `app/src/main/java/com/rorpage/aviationauto/WeatherScreen.kt`
- `app/src/main/java/com/rorpage/aviationauto/WeatherRepository.kt`

## UI preview

- `weather-ui-screenshot.ppm` (mock screenshot of the Android Auto weather template layout)
