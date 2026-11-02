package com.echo.app.shared.data.klipy

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
    data class GifItem(
        val slug: String,
        val title: String,
        val file: File
    )

    @Serializable
    data class File(val md: Resolution)

    @Serializable
    data class Resolution(val gif: GifInfo)

    @Serializable
    data class GifInfo(
        val url: String,
        val width: Int,
        val height: Int,
        val size: Int
    )
}