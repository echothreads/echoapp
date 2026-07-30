package com.echo.app.feature.chat.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.echo.app.R

@Composable
fun ChatInputBar(
    text: String,
    onTextChanged: (String) -> Unit,
    onAddClicked: () -> Unit,
    onSendClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChanged,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            placeholder = { Text("Message...") },
            maxLines = 4,
            prefix = {
                IconButton(modifier = Modifier.size(24.dp), onClick = onAddClicked) {
                    Icon(painterResource(R.drawable.ic_add_circle),
                        modifier = Modifier.padding(end = 4.dp),
                        contentDescription = stringResource(R.string.attach),
                        tint = OutlinedTextFieldDefaults.colors().unfocusedPlaceholderColor
                    )
                }
            },
            shape = RoundedCornerShape(24.dp)
        )

        IconButton(
            onClick = onSendClicked,
            enabled = text.isNotBlank(), // Disable button if text is empty
            modifier =
                if (text.isNotBlank())
                    Modifier.background(MaterialTheme.colorScheme.primary, CircleShape)
                else
                    Modifier.background(MaterialTheme.colorScheme.surfaceContainerHighest, CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_send),
                contentDescription = stringResource(R.string.send),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}