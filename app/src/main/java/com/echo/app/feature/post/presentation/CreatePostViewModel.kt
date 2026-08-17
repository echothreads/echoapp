package com.echo.app.feature.post.presentation

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.echo.app.feature.post.domain.AttachmentType
import com.echo.app.feature.post.domain.PostAttachments
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreatePostViewModel : ViewModel() {

    private val _postAttachmentsState = MutableStateFlow<PostAttachments>(PostAttachments())
    val postAttachmentsState = _postAttachmentsState.asStateFlow()
    var postText = mutableStateOf("")

    // IMAGES
    fun setImages(newUris: List<String>) {
        val combined = (_postAttachmentsState.value.images + newUris).distinct()
        _postAttachmentsState.update {
            val new = it.copyWithoutConflicts(AttachmentType.MEDIA)
            new.copy(images = combined.take(4))
        }
    }

    fun removeImage(uriToRemove: String) {
        _postAttachmentsState.update { it ->
            val newUris = it.images.filter { it != uriToRemove }
            it.copy(images = newUris)
        }
    }

    fun clearImages() {
        _postAttachmentsState.update {
            it.copy(images = emptyList())
        }
    }

    // GIFS
    fun setGif(url: String) {
        _postAttachmentsState.update {
            val new = it.copyWithoutConflicts(AttachmentType.GIF)
            new.copy(gif = url)
        }
    }

    fun removeGif() {
        _postAttachmentsState.update {
            it.copy(gif = null)
        }
    }

    // POLLS
    fun setPoll(options: List<String>) {
        _postAttachmentsState.update {
            val new = it.copyWithoutConflicts(AttachmentType.POLL)
            new.copy(pollOptions = options)
        }
    }
    fun removePoll() {
        _postAttachmentsState.update {
            it.copy(pollOptions = emptyList())
        }
    }
}

