package com.echo.app.feature.post.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.echo.app.R

@Composable
fun PostBottomBar() {
    Row(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f, fill = true)) {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    painterResource(R.drawable.ic_photo),
                    contentDescription = stringResource(R.string.add_picture),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(
                onClick = {}
            ) {
                Icon(
                    painterResource(R.drawable.ic_gif),
                    contentDescription = stringResource(R.string.add_gif),
                    tint = MaterialTheme.colorScheme.primary
                )
            }


            IconButton(
                onClick = {}
            ) {
                Icon(
                    painterResource(R.drawable.ic_chart),
                    contentDescription = stringResource(R.string.add_poll),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(
                onClick = {}
            ) {
                Icon(
                    painterResource(R.drawable.ic_location),
                    contentDescription = stringResource(R.string.add_location),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }


        IconButton(
            onClick = {}
        ) {
            Icon(painterResource(R.drawable.ic_3dots_vertical),
                contentDescription = stringResource(R.string.options)
            )
        }
    }
}