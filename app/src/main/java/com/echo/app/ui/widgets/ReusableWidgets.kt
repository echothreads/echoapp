package com.echo.app.ui.widgets

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echo.app.R
import com.echo.app.ui.theme.EchoTheme

@Composable
fun FollowButton(
    modifier: Modifier = Modifier,
    followed: Boolean = false,
    onFollow: (Boolean) -> Unit = {}
) {

    if (!followed) {
        TextButton(onClick = { onFollow(true) },
            modifier = modifier,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.textButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceContainerHighest)
        ) {
            Text(stringResource(R.string.follow),
                modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface)
        }
    } else {
        TextButton(onClick = { onFollow(false) },
            modifier = modifier,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.textButtonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(stringResource(R.string.unfollow),
                modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
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
fun ReusableComponentsPreview() {
    EchoTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            FollowButton()
        }
    }
}