package com.rorpage.aviationauto

import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest {
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun fetchCurrentWeather_formatsWeatherSummary() {
        server.enqueue(
            MockResponse().setResponseCode(200)
                .setBody(
                    """
                    {
                      "name": "Seattle",
                      "weather": [{"description": "scattered clouds"}],
                      "main": {"temp": 12.34}
                    }
                    """.trimIndent()
                )
        )

        val repository = WeatherRepository(
            client = OkHttpClient(),
            weatherEndpoint = server.url("/data/2.5/weather").toString()
        )

        val result = repository.fetchCurrentWeather(47.6, -122.3, "test-key")

        assertEquals("Seattle: 12.3°C, scattered clouds", result)

        val request = server.takeRequest()
        assertEquals("47.6", request.requestUrl?.queryParameter("lat"))
        assertEquals("-122.3", request.requestUrl?.queryParameter("lon"))
        assertEquals("test-key", request.requestUrl?.queryParameter("appid"))
        assertEquals("metric", request.requestUrl?.queryParameter("units"))
    }

    @Test
    fun fetchCurrentWeather_requiresApiKey() {
        val repository = WeatherRepository()

        val result = runCatching {
            repository.fetchCurrentWeather(0.0, 0.0, "")
        }

        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }
}
