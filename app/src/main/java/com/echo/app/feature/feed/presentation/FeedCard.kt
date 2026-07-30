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
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.echo.app.R
import com.echo.app.Utils
import com.echo.app.feature.feed.domain.PostModel
import com.echo.app.ui.theme.EchoTheme
import com.echo.app.ui.widgets.FullScreenImageViewer
import kotlinx.coroutines.launch
import kotlin.time.Clock

@Composable
fun FeedCard(post: PostModel) {
    var showFullScreenImage by remember { mutableStateOf(false) }
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
            .border(width = 1.dp, color = MaterialTheme.colorScheme.surfaceContainerHighest, shape = RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row() {
                AsyncImage(
                    model = post.authorProfilePic,
                    modifier = Modifier.size(48.dp).clip(CircleShape),
                    contentDescription = stringResource(R.string.profile_picture),
                    placeholder = painterResource(R.drawable.outline_account_circle),
                    error = painterResource(id = R.drawable.outline_account_circle)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Column() {
                    // HEADER: Username, labels, timestamp, options
                    Row(modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(modifier = Modifier.weight(1f, fill = false),
                            verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                post.authorUsername,
                                modifier = Modifier.weight(1f, fill = false),
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            if (post.isVerified) {
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
                                Utils.getTimeAgo(post.timestamp),
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

                    // CONTEXT: REPOSTED OR REPLY?
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        when {
                            // 1. REPOST
                            post.repostedByUsername != null -> {
                                Icon(
                                    painterResource(R.drawable.ic_reposted),
                                    contentDescription = stringResource(R.string.amplified_icon),
                                    modifier = Modifier.padding(end = 4.dp).size(16.dp)
                                )
                                Text(buildAnnotatedString {
                                    val nameStr = post.repostedByUsername
                                    val str = stringResource(R.string.amplified_ui, nameStr)
                                    val nameIndex = str.indexOf(nameStr)

                                    append(str)
                                    addStyle(SpanStyle(color = MaterialTheme.colorScheme.primary),
                                        start = nameIndex,
                                        end = nameStr.length + nameIndex)
                                }, fontSize = 14.sp)
                            }

                            // 2. REPLY WITH QUOTE
                            post.inReplyToPostId != null && post.inReplyToUsername != null && post.inReplyToSnippet != null -> {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceContainer,
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp, bottom = 8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                                        Column(modifier = Modifier.weight(1f, fill = true)) {
                                            Text(
                                                text = "@${post.inReplyToUsername}",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )

                                            Spacer(modifier = Modifier.height(4.dp))

                                            Text(
                                                text = post.inReplyToSnippet,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                maxLines = 3,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Icon(painterResource(R.drawable.ic_reply),
                                            contentDescription = stringResource(R.string.reply_icon),
                                            modifier = Modifier.size(20.dp).align(Alignment.Top))
                                    }
                                }
                            }

                            // 3. REPLY WITHOUT QUOTE
                            post.inReplyToPostId != null && post.inReplyToUsername != null -> {
                                Icon(
                                    painterResource(R.drawable.ic_reply),
                                    contentDescription = stringResource(R.string.reply_icon), // Fixed copy-paste bug here
                                    modifier = Modifier.padding(end = 4.dp).size(16.dp)
                                )
                                Text(buildAnnotatedString {
                                    val nameStr = post.inReplyToUsername
                                    val str = stringResource(R.string.post_reply_to_ui, nameStr)
                                    val nameIndex = str.indexOf(nameStr)

                                    append(str)
                                    addStyle(SpanStyle(color = MaterialTheme.colorScheme.primary),
                                        start = nameIndex,
                                        end = nameStr.length + nameIndex)
                                }, fontSize = 14.sp)
                            }
                        }
                    }

                    // CONTENT: Text, images
                    Column() {
                        Text(post.content,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 10)
                        if (post.imageUrl != null) {
                            Spacer(Modifier.height(16.dp))
                            FeedImage(post.imageUrl,
                                modifier = Modifier.clickable(onClick = { showFullScreenImage = true }))
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                    // ACTIONS: Like, Comment, Amplify (repost)
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {
                        LikeDislikeCounter(post.score)
                        Spacer(Modifier.width(8.dp))
                        CommentCounter(post.comments, {})
                        Spacer(Modifier.width(8.dp))
                        RepostCounter(reposts = post.amplifies, {})
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
    if (showFullScreenImage && post.imageUrl != null) {
        FullScreenImageViewer(
            imageUrl = post.imageUrl,
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
    val testPost = PostModel(
        id = "12",
        authorId = "1",
        authorUsername = "ender1324",
        authorProfilePic = "https://images-ext-1.discordapp.net/external/bO05h5jmNLk9leh1S_TBmrHLdYkIB2ASA3PvOIZFto4/%3Fsize%3D4096/https/cdn.discordapp.com/avatars/485486853063966742/e3cbf4a91c990bbc3b1eae53a7dee911.png?format=webp&quality=lossless&width=1020&height=1020",
        isVerified = true,
        content = "I was in Russia today, was surprised to hear that the local market did not have vodka, i will not be visiting Norilsk again",
        imageUrl = "https://preview.redd.it/moscows-depression-is-upon-us-and-its-hard-to-cope-being-an-v0-7qkqnbqu2h3g1.jpeg?auto=webp&s=75b604a9c74feb830b4a90b43a831f5273c0927a",
        timestamp = Clock.System.now(),
        score = 95643,
        comments = 5435,
        amplifies = 120043,
        inReplyToPostId = "123",
        inReplyToUsername = "parker254",
        inReplyToSnippet = null
    )
    EchoTheme {
        Scaffold { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                FeedCard(testPost)
            }
        }
    }
}