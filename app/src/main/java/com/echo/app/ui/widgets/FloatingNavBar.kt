package com.echo.app.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.echo.app.R
import com.echo.app.navigation.FeedRoute
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.rememberNavController
import com.echo.app.feature.feed.presentation.FeedScreen
import com.echo.app.navigation.AccountRoute
import com.echo.app.navigation.ChatsRoute
import com.echo.app.navigation.PostRoute
import com.echo.app.navigation.SearchRoute
import com.echo.app.ui.theme.EchoTheme

@Composable
fun FloatingNavigationBar(navController: NavController) {

//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentDestination = navBackStackEntry?.destination
    val test = navController.currentDestination
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 16.dp, vertical = 8.dp) // Pushes it off the edges
            .clip(RoundedCornerShape(24.dp)), // Pill shape
        shadowElevation = 8.dp
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                // home
                NavigationBarItem(
                    icon = {
                        Icon(
                            painterResource(R.drawable.ic_house),
                            contentDescription = "Home"
                        )
                    },
                    label = { Text(stringResource(R.string.nav_home)) },
                    selected = test?.hasRoute<FeedRoute>() ?: false,
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,

                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),

                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    ),
                    onClick = {
                        navController.navigate(FeedRoute) {
                            popUpTo<FeedRoute> { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painterResource(R.drawable.ic_search),
                            contentDescription = "Search"
                        )
                    },
                    label = { Text(stringResource(R.string.nav_search)) },
                    selected = test?.hasRoute<SearchRoute>() ?: false,
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,

                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),

                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    ),
                    onClick = {
                        navController.navigate(SearchRoute) {
                            popUpTo<FeedRoute> { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painterResource(R.drawable.ic_nav_post),
                            contentDescription = "Post"
                        )
                    },
                    label = { Text(stringResource(R.string.nav_post)) },
                    selected = test?.hasRoute<PostRoute>() ?: false,
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,

                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),

                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    ),
                    onClick = {
                        navController.navigate(PostRoute) {
                            popUpTo<FeedRoute> { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painterResource(R.drawable.ic_chat),
                            contentDescription = "Chats"
                        )
                    },
                    label = { Text(stringResource(R.string.nav_chat)) },
                    selected = test?.hasRoute<ChatsRoute>() ?: false,
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,

                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),

                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    ),
                    onClick = {
                        navController.navigate(ChatsRoute) {
                            popUpTo<FeedRoute> { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painterResource(R.drawable.outline_account_circle),
                            modifier = Modifier.size(24.dp),
                            contentDescription = "Account"
                        )
                    },
                    label = { Text(stringResource(R.string.nav_account)) },
                    selected = test?.hasRoute<AccountRoute>() ?: false,
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,

                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),

                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    ),
                    onClick = {
                        navController.navigate(AccountRoute) {
                            popUpTo<FeedRoute> { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
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
fun FloatingNavigationBarExample() {
    val navController = rememberNavController()
    EchoTheme {
        Scaffold(bottomBar = { FloatingNavigationBar(navController) }) { innerPadding ->
            Box(modifier = Modifier
                .padding(bottom = innerPadding.calculateBottomPadding() + 24.dp)) {
                Text("Test for floating nav")
            }
        }
    }
}