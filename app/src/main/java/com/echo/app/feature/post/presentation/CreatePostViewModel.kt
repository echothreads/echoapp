package com.echo.app.feature.post.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreatePostViewModel : ViewModel() {

    private val _selectedImageUris = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImageUris = _selectedImageUris.asStateFlow()

    fun addImages(newUris: List<Uri>) {
        val combined = (_selectedImageUris.value + newUris).distinct()
        _selectedImageUris.value = combined.take(4)
    }

    fun removeImage(uriToRemove: Uri) {
        _selectedImageUris.value = _selectedImageUris.value.filter { it != uriToRemove }
    }
}