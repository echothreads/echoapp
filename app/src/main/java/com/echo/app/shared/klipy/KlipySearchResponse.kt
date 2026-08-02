package com.echo.app.shared.klipy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KlipySearchResponse(
    @SerialName("data") val payload: Payload) {

    @Serializable
    data class Payload(
        val data: List<GifItem>,
        @SerialName("current_page") val currentPage: Int,
        @SerialName("has_next") val hasNext: Boolean)

    @Serializable
    data class GifItem(val file: File)

    @Serializable
    data class File(val md: Resolution)

    @Serializable
    data class Resolution(val gif: MediaUrl)

    @Serializable
    data class MediaUrl(val url: String)
}