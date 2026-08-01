package com.echo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.echo.app.feature.chat.domain.ChatItemModel
import com.echo.app.feature.chat.domain.MessageStatus
import com.echo.app.feature.chat.presentation.ChatListScreen
import com.echo.app.feature.feed.data.DummyFeedRepository
import com.echo.app.feature.feed.domain.PostModel
import com.echo.app.ui.theme.EchoTheme
import com.echo.app.feature.feed.presentation.FeedScreen
import com.echo.app.feature.post.presentation.CreatePostScreen
import com.echo.app.navigation.AccountRoute
import com.echo.app.navigation.ChatsRoute
import com.echo.app.navigation.FeedRoute
import com.echo.app.navigation.MainGraph
import com.echo.app.navigation.PostRoute
import com.echo.app.navigation.SearchRoute
import com.echo.app.feature.profile.presentation.ProfileTabType
import com.echo.app.feature.profile.presentation.UserProfileScreenPreview
import com.echo.app.ui.widgets.FloatingNavigationBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            var posts by remember { mutableStateOf<List<PostModel>>(emptyList()) }
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination
            val isBottomBarRoute = currentRoute?.hierarchy?.any { route ->
                route.hasRoute<MainGraph>()
            }

            LaunchedEffect(Unit) {
                posts = DummyFeedRepository().getProfilePosts(
                    userId = "test",
                    profileUsername = "test",
                    tabType = ProfileTabType.POSTS,
                    cursor = null
                )
            }

            EchoTheme {
                Scaffold(bottomBar = {  if (isBottomBarRoute == true) FloatingNavigationBar(navController) }) { globalPadding ->
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

                            composable<ChatsRoute> {
                                val mockChatItems = listOf(
                                    ChatItemModel(
                                        id = "1",
                                        displayName = "Alice Smith",
                                        username = "alice_s",
                                        profileAvatar = "https://i.pravatar.cc/150?u=1",
                                        lastMessageText = "See you tomorrow!",
                                        lastMessageIncoming = false,
                                        lastMessageStatus = MessageStatus.READ,
                                        timestamp = "10:30 AM",
                                        unreadCount = 2
                                    ),
                                    ChatItemModel(
                                        id = "2",
                                        displayName = "Bob Jones",
                                        username = "bjones",
                                        profileAvatar = "https://i.pravatar.cc/150?u=2",
                                        lastMessageText = "Did you check the new file?",
                                        lastMessageIncoming = true,
                                        lastMessageStatus = MessageStatus.DELIVERED,
                                        timestamp = "9:45 AM",
                                        unreadCount = 0
                                    ),
                                    ChatItemModel(
                                        id = "3",
                                        displayName = "Charlie Brown",
                                        username = "goodolcharlie",
                                        profileAvatar = "https://i.pravatar.cc/150?u=3",
                                        lastMessageText = "Lunch at 12?",
                                        lastMessageIncoming = true,
                                        lastMessageStatus = MessageStatus.READ,
                                        timestamp = "Yesterday",
                                        unreadCount = 1
                                    ),
                                    ChatItemModel(
                                        id = "4",
                                        displayName = "Diana Prince",
                                        username = "wonder_d",
                                        profileAvatar = "https://i.pravatar.cc/150?u=4",
                                        lastMessageText = "The meeting was moved to Friday.",
                                        lastMessageIncoming = false,
                                        lastMessageStatus = MessageStatus.DELIVERED,
                                        timestamp = "Monday",
                                        unreadCount = 0
                                    ),
                                    ChatItemModel(
                                        id = "5",
                                        displayName = "Ethan Hunt",
                                        username = "mission_impossible",
                                        profileAvatar = "https://i.pravatar.cc/150?u=5",
                                        lastMessageText = "This message will self-destruct.",
                                        lastMessageIncoming = true,
                                        lastMessageStatus = MessageStatus.READ,
                                        timestamp = "1 min ago",
                                        unreadCount = 5
                                    )
                                )
                                ChatListScreen(globalPadding, mockChatItems)
                            }

                            composable<AccountRoute> {
                                Box() {
                                    Text("TEST")
                                }
                            }
                        }

                        composable<PostRoute> {
                            CreatePostScreen("https://i.pravatar.cc/150?u=1",
                                { navController.popBackStack()}, {})
                        }
                    }
                }
            }
        }
    }
}