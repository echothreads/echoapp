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
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.echo.app.feature.auth.LoginView
import com.echo.app.ui.theme.EchoTheme
import com.echo.app.feature.feed.presentation.FeedScreen
import com.echo.app.navigation.AccountRoute
import com.echo.app.navigation.ChatsRoute
import com.echo.app.navigation.FeedRoute
import com.echo.app.navigation.MainGraph
import com.echo.app.navigation.PostRoute
import com.echo.app.navigation.SearchRoute
import com.echo.app.feature.feed.presentation.FeedCard
import com.echo.app.ui.widgets.FloatingNavigationBar
import kotlin.time.Instant

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            EchoTheme {
                Scaffold(bottomBar = { FloatingNavigationBar(navController) }) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = MainGraph,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        navigation<MainGraph>(startDestination = FeedRoute) {
                            composable<FeedRoute> {
                                FeedScreen()
                            }

                            composable<SearchRoute> {
                                LoginView()
                            }

                            composable<PostRoute> {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    FeedCard("ender1324", Instant.fromEpochSeconds(1784901564), "Excited for my new trip to tel aviv, might try some cuisine", null, 60, 850, 6000, true)
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