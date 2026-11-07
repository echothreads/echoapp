package com.echo.app.ui.widgets

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.echo.app.R
import com.echo.app.ui.theme.EchoTheme

@Composable
fun UserRow(
    displayName: String,
    username: String?,
    profileAvatar: String?,
    followed: Boolean = false,
    onFollow: (Boolean) -> Unit = {}
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
            .border(width = 1.dp, color = MaterialTheme.colorScheme.surfaceContainerHighest, shape = RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AsyncImage(
                    model = profileAvatar,
                    modifier = Modifier.size(52.dp).clip(CircleShape),
                    contentDescription = stringResource(R.string.profile_picture),
                    placeholder = painterResource(R.drawable.outline_account_circle),
                    error = painterResource(id = R.drawable.outline_account_circle)
                )

                Column(horizontalAlignment = Alignment.Start) {
                    Text(displayName, fontWeight = FontWeight.Medium)
                    if (username != null) {
                        Text("@$username", fontWeight = FontWeight.Light)
                    }
                }
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            FollowButton(followed = followed, onFollow = onFollow)
        }
    }
}

@Preview(
    showBackground = true
)
@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun UserRowPreview() {
    EchoTheme {
        UserRow(
            displayName = "Test Display Name",
            username = "test",
            profileAvatar = null,
            followed = false,
            onFollow = {}
        )
    }
}