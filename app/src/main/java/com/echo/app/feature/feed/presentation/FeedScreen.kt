package com.echo.app.feature.feed.presentation

import android.R.attr.scaleX
import android.R.attr.scaleY
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echo.app.feature.feed.data.DummyFeedRepository
import com.echo.app.feature.feed.domain.PostModel
import com.echo.app.ui.theme.EchoTheme
import kotlin.time.Clock
import com.echo.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(globalPadding: PaddingValues, posts: List<PostModel>) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.nav_home)) },
                scrollBehavior = scrollBehavior
            )
        }
    ) { localPadding ->
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
            FeedCard(post)
        }
    }
}

@Preview(
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun FeedScreenPreview() {

    val previewPosts = List(3) { index ->
        PostModel(
            id = "preview_$index",
            authorId = "user1",
            authorUsername = "test_user",
            authorProfilePic = "https://randomuser.me/api/portraits/med/men/1.jpg",
            isVerified = true,
            content = "This is a preview post number $index",
            imageUrl = null,
            timestamp = Clock.System.now(),
            score = 100,
            comments = 10,
            amplifies = 5
        )
    }

    EchoTheme() {
        Scaffold() { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                FeedScreen(innerPadding, previewPosts)
            }
        }
    }
}