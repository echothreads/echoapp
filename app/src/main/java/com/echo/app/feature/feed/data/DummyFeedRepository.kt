package com.echo.app.feature.feed.data

import com.echo.app.feature.feed.domain.PostModel
import kotlin.time.Duration.Companion.minutes
import kotlin.random.Random
import kotlin.time.Clock

class DummyFeedRepository {

    fun getDummyPosts(): List<PostModel> {
        val usernames = listOf("parker545", "tech_lead", "gearhead99", "mayor_of_town", "flutter_fanatic")

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

        return List(50) { index ->
            val randomUser = usernames.random()
            val randomText = dummyContent.random()
            val hasImage = Random.nextBoolean()

            val timeOffset = (index * Random.nextInt(2, 45)).minutes
            val postTimestamp = Clock.System.now().minus(timeOffset)

            PostModel(
                id = "post_$index",
                authorId = "$index",
                authorUsername = randomUser,
                authorProfilePic = "https://randomuser.me/api/portraits/med/men/$index.jpg",
                isVerified = Random.nextBoolean(),
                content = randomText,
                imageUrl = if (hasImage) "https://picsum.photos/seed/$index/800/600" else null,
                timestamp = postTimestamp,
                score = Random.nextInt(10, 5000),
                comments = Random.nextInt(0, 800),
                amplifies = Random.nextInt(0, 200)
            )
        }
    }
}