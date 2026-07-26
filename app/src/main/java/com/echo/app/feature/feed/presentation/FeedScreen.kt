package com.echo.app.feature.feed.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.echo.app.feature.feed.data.DummyFeedRepository
import com.echo.app.feature.feed.domain.PostModel
import com.echo.app.ui.theme.EchoTheme

@Composable
fun FeedScreen(globalPadding: PaddingValues) {
    val posts = DummyFeedRepository().getDummyPosts()
    Scaffold() { localPadding ->
        FeedScroller(posts,
            contentPadding = PaddingValues(
                top = localPadding.calculateTopPadding(),
                bottom = globalPadding.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp
            ))
    }
}

@Composable
fun FeedScroller(posts: List<PostModel>, contentPadding: PaddingValues = PaddingValues(0.dp)) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(posts, key = { post -> post.id }){ post ->
            FeedCard(
                username = post.username,
                timestamp = post.timestamp,
                postContent = post.content,
                postImage = post.imageUrl,
                score = post.score,
                comments = post.comments,
                amplifies = post.amplifies,
                isVerified = post.isVerified
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun FeedScreenPreview() {
    EchoTheme() {
        Scaffold() { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                FeedScreen(innerPadding)
            }
        }
    }
}