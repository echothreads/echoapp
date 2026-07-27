package com.echo.app.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echo.app.feature.feed.data.DummyFeedRepository
import com.echo.app.feature.feed.domain.PostModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class UserProfileScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileFeedUiState())
    val uiState: StateFlow<ProfileFeedUiState> = _uiState.asStateFlow()

    private var currentCursor: String? = null
    private var fetchJob: Job? = null

    fun loadNextPage(tabType: ProfileTabType = ProfileTabType.POSTS) {
        if (_uiState.value.isLoading || _uiState.value.isEndOfFeed) return

        fetchJob = viewModelScope.launch {
            _uiState.update { state -> state.copy(isLoading = true, error = null) }

            try {
                var newPosts: List<PostModel>
                // TODO repository.getPosts(userId, tabType, currentCursor, limit = 20)
                when (tabType) {
                    ProfileTabType.POSTS -> newPosts = fetchPosts(currentCursor, ProfileTabType.POSTS)
                    ProfileTabType.REPLIES -> newPosts = fetchPosts(currentCursor, ProfileTabType.REPLIES)
                    ProfileTabType.REPOSTS -> newPosts = fetchPosts(currentCursor, ProfileTabType.REPOSTS)
                }

                if (newPosts.isEmpty()) {
                    // user has no posts or has reached the end
                    _uiState.update { state -> state.copy(isLoading = false, isEndOfFeed = true) }
                } else {
                    currentCursor = newPosts.last().id

                    _uiState.update { currentState ->
                        currentState.copy(
                            posts = currentState.posts + newPosts,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun switchTab(newTabType: ProfileTabType) {
        fetchJob?.cancel()
        currentCursor = null
        _uiState.value = ProfileFeedUiState()
        loadNextPage(newTabType)
    }

    // TODO repository.getPosts(userId, tabType, currentCursor, limit = 20)
    private suspend fun fetchPosts(cursor: String?, tabType: ProfileTabType): List<PostModel> {
        return DummyFeedRepository().getProfilePosts(userId = "user_1",
            profileUsername = "ender1324",
            tabType = tabType,
            cursor = cursor,
            limit = 50)
    }
}

data class ProfileFeedUiState(
    val posts: List<PostModel> = emptyList(),
    val isLoading: Boolean = false,
    val isEndOfFeed: Boolean = false,
    val error: String? = null
)

enum class ProfileTabType {
    POSTS,
    REPLIES,
    REPOSTS
}