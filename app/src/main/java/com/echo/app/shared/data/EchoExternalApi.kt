package com.echo.app.shared.data

import com.echo.app.BuildConfig
import com.echo.app.shared.data.klipy.KlipySearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class EchoExternalApi(private val ktorClient: HttpClient) {
    private val baseGifUrl = "https://api.klipy.com"
    private val gifApiKey = BuildConfig.KLIPY_API_KEY

    suspend fun getPopularGifs(page: Int = 1, userId: String = "-1"): KlipySearchResponse {
        val response = ktorClient.get("$baseGifUrl/api/v1/$gifApiKey/gifs/trending") {
            parameter("page", page)
            parameter("per_page", 24)
            parameter("customer_id", userId)
            parameter("format_filter", "gif")
            parameter("content_filter", "low")
        }.body<KlipySearchResponse>()

        return response
    }

    suspend fun searchForGifs(query: String, page: Int = 1, userId: String = "-1"): KlipySearchResponse {
        val response = ktorClient.get("$baseGifUrl/api/v1/$gifApiKey/gifs/search") {
            parameter("q", query)
            parameter("page", page)
            parameter("per_page", 24)
            parameter("customer_id", userId)
            parameter("format_filter", "gif")
            parameter("content_filter", "low")
        }.body<KlipySearchResponse>()

        return response
    }
}