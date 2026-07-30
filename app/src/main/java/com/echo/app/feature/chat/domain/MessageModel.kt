package com.echo.app.feature.chat.domain

import androidx.compose.ui.text.toUpperCase
import com.echo.app.Utils
import com.echo.app.feature.chat.data.MessageDto
import java.util.Locale
import kotlin.time.Instant

data class MessageModel(
    val id: String,
    val userId: String,
    val content: String,
    val timestamp: String, // ISO
    val incoming: Boolean,
    val photoUrl: String? = null,
    val status: MessageStatus
) {
    companion object {
        fun fromDto(dto: MessageDto): MessageModel {
            return MessageModel(
                id = dto.id,
                userId = dto.userId,
                content = dto.content,
                timestamp = Utils.getTimeAgo(Instant.parse(dto.timestamp)),
                incoming = dto.incoming,
                photoUrl = dto.photoUrl,
                status = MessageStatus.valueOf(dto.status.uppercase(Locale.ROOT))
            )
        }
    }
}

enum class MessageStatus {
    SENDING,   // Sending the request
    SENT,      // Reached the server
    DELIVERED, // Reached the target
    READ,      // Has been seen by the target
    FAILED     // Error
}