package com.echo.app.feature.chat.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.echo.app.feature.chat.domain.ChatItemModel
import com.echo.app.feature.chat.domain.MessageModel
import com.echo.app.feature.chat.domain.MessageStatus
import kotlin.collections.reversed
import com.echo.app.R
import com.echo.app.feature.profile.data.ProfileDto
import com.echo.app.ui.theme.EchoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chat: ChatItemModel) {
    val user = ProfileDto(
        id = "user_123",
        //profileAvatar = "https://i.pravatar.cc/150?u=john",
        profileAvatar = "https://media1.giphy.com/media/v1.Y2lkPTZjMDliOTUyMXA2ejByZmdzYm9idXR6MTh1Zzd6NWljNXE4MzdrcjBpMG5jcGNpOSZlcD12MV9naWZzX3NlYXJjaCZjdD1n/0j9Xe1SYRikDqhMRgb/200w.gif",
        profileBanner = "https://picsum.photos/seed/picsum/600/200",
        username = "johndoe",
        displayName = "John Doe",
        isVerified = true,
        bio = "Android Developer & Tech Enthusiast. Always building something new.",
        followers = 1250,
        following = 450,
        followed = false,
        lastSeenTimestamp = 1715856000L,
        isOnline = false
    )
    var messageText by remember { mutableStateOf("") }
    val messages = remember {
        listOf(
            MessageModel(
                id = "1",
                userId = "12",
                content = "Yo",
                timestamp = "5h ago",
                incoming = true,
                photoUrl = null,
                status = MessageStatus.READ
            ),
            MessageModel(
                id = "2",
                userId = "me",
                content = "Fuck you want",
                timestamp = "4h ago",
                incoming = false,
                photoUrl = null,
                status = MessageStatus.READ
            ),
            MessageModel(
                id = "3",
                userId = "12",
                content = "cash app me some $$$",
                timestamp = "3h ago",
                incoming = true,
                photoUrl = "https://cdn.pixabay.com/photo/2021/12/12/20/00/play-6865967_1280.jpg",
                status = MessageStatus.READ
            ),
            MessageModel(
                id = "4",
                userId = "me",
                content = "what the fuck?",
                timestamp = "2h ago",
                incoming = false,
                photoUrl = null,
                status = MessageStatus.READ
            ),
            MessageModel(
                id = "5",
                userId = "12",
                content = "$$$ quick i need",
                timestamp = "1h ago",
                incoming = true,
                photoUrl = null,
                status = MessageStatus.DELIVERED
            ),
            MessageModel(
                id = "6",
                userId = "12",
                content = "cmon man just 5$",
                timestamp = "1m ago",
                incoming = true,
                photoUrl = null,
                status = MessageStatus.DELIVERED
            ),
            MessageModel(
                id = "7",
                userId = "me",
                content = "ni",
                timestamp = "Now",
                incoming = false,
                photoUrl = null,
                status = MessageStatus.DELIVERED
            ),
            MessageModel(
                id = "8",
                userId = "me",
                content = "no",
                timestamp = "Now",
                incoming = false,
                photoUrl = null,
                status = MessageStatus.DELIVERED
            )
        ).reversed()
    }

    var isTyping = true // TODO: temporary

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = chat.profileAvatar,
                            modifier = Modifier.padding(horizontal = 8.dp).size(48.dp)
                                .clip(CircleShape)
                                .background(Color.Black)
                                .border(2.dp, shape = CircleShape, color = MaterialTheme.colorScheme.surfaceContainerHighest),
                            contentDescription = stringResource(R.string.profile_picture),
                            placeholder = painterResource(R.drawable.outline_account_circle),
                            error = painterResource(id = R.drawable.outline_account_circle)
                        )
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(user.displayName)
                            Row(modifier = Modifier.padding(top = 2.dp),
                                verticalAlignment = Alignment.CenterVertically) {
                                when {
                                    user.isOnline == true -> {
                                        Icon(painterResource(R.drawable.ic_dot),
                                            contentDescription = stringResource(R.string.status_dot),
                                            modifier = Modifier.size(8.dp), tint = Color.Green
                                        )
                                        Text(stringResource(R.string.online),
                                            fontSize = 12.sp,
                                            lineHeight = 1.sp,
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        )
                                    }
                                    isTyping -> {
                                        TypingIndicator(Modifier.size(24.dp), 4.dp, MaterialTheme.colorScheme.primary)
                                        Text(stringResource(R.string.typing),
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 12.sp,
                                            lineHeight = 1.sp,
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        )
                                    }
                                    else -> {
                                        Icon(painterResource(R.drawable.ic_dot),
                                            contentDescription = stringResource(R.string.status_dot),
                                            modifier = Modifier.size(8.dp), tint = Color.Gray
                                        )
                                        Text(stringResource(R.string.offline),
                                            fontSize = 12.sp,
                                            lineHeight = 1.sp,
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Go back */ }) {
                        Icon(painterResource(R.drawable.arrow_back),
                            contentDescription = stringResource(R.string.back_button),
                            modifier = Modifier.size(24.dp))
                    }
                },
                actions = {
                    IconButton(onClick = { /* options */ }) {
                        Icon(painterResource(R.drawable.ic_3dots_vertical),
                            contentDescription = stringResource(R.string.options),
                            modifier = Modifier.size(24.dp))
                    }
                }
            )
        },
        bottomBar = {
            ChatInputBar(
                text = messageText,
                onTextChanged = { messageText = it },
                onAddClicked = {
                    // TODO
                },
                onSendClicked = {
                    // TODO: Send message
                    messageText = ""
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            // flips so newest message appears last
            reverseLayout = true,
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            itemsIndexed(items = messages, key = { _, message -> message.id }) { index, message ->
                val isLastInGroup = if (index == 0) {
                    true
                } else {
                    val previousChat = messages[index - 1]
                    message.incoming != previousChat.incoming
                }

                val isFirstInGroup = if (index == messages.lastIndex) {
                    true
                } else {
                    val messageAbove = messages[index + 1]
                    message.incoming != messageAbove.incoming
                }

                Row(modifier = Modifier.padding(start = 8.dp)) {
                    if (isFirstInGroup && message.incoming) {
                        AsyncImage(
                            model = chat.profileAvatar,
                            modifier = Modifier.padding(top = 8.dp).size(32.dp)
                                .clip(CircleShape)
                                .background(Color.Black)
                                .border(2.dp, shape = CircleShape, color = MaterialTheme.colorScheme.surfaceContainerHighest),
                            contentDescription = stringResource(R.string.profile_picture),
                            placeholder = painterResource(R.drawable.outline_account_circle),
                            error = painterResource(id = R.drawable.outline_account_circle)
                        )
                    } else {
                        Spacer(modifier = Modifier.size(32.dp))
                    }
                    val chatBoxPadding = if (message.incoming)
                        PaddingValues(end = 24.dp)
                    else
                        PaddingValues(start = 24.dp)
                    val chatBoxAlignment = if (message.incoming)
                        Alignment.End
                    else Alignment.Start
                    Column(
                        modifier = Modifier.padding(chatBoxPadding),
                        horizontalAlignment = chatBoxAlignment
                    ) {
                        when {
                            isFirstInGroup && message.incoming -> {
                                Text(chat.username,
                                    modifier = Modifier.padding(start = 6.dp, top = 8.dp).align(Alignment.Start),
                                    color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                                )
                            }
                            isFirstInGroup && !message.incoming -> {
                                Text("You",
                                    modifier = Modifier.padding(end = 6.dp, top = 8.dp).align(Alignment.End),
                                    color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                                )
                            }
                            else -> Unit
                        }
                        MessageBubble(message = message)
                    }
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
fun ChatScreenPreview() {
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
        ChatScreen(mockChatItems[1])
    }
}