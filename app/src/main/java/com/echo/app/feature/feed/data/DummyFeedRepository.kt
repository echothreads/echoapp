package com.echo.app.feature.feed.data

import com.echo.app.feature.feed.domain.PostModel
import com.echo.app.feature.profile.presentation.ProfileTabType // Adjust import as needed
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.minutes
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

class DummyFeedRepository {

    suspend fun getProfilePosts(
        userId: String,
        profileUsername: String,
        tabType: ProfileTabType,
        cursor: String?,
        limit: Int = 20
    ): List<PostModel> {
        delay(800.milliseconds)

        // Pagination simulation
        val totalAvailablePosts = 45
        val startIndex = if (cursor == null) 0 else cursor.substringAfterLast("_").toIntOrNull()?.plus(1) ?: 0

        if (startIndex >= totalAvailablePosts) return emptyList()
        val endIndex = minOf(startIndex + limit - 1, totalAvailablePosts - 1)

        val otherUsernames = listOf("tech_lead", "gearhead99", "mayor_of_town", "flutter_fanatic")
        val dummyContent = listOf(
            "Finally got the engine swap done on the Polo. Runs like an absolute dream now.",
            "Anyone else experiencing crazy traffic AI in Cities: Skylines 2 after the new patch? My intersections are completely gridlocked.",
            "Setting up a Fastify server with MongoDB today. Honestly, the routing is so much cleaner than standard Express.",
            "Just spent 3 hours debugging a UI layout only to realize I had a rogue Spacer taking up infinite weight. We love to see it.",
            "BeamNG physics never cease to amaze me. Just spent an hour dropping cars off a cliff for 'research'.",
            "Excited for my new trip, might try some cuisine",
            "Honestly, manual transmissions are just infinitely better. You actually feel connected to the machine.",
            "Can't believe how smooth Jetpack Compose is once you actually understand the state modifiers."
        )

        // 3. Generate the specific batch
        return (startIndex..endIndex).map { index ->
            val randomText = dummyContent.random()
            val timeOffset = (index * Random.nextInt(2, 45)).minutes
            val postTimestamp = Clock.System.now().minus(timeOffset)

            // Determine author based on the tab type
            val isRepost = tabType == ProfileTabType.REPOSTS
            val isReply = tabType == ProfileTabType.REPLIES

            val authorName = if (isRepost) otherUsernames.random() else profileUsername
            val authorId = if (isRepost) "other_user_${Random.nextInt(100)}" else userId
            val authorPic = if (isRepost) "https://randomuser.me/api/portraits/med/men/${Random.nextInt(1, 99)}.jpg"
            else "https://randomuser.me/api/portraits/med/men/50.jpg"

            // Adjust content based on tab Type
            val finalContent = when {
                isReply -> randomText
                isRepost -> randomText
                else -> randomText
            }

            val hasImage = Random.nextBoolean() && !isReply

            PostModel(
                id = "post_$index",
                authorId = authorId,
                authorUsername = authorName,
                authorProfilePic = authorPic,
                isVerified = Random.nextBoolean(),
                content = finalContent,
                imageUrl = if (hasImage) "https://picsum.photos/seed/${index + tabType.ordinal * 100}/800/600" else null,
                timestamp = postTimestamp,
                score = Random.nextInt(10, 5000),
                comments = Random.nextInt(0, 800),
                amplifies = Random.nextInt(0, 200),
                inReplyToPostId = if (isReply) "post_2654" else null,
                inReplyToUsername = if (isReply) otherUsernames.random() else null,
                inReplyToSnippet = if (isReply) dummyContent.random() else null,
                repostedByUsername = if (isRepost) profileUsername else null
            )
        }
    }
}