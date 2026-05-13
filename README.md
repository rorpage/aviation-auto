# aviation-auto

A basic Android Auto app that shows current weather for the device's current location using OpenWeatherMap.

## Setup

1. Get a free API key from [openweathermap.org](https://openweathermap.org/api).
2. Add it to `local.properties` in the project root (this file is gitignored, so it is safe for secrets):

```properties
OPEN_WEATHER_MAP_API_KEY=your_api_key_here
```

3. Sync and build the project in Android Studio.

## CI / GitHub Actions

The API key is injected at build time via `BuildConfig`. In GitHub Actions, add the key as a repository secret named `OPEN_WEATHER_MAP_API_KEY`, then pass it to Gradle in the workflow:

```yaml
run: ./gradlew --no-daemon :app:assembleDebug -POPEN_WEATHER_MAP_API_KEY=${{ secrets.OPEN_WEATHER_MAP_API_KEY }}
```

The build succeeds without the key (it defaults to an empty string), but weather fetches will fail at runtime.

## What is implemented

- Android Auto `CarAppService` entry point (`WeatherCarAppService`)
- Weather screen template with:
  - Current weather title
  - Background weather fetch on first render
  - Refresh button (parked only)
- Current-location lookup via `LocationManager` last known location
- Current weather lookup from OpenWeatherMap current weather API
- Unit tests for the weather repository (`WeatherRepositoryTest`)

## Main files

- `app/src/main/java/com/rorpage/aviationauto/WeatherCarAppService.kt`
- `app/src/main/java/com/rorpage/aviationauto/WeatherSession.kt`
- `app/src/main/java/com/rorpage/aviationauto/WeatherScreen.kt`
- `app/src/main/java/com/rorpage/aviationauto/WeatherRepository.kt`
