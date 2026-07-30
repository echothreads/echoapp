package com.echo.app.feature.feed.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.echo.app.R

@Composable
fun FeedImage(
    imageUrl: String,
    roundingSize: Dp = 8.dp,
    modifier: Modifier = Modifier
) {
    val painter = rememberAsyncImagePainter(model = imageUrl)
    val state by painter.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = RoundedCornerShape(roundingSize)
            )
            .clip(RoundedCornerShape(roundingSize))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .then(if(state is AsyncImagePainter.State.Success) modifier else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = stringResource(R.string.post_attachment),
            modifier = Modifier.fillMaxWidth().then(
                if (state is AsyncImagePainter.State.Success) {
                    Modifier.heightIn(min = 200.dp, max = 400.dp)
                } else {
                    Modifier.height(200.dp) // otherwise the image expands to max size on error
                }
            ),
            contentScale = ContentScale.Fit,
        )

        when (state) {
            is AsyncImagePainter.State.Empty,
            is AsyncImagePainter.State.Loading -> {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            is AsyncImagePainter.State.Error -> {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            painter.restart()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_refresh),
                        contentDescription = "Tap to retry",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            else -> {
            }
        }
    }
}