package com.echo.app.ui.widgets

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import coil3.SingletonImageLoader
import coil3.compose.AsyncImage
import com.echo.app.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun FullScreenImageViewer(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var imageContainerSize by remember { mutableStateOf(IntSize.Zero) }

    var pendingDownloadUrl by rememberSaveable { mutableStateOf<String?>(null) }
    var isCopying by remember { mutableStateOf(false) }
    var isSharing by remember { mutableStateOf(false) }

    val saveImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/*")
    ) { destUri: Uri? ->
        val url = pendingDownloadUrl
        if (destUri != null && url != null) {
            scope.launch {
                saveCachedImageToUri(context, url, destUri)
            }
        }
        pendingDownloadUrl = null
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
            dismissOnBackPress = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Full Screen Post Image",
                modifier = Modifier
                    .onSizeChanged { size -> imageContainerSize = size }
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(1f, 3f)
                            if (scale > 1f) {
                                val maxX = (imageContainerSize.width * (scale - 1)) / 2f
                                val maxY = (imageContainerSize.height * (scale - 1)) / 2f
                                val newOffset = offset + pan
                                offset = Offset(
                                    x = newOffset.x.coerceIn(-maxX, maxX),
                                    y = newOffset.y.coerceIn(-maxY, maxY)
                                )
                            } else {
                                offset = Offset.Zero
                            }
                        }
                    }
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (scale != 1f) {
                                    scale = 1f
                                    offset = Offset.Zero
                                } else {
                                    scale = 1.5f
                                }
                            }
                        )
                    },
            )

            // Close Button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "Close",
                    tint = Color.White
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 48.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Download Button
                IconButton(onClick = {
                    val ext = MimeTypeMap.getFileExtensionFromUrl(imageUrl).ifEmpty { "jpg" }
                    pendingDownloadUrl = imageUrl
                    saveImageLauncher.launch("Echo_${System.currentTimeMillis()}.$ext")
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_download),
                        contentDescription = "Download Image",
                        tint = Color.White
                    )
                }

                // Copy Button
                IconButton(
                    enabled = !isCopying,
                    onClick = {
                        isCopying = true
                        scope.launch {
                            try {
                                copyImageToClipboard(context, imageUrl)
                            } finally {
                                isCopying = false
                            }
                        }
                    }
                ) {
                    if (isCopying) {
                        CircularProgressIndicator(Modifier.size(16.dp), color = Color.White)
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_copy),
                            contentDescription = "Copy Image",
                            tint = Color.White
                        )
                    }
                }

                // Share Button
                IconButton(
                    enabled = !isSharing,
                    onClick = {
                        isSharing = true
                        scope.launch {
                            try {
                                shareImage(context, imageUrl)
                            } finally {
                                isSharing = false
                            }
                        }
                    }
                ) {
                    if (isSharing) {
                        CircularProgressIndicator(Modifier.size(16.dp), color = Color.White)
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_send),
                            contentDescription = "Share Image",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

// Helpers

/**
 * Opens a Coil 3 cached snapshot for [imageUrl] and passes the cached
 * file to [block]. Returns null if the image isn't cached.
 */
private suspend fun <T> withCoilCacheFile(
    context: Context,
    imageUrl: String,
    block: (File) -> T
): T? = withContext(Dispatchers.IO) {
    val snapshot = SingletonImageLoader.get(context)
        .diskCache
        ?.openSnapshot(imageUrl) ?: return@withContext null
    snapshot.use { block(it.data.toFile()) }
}

/** Saves a Coil-cached image to the user-chosen [destUri]. */
private suspend fun saveCachedImageToUri(
    context: Context,
    imageUrl: String,
    destUri: Uri
): Boolean = withCoilCacheFile(context, imageUrl) { cached ->
    context.contentResolver.openOutputStream(destUri)?.use { out ->
        cached.inputStream().use { it.copyTo(out) }
    } != null
} ?: false

/**
 * Stages a Coil-cached image into `cacheDir/shared_images/` and returns
 * a FileProvider content URI and MIME type.
 */
private suspend fun prepareShareableImage(
    context: Context,
    imageUrl: String
): Pair<Uri, String>? = withContext(Dispatchers.IO) {
    val snapshot = SingletonImageLoader.get(context)
        .diskCache
        ?.openSnapshot(imageUrl) ?: return@withContext null
    val cachedFile = snapshot.use { it.data.toFile() }

    // Detect MIME
    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(cachedFile.absolutePath, options)
    val mimeType = options.outMimeType ?: "image/jpeg"
    val ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "jpg"

    val sharedDir = File(context.cacheDir, "shared_images").apply { mkdirs() }

    // Clean up old staged files (older than 1 hour)
    val cutoff = System.currentTimeMillis() - 3_600_000L
    sharedDir.listFiles()?.forEach { if (it.lastModified() < cutoff) it.delete() }

    val stagedFile = File(sharedDir, "Echo_${System.currentTimeMillis()}.$ext")
    cachedFile.copyTo(stagedFile, overwrite = true)

    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileProvider",
        stagedFile
    )

    Pair(uri, mimeType)
}

/**
 * Copies an image to the clipboard via FileProvider
 */
private suspend fun copyImageToClipboard(context: Context, imageUrl: String) {
    val (uri, _) = prepareShareableImage(context, imageUrl) ?: run {
        withContext(Dispatchers.Main) {
            Toast.makeText(
                context,
                context.getString(R.string.image_not_in_cache),
                Toast.LENGTH_SHORT
            ).show()
        }
        return
    }

    withContext(Dispatchers.Main) {
        // No idea why but system ui just crashes if we do not give it explicit permission to acces the uri
        // Good job to the AOSP developers
        try {
            context.grantUriPermission(
                "com.android.systemui", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (_: Exception) {
            // hope for the best
        }

        val clip = ClipData.newUri(context.contentResolver, "Image", uri)

        val clipboard = context.getSystemService(ClipboardManager::class.java)
        @Suppress("UsePropertyAccessSyntax")
        clipboard.setPrimaryClip(clip)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            Toast.makeText(context, R.string.image_copied, Toast.LENGTH_SHORT).show()
        }
    }
}

/**
 * Shares an image via the system sharesheet.
 */
private suspend fun shareImage(context: Context, imageUrl: String) {
    val (uri, mimeType) = prepareShareableImage(context, imageUrl) ?: run {
        withContext(Dispatchers.Main) {
            Toast.makeText(
                context,
                context.getString(R.string.image_not_in_cache),
                Toast.LENGTH_SHORT
            ).show()
        }
        return
    }

    withContext(Dispatchers.Main) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            clipData = ClipData.newUri(context.contentResolver, "Image", uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(sendIntent, null).apply {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (context !is Activity) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
        context.startActivity(chooser)
    }
}
