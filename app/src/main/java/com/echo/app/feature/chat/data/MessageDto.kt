package com.echo.app.feature.chat.data

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: String,
    val userId: String,
    val content: String,
    val timestamp: String, // ISO
    val incoming: Boolean,
    val photoUrl: String? = null,
    val status: String
)