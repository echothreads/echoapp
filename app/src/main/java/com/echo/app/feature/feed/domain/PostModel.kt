package com.echo.app.feature.feed.domain

import com.echo.app.feature.feed.data.PostDto
import kotlin.time.Instant

data class PostModel(
    val id: String,
    val authorId: String,
    val authorUsername: String,
    val authorProfilePic: String?,
    val isVerified: Boolean,
    val content: String,
    val imageUrl: String?,
    val timestamp: Instant,
    val score: Int,
    val comments: Int,
    val amplifies: Int,
    // is reply?
    val inReplyToPostId: String? = null,
    val inReplyToUsername: String? = null,
    val inReplyToSnippet: String? = null,

    // is it a repost?
    val repostedByUsername: String? = null,
) {
    companion object {
        fun fromDto(dto: PostDto): PostModel {
            return PostModel(
                id = dto.id,
                authorId = dto.authorId,
                authorUsername = dto.authorUsername,
                authorProfilePic = dto.authorProfilePic,
                isVerified = dto.isVerified,
                content = dto.contentText,
                imageUrl = dto.mediaUrl,
                timestamp = Instant.parse(dto.timestamp),
                score = dto.stats.likes + dto.stats.dislikes,
                comments = dto.stats.commentsCount,
                amplifies = dto.stats.repostsCount,

                inReplyToPostId = dto.inReplyToPostId,
                inReplyToUsername = dto.inReplyToUsername,
                inReplyToSnippet = dto.inReplyToSnippet,
                repostedByUsername = dto.repostedByUsername
            )
        }
    }
}