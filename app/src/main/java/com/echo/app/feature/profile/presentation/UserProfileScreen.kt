package com.echo.app.feature.profile.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.echo.app.R
import com.echo.app.Utils
import com.echo.app.feature.feed.presentation.FeedCard
import com.echo.app.feature.profile.data.ProfileDto
import com.echo.app.ui.theme.EchoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(user: ProfileDto, viewModel: UserProfileScreenViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    var selectedTab by remember { mutableStateOf(ProfileTabType.POSTS) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            totalItems > 0 && lastVisibleItem >= totalItems - 3
        }
    }

    val isBelowHeader by remember {
        derivedStateOf {
            // First item is header so if we're above it, its below
            listState.firstVisibleItemIndex > 0
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadNextPage(selectedTab)
        }
    }

    LaunchedEffect(selectedTab) {
        if (isBelowHeader) {
            listState.scrollToItem(1)
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopAppBar(title = {
            AnimatedVisibility(
                visible = isBelowHeader,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Text(user.displayName)
            }
        },
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(painterResource(R.drawable.arrow_back),
                        contentDescription = stringResource(R.string.back_button),
                        modifier = Modifier.size(24.dp))
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(painterResource(R.drawable.ic_3dots_vertical),
                        contentDescription = stringResource(R.string.options),
                        modifier = Modifier.size(24.dp))
                }
            }
        )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            item {
                ProfileHeader(user)
            }

            stickyHeader {
                ContentTabSelector(selectedTabIndex = selectedTab.ordinal,
                    onTabSelected = {newSelected ->
                        val newTab = ProfileTabType.entries[newSelected]
                        if (selectedTab != newTab) {
                            selectedTab = newTab
                            viewModel.switchTab(newTab)
                        }
                    }
                )
            }

            items(state.posts, key = { post -> post.id }) { post ->
                Box(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    FeedCard(post = post)
                }
            }

            item {
                when {
                    // Initial Loading: Empty list + Loading -> Full Screen Spinner
                    state.posts.isEmpty() && state.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth().fillParentMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    // No Posts: Empty list + Not Loading -> Full Screen Text
                    state.posts.isEmpty() && !state.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth().fillParentMaxHeight(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Text(
                                text = "No posts yet.",
                                modifier = Modifier.padding(32.dp)
                            )
                        }
                    }

                    // Pagination Loading: Has Posts + Loading -> Small Bottom Spinner
                    state.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    // End of Feed: Has Posts + Reached End -> Small Bottom Text
                    state.isEndOfFeed -> {
                        Text(
                            text = "No more posts to show.",
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileBanner(banner: String?, pfp: String?) {
    Box(Modifier.fillMaxWidth().height(208.dp),
        contentAlignment = Alignment.TopStart) {
        if(banner != null) {
            AsyncImage(
                model = banner,
                contentDescription = stringResource(R.string.profile_banner),
                modifier = Modifier.fillMaxWidth().height(160.dp)
            )
        } else {
            Box(modifier = Modifier.fillMaxWidth().height(160.dp).background(color = Color.White))
        }
        AsyncImage(model = pfp,
            contentDescription = stringResource(R.string.profile_picture),
            error = painterResource(R.drawable.outline_account_circle),
            modifier = Modifier.align(Alignment.BottomStart)
                .padding(start = 16.dp)
                .size(96.dp)
                .clip(CircleShape)
                .background(Color.Black)
                .border(width = 2.dp, color = MaterialTheme.colorScheme.surfaceContainerHighest, shape = CircleShape)
        )
    }
}
@Composable
fun ProfileHeader(user: ProfileDto) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        ProfileBanner(user.profileBanner, user.profileAvatar)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f, fill = true)) {
                // Display name
                Text(
                    user.displayName,
                    maxLines = 1,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )

                // Username
                Text(
                    "@" + user.username,
                    maxLines = 1,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            TextButton(onClick = {},
                shape = RoundedCornerShape(50),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceContainerHighest)
            ) {
                Text(stringResource(R.string.message),
                    modifier = Modifier.padding(4.dp), // the 2dp border goes inside
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp)
            }

            if (!user.followed) {
                TextButton(onClick = {},
                    modifier = Modifier.padding(start = 8.dp, end = 12.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.textButtonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(stringResource(R.string.follow),
                        modifier = Modifier.padding(4.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                }
            } else {
                TextButton(onClick = {},
                    modifier = Modifier.padding(start = 8.dp, end = 16.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.textButtonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(stringResource(R.string.unfollow),
                        modifier = Modifier.padding(4.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                }
            }
        }
        // Bio, needs to be limited to 6 lines serverside
        Text(user.bio,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            maxLines = 6,
            overflow = TextOverflow.Ellipsis)

        // Followed by people you follow card, needs to be dynamically added and removed
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)) {
                Box(contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.padding(end = 8.dp)) {
                    AsyncImage(model = user.profileAvatar,
                        contentDescription = stringResource(R.string.profile_picture),
                        error = painterResource(R.drawable.outline_account_circle),
                        modifier = Modifier.size(24.dp)
                            .clip(CircleShape)
                            .background(Color.Black)
                            //.border(width = 2.dp, color = MaterialTheme.colorScheme.surfaceContainer, shape = CircleShape)
                    )
                    AsyncImage(model = user.profileAvatar,
                        contentDescription = stringResource(R.string.profile_picture),
                        error = painterResource(R.drawable.outline_account_circle),
                        modifier = Modifier
                            .padding(start = 8.dp)

                            // 2. Total Size = 24dp (image) + 2dp (left border) + 2dp (right border)
                            .size(28.dp)

                            .border(
                                width = 2.2.dp,
                                color = MaterialTheme.colorScheme.surfaceContainer,
                                shape = CircleShape
                            )

                            .padding(2.dp)

                            .clip(CircleShape)
                            .background(Color.Black)
                    )
                }
                FollowedByText("MrBeast", "PewDiePie", 65) { }
            }
        }
        //Followed by and Following
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp)) {
            Text(
                buildAnnotatedString {
                    val numStr = Utils.formatNumber(user.followers)
                    val str = stringResource(R.string.followers, numStr)
                    val numIndex = str.indexOf(numStr)
                    if (numIndex >= 0) {
                        addStyle(SpanStyle(fontWeight = FontWeight.Bold),
                            start = numIndex,
                            end = numStr.length + numIndex
                        )
                    }
                    append(str)
                }
            )
            Text(
                buildAnnotatedString {
                    val numStr = Utils.formatNumber(user.following)
                    val str = stringResource(R.string.following, numStr)
                    val numIndex = str.indexOf(numStr)
                    if (numIndex >= 0) {
                        addStyle(SpanStyle(fontWeight = FontWeight.Bold),
                            start = numIndex,
                            end = numStr.length + numIndex
                        )
                    }
                    append(str)
                }
            )
        }
        // End
    }
}

@Composable
fun ContentTabSelector(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf(
        R.string.posts,
        R.string.replies,
        R.string.amplifies
    )
    SecondaryTabRow(
        selectedTabIndex = selectedTabIndex
    ) {
        tabs.forEachIndexed { index, stringRes ->
            val isSelected = selectedTabIndex == index

            Tab(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(
                    text = stringResource(stringRes),
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Unspecified
                    },
                    fontWeight = if (isSelected) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Medium
                    },
                    letterSpacing = if (!isSelected) 0.5.sp else 0.8.sp
                )
            }
        }
    }
}

@Preview(
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun UserProfileScreenPreview() {
    val bio = "Senior AI Architect & Prompt Systems Strategist | Pioneering autonomous software engineering by replacing traditional development lifecycles with enterprise-grade Claude Sonnet orchestration |"
    val user = ProfileDto(
        id = "231",
        profileAvatar = "https://images-ext-1.discordapp.net/external/bO05h5jmNLk9leh1S_TBmrHLdYkIB2ASA3PvOIZFto4/%3Fsize%3D4096/https/cdn.discordapp.com/avatars/485486853063966742/e3cbf4a91c990bbc3b1eae53a7dee911.png?format=webp&quality=lossless&width=1020&height=1020",
        profileBanner = null,
        username = "ender1324",
        displayName = "Pavel Petrov",
        isVerified = true,
        bio = bio,
        followers = 49302,
        following = 5,
        followed = true
    )
    EchoTheme {
        UserProfileScreen(user)
    }
}