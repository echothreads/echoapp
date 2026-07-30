package com.echo.app.feature.chat.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echo.app.R
import com.echo.app.feature.chat.domain.MessageModel
import com.echo.app.feature.chat.domain.MessageStatus
import com.echo.app.ui.theme.EchoTheme

@Composable
fun MessageBubble(message: MessageModel) {
    // SMART HIDING AND SHOWING BS
    // State to hold whether we should show the timestamp
    var showTimestamp by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val minWidthThresholdPx = with(density) { 60.dp.toPx() }

    // Align right if it's from user, left if it's from other
    val alignment = if (message.incoming) Alignment.CenterStart else Alignment.CenterEnd
    val backgroundColor = if (message.incoming) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary
    val textColor = if (message.incoming) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Surface(
            color = backgroundColor,
            shape = RoundedCornerShape(
                topStart = if (message.incoming) 4.dp else 16.dp,
                topEnd = if (message.incoming) 16.dp else 4.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp,
            ),
            tonalElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(message.content,
                        color = textColor,
                        onTextLayout = { textLayoutResult ->
                            val isWide = textLayoutResult.size.width > minWidthThresholdPx
                            if (showTimestamp != isWide) {
                                showTimestamp = isWide
                            }
                        }
                    )

                    // User message, short message, inline status
                    if (!showTimestamp && !message.incoming) {
                        Spacer(modifier = Modifier.width(4.dp))
                        MessageStatusIcon(message.status, textColor)
                    }
                }

                // Any message, long message, timestamp on new line
                if (showTimestamp) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = message.timestamp,
                            fontSize = 11.sp,
                            color = textColor.copy(alpha = 0.7f)
                        )
                        if (!message.incoming) {
                            Spacer(modifier = Modifier.width(4.dp))
                            MessageStatusIcon(message.status, textColor)
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun MessageStatusIcon(messageStatus: MessageStatus, textColor: Color) {
    val iconModifier = Modifier.padding(start = 8.dp, end = 4.dp).size(16.dp)
    when (messageStatus) {
        MessageStatus.SENDING ->
            Icon(painterResource(R.drawable.ic_sending),
                modifier = iconModifier,
                contentDescription = stringResource(R.string.sending),
                tint = textColor
            )

        MessageStatus.SENT ->
            Icon(painterResource(R.drawable.ic_delivered),
                modifier = iconModifier,
                contentDescription = stringResource(R.string.sent),
                tint = textColor
            )

        MessageStatus.DELIVERED ->
            Icon(painterResource(R.drawable.ic_delivered),
                modifier = iconModifier,
                contentDescription = stringResource(R.string.delivered),
                tint = textColor
            )

        MessageStatus.READ ->
            Icon(painterResource(R.drawable.ic_eye),
                modifier = iconModifier,
                contentDescription = stringResource(R.string.read),
                tint = textColor
            )

        else ->
            Icon(painterResource(R.drawable.ic_chat_error),
                modifier = iconModifier,
                contentDescription = stringResource(R.string.chat_error),
                tint = MaterialTheme.colorScheme.error
            )
    }
}

@Preview(showBackground = true)
@Composable
fun MessageBubblePreview() {
    EchoTheme {
        Column {
            MessageBubble(
                message = MessageModel(
                    id = "1",
                    userId = "user1",
                    content = "Hey!",
                    timestamp = "10:00 AM",
                    incoming = true,
                    status = MessageStatus.SENT
                )
            )
            MessageBubble(
                message = MessageModel(
                    id = "2",
                    userId = "me",
                    content = "It's going well! Just added some previews.",
                    timestamp = "10:01 AM",
                    incoming = false,
                    status = MessageStatus.SENDING
                )
            )
        }
    }
}