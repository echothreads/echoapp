package com.echo.app.feature.search.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echo.app.R
import com.echo.app.feature.profile.domain.ProfileModel
import com.echo.app.feature.search.domain.TrendModel
import com.echo.app.ui.theme.EchoTheme
import com.echo.app.ui.widgets.UserRow

@Composable
fun SearchScreen(globalPadding: PaddingValues) {
    SearchScreenContent(globalPadding)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    globalPadding: PaddingValues,
    suggestedProfiles: List<ProfileModel> = emptyList(),
    suggestedTrends: List<TrendModel> = emptyList(),
    onFollow: (Boolean) -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    val searchBarState = rememberSearchBarState()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.nav_search)) },
                scrollBehavior = scrollBehavior
            )
        }
    ) { localPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(localPadding)
                .verticalScroll(rememberScrollState())
        ) {
            SearchBar(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 8.dp),
                state = searchBarState,
                colors = SearchBarDefaults.colors(),
                inputField = {
                    SearchBarDefaults.InputField(
                        textFieldState = rememberTextFieldState(),
                        searchBarState = searchBarState,
                        onSearch = { /* perform search */ },
                        placeholder = { Text(stringResource(R.string.nav_search)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_search),
                                contentDescription = null
                            )
                        }
                    )
                }
            )
            RecommendedProfiles(profiles = suggestedProfiles, onFollow = onFollow)
            TrendsGrid(suggestedTrends)
        }
    }
}

@Composable
fun RecommendedProfiles(profiles: List<ProfileModel>,
                        onFollow: (Boolean) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(stringResource(R.string.suggested_for_you),
            modifier = Modifier.align(Alignment.Start),
            fontSize = 18.sp, fontWeight = FontWeight.Bold)
        profiles.forEach {
            UserRow(
                displayName = it.displayName,
                username = it.username,
                profileAvatar = it.profileAvatar,
                followed = it.followed,
                onFollow = onFollow
            )
        }
    }
}

@Composable
fun TrendsGrid(trends: List<TrendModel>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(stringResource(R.string.trending_posts),
            modifier = Modifier.align(Alignment.Start),
            fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            trends.chunked(3).forEach { chunk ->
                TrendingCard(chunk[0])

                if (chunk.size > 1) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        TrendingCard(chunk[1], Modifier.weight(1f))
                        if (chunk.size > 2) TrendingCard(chunk[2], Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Preview(
    showSystemUi = true
)
@Preview(
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun SearchScreenPreview() {
    val mockSuggestedProfiles = listOf(
        ProfileModel(
            id = "1",
            profileAvatar = "https://i.pravatar.cc/150?u=10",
            profileBanner = null,
            username = "alexrivera",
            displayName = "Alex Rivera",
            isVerified = true,
            bio = "Mobile engineer & open source enthusiast. Building cool things with Kotlin & Jetpack Compose.",
            followers = 12400,
            following = 412,
            followed = false
        ),
        ProfileModel(
            id = "2",
            profileAvatar = "https://i.pravatar.cc/150?u=25",
            profileBanner = null,
            username = "sarah_design",
            displayName = "Sarah Chen",
            isVerified = true,
            bio = "Product Designer @Echo. Crafting delightful user experiences and design systems.",
            followers = 8950,
            following = 210,
            followed = true
        ),
        ProfileModel(
            id = "3",
            profileAvatar = "https://i.pravatar.cc/150?u=33",
            profileBanner = null,
            username = "marcus_dev",
            displayName = "Marcus Vance",
            isVerified = false,
            bio = "Android enthusiast, writer, and coffee lover ☕",
            followers = 3420,
            following = 589,
            followed = false
        )
    )

    val mockSuggestedTrends = listOf(
        TrendModel(
            name = "Android16",
            echoes = 142500,
            ranking = 1,
            imageUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=800&auto=format&fit=crop&q=60"
        ),
        TrendModel(
            name = "ComposeMultiplatform",
            echoes = 89300,
            ranking = 2,
            imageUrl = "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=800&auto=format&fit=crop&q=60"
        ),
        TrendModel(
            name = "TechSummit2026",
            echoes = 45200,
            ranking = 3,
            imageUrl = "https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=800&auto=format&fit=crop&q=60"
        )
    )

    EchoTheme {
        SearchScreenContent(
            globalPadding = PaddingValues(16.dp),
            suggestedProfiles = mockSuggestedProfiles,
            suggestedTrends = mockSuggestedTrends
        )
    }
}