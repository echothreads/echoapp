package com.echo.app.feature.feed.domain

import com.echo.app.feature.feed.data.PostDto
import kotlin.time.Instant

data class PostModel(
    val id: String,
    val username: String,
    val isVerified: Boolean,
    val content: String,
    val imageUrl: String?,
    val timestamp: Instant,
    val score: Int,
    val comments: Int,
    val amplifies: Int
) {
    companion object {
        fun fromDto(dto: PostDto): PostModel {
            return PostModel(
                id = dto.id,
                username = dto.username,
                isVerified = dto.isVerified,
                content = dto.contentText,
                imageUrl = dto.mediaUrl,
                timestamp = Instant.parse(dto.timestamp),
                score = dto.stats.likes + dto.stats.dislikes,
                comments = dto.stats.commentsCount,
                amplifies = dto.stats.repostsCount
            )
        }
    }
}