package com.echo.app.feature.feed.data

data class PostDto(
    val id: String,
    val username: String,
    val isVerified: Boolean,
    val contentText: String,
    val mediaUrl: String?,
    val timestamp: String,
    val stats: PostStatsDto
)

data class PostStatsDto(
    val likes: Int,
    val dislikes: Int,
    val commentsCount: Int,
    val repostsCount: Int
)