package com.echo.app.feature.profile.domain

import com.echo.app.feature.profile.data.ProfileDto

data class ProfileModel(
    val id: String,
    val profileAvatar: String?,
    val profileBanner: String?,
    val username: String,
    val displayName: String,
    val isVerified: Boolean,
    val bio: String,
    val followers: Long,
    val following: Long,
    val followed: Boolean,
    val lastSeenTimestamp: Long? = null,
    val isOnline: Boolean? = null
) {
    companion object {
        fun fromDto(dto: ProfileDto): ProfileModel {
            return ProfileModel(
                id = dto.id,
                profileAvatar = dto.profileAvatar,
                profileBanner = dto.profileBanner,
                username = dto.username,
                displayName = dto.displayName,
                isVerified = dto.isVerified,
                bio = dto.bio,
                followers = dto.followers,
                following = dto.following,
                followed = dto.followed,
                lastSeenTimestamp = dto.lastSeenTimestamp,
                isOnline = dto.isOnline
            )
        }
    }
}