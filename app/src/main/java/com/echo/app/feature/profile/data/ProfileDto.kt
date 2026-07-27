package com.echo.app.feature.profile.data

data class ProfileDto(
    val id: String,
    val profileAvatar: String?,
    val profileBanner: String?,
    val username: String,
    val displayName: String,
    val isVerified: Boolean,
    val bio: String,
    val followers: Long,
    val following: Long,
    val followed: Boolean
) {

}