package com.echo.app.feature.chat.domain

import com.echo.app.Utils.getTimeAgo
import com.echo.app.feature.chat.data.ChatItemDto
import com.echo.app.feature.chat.data.MessageDto
import com.echo.app.feature.chat.domain.MessageStatus
import kotlin.time.Clock
import kotlin.time.Instant

data class ChatItemModel(
    val id: String,
    val displayName: String,
    val username: String,
    val profileAvatar: String,

    val lastMessageText: String? = null,
    val lastMessageIncoming: Boolean? = null,
    val lastMessageStatus: MessageStatus? = null,
    val timestamp: String? = null,
    val unreadCount: Int
) {
    companion object {
        fun ChatItemModel.fromDto(dto: ChatItemDto): ChatItemModel {
            return ChatItemModel(
                id = dto.id,
                displayName = dto.displayName,
                username = dto.username,
                profileAvatar = dto.profileAvatar,
                lastMessageText = dto.lastMessage?.content,
                lastMessageIncoming = dto.lastMessage?.incoming,
                lastMessageStatus = dto.lastMessage?.status?.toMessageStatus(),
                timestamp = if (dto.lastMessage != null)
                    getTimeAgo(Instant.parseOrNull(dto.lastMessage.timestamp) ?: Clock.System.now())
                else null,
                unreadCount = dto.unreadCount
            )
        }
        fun String.toMessageStatus(): MessageStatus {
            return try {
                MessageStatus.valueOf(this.uppercase())
            } catch (e: Exception) {
                MessageStatus.SENT
            }
        }
    }
}
