package com.echo.app.feature.feed.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.echo.app.R
import com.echo.app.Utils
import com.echo.app.ui.theme.EchoTheme
import com.echo.app.ui.widgets.FullScreenImageViewer
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun FeedCard(username: String, timestamp: Instant, postContent: String, postImage: String?, score: Int, comments: Int, amplifies: Int, isVerified: Boolean = false, profilePic: String? = null) {
    var showFullScreenImage by remember { mutableStateOf(false) }
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
            .border(width = 1.dp, color = MaterialTheme.colorScheme.surfaceContainerHighest, shape = RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
            Row() {
                AsyncImage(
                    model = profilePic,
                    modifier = Modifier.size(48.dp),
                    contentDescription = "Account",
                    placeholder = painterResource(R.drawable.outline_account_circle),
                    error = painterResource(id = R.drawable.outline_account_circle),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Column() {
                    // HEAD: Username, labels, timestamp, options
                    Row(modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(modifier = Modifier.weight(1f, fill = false),
                            verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                username,
                                modifier = Modifier.weight(1f, fill = false),
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            if (isVerified) {
                                Icon(
                                    painterResource(R.drawable.ic_verified),
                                    contentDescription = stringResource(R.string.verified),
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                Utils.getTimeAgo(timestamp),
                                maxLines = 1,
                                textAlign = TextAlign.End,
                                style = LocalTextStyle.current.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.8f
                                    )
                                )
                            )
                            Spacer(Modifier.width(4.dp))
                            IconButton(
                                modifier = Modifier.size(24.dp),
                                onClick = {},
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent
                                )
                            ) {
                                Icon(
                                    painterResource(R.drawable.ic_3dots_vertical),
                                    //tint = MaterialTheme.colorScheme.onSurface,
                                    contentDescription = stringResource(R.string.options)
                                )
                            }
                        }
                    }
                    // CONTENT: Text, images
                    Column() {
                        Text(postContent,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 10)
                        if (postImage != null) {
                            Spacer(Modifier.height(16.dp))
                            FeedImage(postImage,
                                modifier = Modifier.clickable(onClick = { showFullScreenImage = true }))
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                    // ACTIONS: Like, Comment, Amplify (repost)
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {
                        LikeDislikeCounter(score)
                        Spacer(Modifier.width(8.dp))
                        CommentCounter(comments, {})
                        Spacer(Modifier.width(8.dp))
                        RepostCounter(reposts = amplifies, {})
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton({}) {
                            Icon(painterResource(R.drawable.ic_send),
                                contentDescription = stringResource(R.string.share)
                            )
                        }
                    }
                }
            }
        }
    }
    if (showFullScreenImage && postImage != null) {
        FullScreenImageViewer(
            imageUrl = postImage,
            onDismiss = { showFullScreenImage = false }
        )
    }
}

@Composable
fun LikeDislikeCounter(score: Int) {
    var currentScore by remember { mutableIntStateOf(score) }
    var currentLike by remember { mutableStateOf(LikeState.NONE) }
    val likeScale by animateFloatAsState(
        targetValue = if (currentLike == LikeState.LIKED) 1.1f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy)
    )
    val dislikeRotation = remember { Animatable(0f) }
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(8.dp) // Fully rounded pill shape
            )
            .border(width = 1.dp, color = MaterialTheme.colorScheme.surfaceContainerHighest, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 6.dp, vertical = 3.dp)) {
        // Like Button
        AnimatedVisibility(
            visible = currentLike != LikeState.DISLIKED,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            IconButton(
                modifier = Modifier.size(24.dp).scale(likeScale),
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    if (currentLike == LikeState.LIKED) {
                        currentScore -= 1
                        currentLike = LikeState.NONE
                    } else {
                        currentScore += if (currentLike == LikeState.DISLIKED) 2 else 1
                        currentLike = LikeState.LIKED
                    }
                }) {
                Crossfade(
                    targetState = currentLike == LikeState.LIKED,
                    animationSpec = tween(durationMillis = 200), // Quick 200ms fade
                    label = "heart_fade"
                ) { currentlyLiked ->
                    Icon(
                        painter = painterResource(
                            id = if (currentlyLiked) R.drawable.ic_like_filled else R.drawable.ic_like
                        ),
                        contentDescription = if (currentlyLiked) "Unlike" else "Like",
                        tint = if (currentlyLiked) MaterialTheme.colorScheme.primary else LocalContentColor.current,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // The Unified Score
        Text(
            text = Utils.formatNumber(currentScore),
            modifier = Modifier.padding(horizontal = 6.dp)
                .widthIn(20.dp, 40.dp),
            textAlign = TextAlign.Center,
            maxLines = 1,
            style = MaterialTheme.typography.labelLarge,
            color = if (currentLike == LikeState.LIKED) MaterialTheme.colorScheme.primary else if (currentLike == LikeState.DISLIKED) MaterialTheme.colorScheme.error else Color.Unspecified
        )

        // Dislike Button
        AnimatedVisibility(
            visible = currentLike != LikeState.LIKED,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            IconButton(
                modifier = Modifier.size(24.dp).rotate(dislikeRotation.value),
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    if (currentLike == LikeState.DISLIKED) {
                        currentScore += 1
                        currentLike = LikeState.NONE
                    } else {
                        currentScore -= if (currentLike == LikeState.LIKED) 2 else 1
                        currentLike = LikeState.DISLIKED
                    }

                    coroutineScope.launch {
                        dislikeRotation.animateTo(-15f, tween(50))
                        dislikeRotation.animateTo(15f, tween(50))
                        dislikeRotation.animateTo(-15f, tween(50))
                        dislikeRotation.animateTo(15f, tween(50))

                        dislikeRotation.animateTo(0f, tween(50))
                    }
                }) {
                Crossfade(
                    targetState = currentLike == LikeState.DISLIKED,
                    animationSpec = tween(durationMillis = 200),
                    label = "dislike_fade"
                ) { currentlyDisliked ->
                    Icon(
                        painter = painterResource(
                            id = if (currentlyDisliked) R.drawable.ic_dislike_filled else R.drawable.ic_dislike
                        ),
                        contentDescription = "Dislike",
                        tint = if (currentlyDisliked) MaterialTheme.colorScheme.error else LocalContentColor.current
                    )
                }
            }
        }
    }
}
enum class LikeState { LIKED, DISLIKED, NONE }

@Composable
fun CommentCounter(comments: Int, onClick: () -> Unit) {
    Row(modifier = Modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        Icon(painterResource(R.drawable.ic_comment),
            modifier = Modifier.size(22.dp),
            contentDescription = stringResource(R.string.comments))
        Spacer(Modifier.width(4.dp))
        Text(Utils.formatNumber(comments),
            modifier = Modifier.widthIn(min = 24.dp),
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun RepostCounter(reposts: Int, onClick: () -> Unit) {
    Row(modifier = Modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start) {
        Icon(painterResource(R.drawable.ic_repost), contentDescription = stringResource(R.string.comments))
        Spacer(Modifier.width(4.dp))
        Text(Utils.formatNumber(reposts),
            modifier = Modifier.widthIn(min = 24.dp),
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.labelLarge)
    }
}

@Preview(
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun FeedCardPreview() {
    EchoTheme {
        Scaffold { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                FeedCard("ender1324", Clock.System.now(), "Excited for my new trip, might try some cuisine", null, 658493, 850, 6000, true)
            }
        }
    }
}