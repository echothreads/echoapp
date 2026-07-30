package com.echo.app.feature.chat.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.echo.app.R
import com.echo.app.feature.chat.domain.MessageStatus
import com.echo.app.feature.chat.domain.ChatItemModel
import com.echo.app.ui.theme.EchoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(globalPadding: PaddingValues, chats: List<ChatItemModel>) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.nav_chat)) },
                scrollBehavior = scrollBehavior
            )
        }
    ) { localPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(contentPadding = PaddingValues(
                top = localPadding.calculateTopPadding(),
                bottom = globalPadding.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp)) {
                items(chats, key = { chat -> chat.id }) { chat ->
                    ChatItem(chat)
                }
            }
        }
    }
}

@Composable
fun ChatItem(chat: ChatItemModel) {
    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = chat.profileAvatar,
            modifier = Modifier.size(48.dp)
                .clip(CircleShape)
                .background(Color.Black)
                .border(2.dp, shape = CircleShape, color = MaterialTheme.colorScheme.surfaceContainerHighest),
            contentDescription = stringResource(R.string.profile_picture),
            placeholder = painterResource(R.drawable.outline_account_circle),
            error = painterResource(id = R.drawable.outline_account_circle)
        )
        Column(modifier = Modifier.padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.Start) {
            // name and timestamp
            Row() {
                Text(chat.displayName,
                    modifier = Modifier.weight(1f, fill = true),
                    fontWeight = FontWeight.Medium)
                Text(chat.timestamp ?: "")
            }
            // Message
            if (chat.lastMessageIncoming == true) {
                if (chat.lastMessageStatus == MessageStatus.DELIVERED) {
                    Text("@${chat.username}: ${chat.lastMessageText}",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Text("@${chat.username}: ${chat.lastMessageText}",
                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else {
                Row {
                    Text("You: ${chat.lastMessageText}",
                        modifier = Modifier.weight(1f, fill = true),
                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                    Spacer(Modifier.width(8.dp))
                    Text(chat.lastMessageStatus.toString(),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Preview(
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun ChatListScreenPreview() {
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

    EchoTheme {
        Scaffold { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                ChatListScreen(innerPadding, chats = mockChatItems)
            }
        }
    }
}