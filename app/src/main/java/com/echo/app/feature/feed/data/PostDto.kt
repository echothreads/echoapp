package com.echo.app.feature.feed.data

data class PostDto(
    val id: String,
    val contentText: String,
    val mediaUrl: String?,
    val timestamp: String,
    val stats: PostStatsDto,

    val authorId: String,
    val authorUsername: String,
    val authorProfilePic: String?,
    val isVerified: Boolean,

    // is reply?
    val inReplyToPostId: String? = null,
    val inReplyToUsername: String? = null,
    val inReplyToSnippet: String? = null,

    // is it a repost?
    val repostedByUsername: String? = null,
)

data class PostStatsDto(
    val likes: Int,
    val dislikes: Int,
    val commentsCount: Int,
    val repostsCount: Int
)