package com.echo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.echo.app.feature.auth.LoginView
import com.echo.app.feature.feed.data.DummyFeedRepository
import com.echo.app.feature.feed.domain.PostModel
import com.echo.app.ui.theme.EchoTheme
import com.echo.app.feature.feed.presentation.FeedScreen
import com.echo.app.navigation.AccountRoute
import com.echo.app.navigation.ChatsRoute
import com.echo.app.navigation.FeedRoute
import com.echo.app.navigation.MainGraph
import com.echo.app.navigation.PostRoute
import com.echo.app.navigation.SearchRoute
import com.echo.app.feature.feed.presentation.FeedCard
import com.echo.app.feature.profile.presentation.ProfileTabType
import com.echo.app.feature.profile.presentation.UserProfileScreen
import com.echo.app.feature.profile.presentation.UserProfileScreenPreview
import com.echo.app.ui.widgets.FloatingNavigationBar
import kotlin.time.Instant

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            var posts by remember { mutableStateOf<List<PostModel>>(emptyList()) }

            LaunchedEffect(Unit) {
                posts = DummyFeedRepository().getProfilePosts(
                    userId = "test",
                    profileUsername = "test",
                    tabType = ProfileTabType.POSTS,
                    cursor = null
                )
            }

            EchoTheme {
                Scaffold(bottomBar = { FloatingNavigationBar(navController) }) { globalPadding ->
                    NavHost(
                        navController,
                        startDestination = MainGraph,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        navigation<MainGraph>(startDestination = FeedRoute) {
                            composable<FeedRoute> {
                                FeedScreen(globalPadding, posts)
                            }

                            composable<SearchRoute> {
                                UserProfileScreenPreview()
                            }

                            composable<PostRoute> {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Text("New Post")
                                }
                            }

                            composable<ChatsRoute> {
                                Box() {
                                    Text("TEST")
                                }
                            }

                            composable<AccountRoute> {
                                Box() {
                                    Text("TEST")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}