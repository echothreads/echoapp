package com.echo.app.feature.post.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.echo.app.R
import com.echo.app.ui.theme.EchoTheme
import com.echo.app.ui.widgets.GifPickerBottomSheet
import com.echo.app.ui.widgets.SimpleAlertDialog
import com.echo.app.ui.widgets.poll.FeedPollView
import com.echo.app.ui.widgets.poll.PollBottomSheet
import androidx.compose.runtime.collectAsState
import com.echo.app.feature.post.domain.AttachmentType
import com.echo.app.feature.post.domain.PostAttachments
import com.echo.app.ui.widgets.poll.PostPollModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    userAvatarUrl: String,
    onClose: () -> Unit,
    onPostCreated: (String) -> Unit,
    viewModel: CreatePostViewModel = viewModel()
) {
    // ViewModel States
    val postAttachments = viewModel.postAttachmentsState.collectAsState().value

    var postText by viewModel.postText
    val isPostButtonEnabled = postText.trim().isNotEmpty()

    // Native Android Image Picker
    val multiplePhotosPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 4)
    ) { uris ->
        if (uris.isNotEmpty()) {
            val stringUris: List<String> = uris.map { it.toString() }
            viewModel.setImages(stringUris)
        }
    }

    // coroutine scope
    val scope = rememberCoroutineScope()
    // local UI states
    var showGifPicker by remember { mutableStateOf(false) }
    var showPollCreator by remember { mutableStateOf(false) }
    var showMapsSheet by remember { mutableStateOf(false) }
    var pendingConflict by remember {
        mutableStateOf<ConflictDialogState?>(null)
    }

    // Attachment button action avoiding boilerplate
    fun attachmentButtonAction(incoming: AttachmentType, openPicker: () -> Unit) {
        scope.launch {
            var shouldContinue = true
            val conflicts = postAttachments.getConflictsWith(incoming)
            if (conflicts.isNotEmpty()) {
                val completer = CompletableDeferred<Boolean>()
                pendingConflict = ConflictDialogState(conflicts, completer)
                shouldContinue = completer.await()
            }
            if (shouldContinue) {
                openPicker()
            }
            pendingConflict = null
        }
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = { Text("New Post", fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(painterResource(R.drawable.ic_close), contentDescription = stringResource(R.string.cancel))
                    }
                },
                actions = {
                    Button(
                        onClick = { onPostCreated(postText) },
                        enabled = isPostButtonEnabled,
                    ) {
                        Text("Post", fontSize = 16.sp)
                    }
                }
            )
        },
        bottomBar = {
            PostBottomBar(
                imageOnclick = {
                    attachmentButtonAction(
                        incoming = AttachmentType.MEDIA,
                        openPicker = {
                            multiplePhotosPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    )
                },
                gifOnclick = {
                    attachmentButtonAction(
                        incoming = AttachmentType.GIF,
                        openPicker = { showGifPicker = true }
                    )
                },
                pollOnclick = {
                    attachmentButtonAction(
                        incoming = AttachmentType.POLL,
                        openPicker = { showPollCreator = true }
                    )
                },
                mapsOnclick = {
                    attachmentButtonAction(
                        incoming = AttachmentType.MAP,
                        openPicker = { showMapsSheet = true }
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(innerPadding)) {
            // Image with text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // User Avatar
                AsyncImage(model = userAvatarUrl,
                    modifier = Modifier.size(48.dp).clip(CircleShape),
                    contentDescription = stringResource(R.string.profile_picture),
                    placeholder = painterResource(R.drawable.outline_account_circle),
                    error = painterResource(id = R.drawable.outline_account_circle)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column() {
                    // The Text
                    BasicTextField(value = postText,
                        onValueChange = {
                            if (it.length <= 500) {
                                postText = it
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 18.sp,
                            lineHeight = 24.sp
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        decorationBox = { innerTextField ->
                            Box {
                                if (postText.isEmpty()) {
                                    Text(
                                        text = "What's happening?",
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                        fontSize = 18.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                    Text(
                        text = "${postText.length} / 500",
                        color = if (postText.length == 500) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, end = 8.dp),
                        textAlign = TextAlign.End
                    )
                }
            }
            // Poll
            if (postAttachments.pollOptions != emptyList<String>())
                FeedPollView(postAttachments.pollOptions, modifier = Modifier.padding(horizontal = 16.dp))
            // GIF
            if (postAttachments.gif != null) {
                AsyncImage(model = postAttachments.gif,
                    contentDescription = "Selected GIF",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .heightIn(max = 240.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            // Images
            if (postAttachments.images.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth().height(120.dp)
                ) {
                    items(postAttachments.images) { uri ->
                        Box {
                            // The actual image
                            AsyncImage(
                                model = uri,
                                contentDescription = "Selected image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(120.dp)
                            )

                            // X button
                            IconButton(onClick = { viewModel.removeImage(uri) }) {
                                Icon(painterResource(R.drawable.ic_close),
                                    contentDescription = stringResource(R.string.cancel))
                            }
                        }
                    }
                }
            }
        }
        // BOTTOM SHEET PICKERS
        when {
            showGifPicker -> GifPickerBottomSheet(
                onDismiss = {
                    showGifPicker = false
                },
                onGifSelected = { gif ->
                    viewModel.setGif(gif)
                    showGifPicker = false
                }
            )
            showPollCreator -> PollBottomSheet(
                onDismiss = {
                    showPollCreator = false
                },
                onSelect = { pollList ->
                    showPollCreator = false
                    viewModel.setPoll(pollList)
                }
            )
            showMapsSheet -> SimpleAlertDialog(
                title = "Not implemented",
                text = "Location picker unimplemented",
                confirmText = "OK",
                onDismissRequest = { showMapsSheet = false },
                onConfirmation = { showMapsSheet = false }
            )
            pendingConflict != null -> PostConflictsDialog(
                conflicts = pendingConflict!!.conflicts,
                onDismiss = {
                    pendingConflict!!.completer.complete(false)
                }
            ) {
                pendingConflict!!.completer.complete(true)
            }
        }
    }
}

@Composable
fun PostConflictsDialog(conflicts: List<AttachmentType>,
                        onDismiss: () -> Unit,
                        onConfirm: () -> Unit) {
    val conflictsString = conflicts.joinToString {
        it.toString()
    }
    SimpleAlertDialog(
        title = stringResource(R.string.too_many_attachments),
        text = stringResource(R.string.too_many_attachments_message, conflictsString),
        confirmText = "OK",
        onDismissRequest = onDismiss,
        onConfirmation = onConfirm
    )
}

@Preview(
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun CreatePostScreenPreview() {
    EchoTheme {
        CreatePostScreen("https://randomuser.me/api/portraits/med/men/1.jpg",
            {}, {})
    }
}

data class ConflictDialogState(
    val conflicts: List<AttachmentType>,
    val completer: CompletableDeferred<Boolean>
)