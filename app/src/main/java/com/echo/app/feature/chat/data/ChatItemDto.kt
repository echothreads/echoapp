package com.echo.app.feature.chat.data

import kotlinx.serialization.Serializable

@Serializable
data class ChatItemDto(
    val id: String,
    val displayName: String,
    val username: String,
    val profileAvatar: String,

    val lastMessage: MessageDto? = null,
    val unreadCount: Int = 0
)