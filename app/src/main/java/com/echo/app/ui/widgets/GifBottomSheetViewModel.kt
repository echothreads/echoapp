package com.echo.app.ui.widgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echo.app.shared.klipy.KlipySearchResponse
import com.echo.app.shared.network.ktorClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import com.echo.app.BuildConfig
import kotlin.collections.emptyList

class GifBottomSheetViewModel(
    // TODO: DI the authrepo here to get the user id dynamically
    val userId: String = "1"
) : ViewModel() {
    val baseUrl = "https://api.klipy.com"
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _gifResults = MutableStateFlow<GifSelectorState>(GifSelectorState())
    val gifResults = _gifResults.asStateFlow()

    val apiKey = BuildConfig.KLIPY_API_KEY

    var currentPage: Int? = null;
    init {
        getPopularGifs()
        setupSearchDebounce()
    }

    fun getPopularGifs() {
        viewModelScope.launch {
            try {
                if (currentPage == null) currentPage = 1
                _gifResults.update { it.copy(isLoading = true) }
                val response = ktorClient.get("$baseUrl/api/v1/$apiKey/gifs/trending") {
                    parameter("page", currentPage)
                    parameter("per_page", 24)
                    parameter("customer_id", userId)
                    parameter("format_filter", "gif")
                    parameter("content_filter", "low")
                }.body<KlipySearchResponse>()
                val newGifs = response.payload.data.map { it.file.md.gif.url }
                _gifResults.update {
                    it.copy(
                        gifResults = (it.gifResults + newGifs).distinct(),
                        isEnd = !response.payload.hasNext,
                        isLoading = false,
                        error = null
                    )
                }
                currentPage = response.payload.currentPage
            } catch (e: Exception) {
                _gifResults.update {
                    it.copy(
                        gifResults = emptyList(),
                        isEnd = false,
                        isLoading = false,
                        error = e.toString()
                    )
                }
                e.printStackTrace()
            }
        }
    }


    @OptIn(FlowPreview::class)
    private fun setupSearchDebounce() {
        viewModelScope.launch {
            if (currentPage == null) currentPage = 1
            _searchQuery
                .debounce(500.milliseconds)
                .filter { it.isNotBlank() }
                .collectLatest { query ->
                    _gifResults.update { it.copy(isLoading = true) }
                    try {
                        val response = ktorClient.get("$baseUrl/api/v1/$apiKey/gifs/search") {
                            parameter("q", query)
                            parameter("page", currentPage)
                            parameter("per_page", 24)
                            parameter("customer_id", userId)
                            parameter("format_filter", "gif")
                            parameter("content_filter", "low")
                        }.body<KlipySearchResponse>()
                        val newGifs = response.payload.data.map { it.file.md.gif.url }
                        _gifResults.update {
                            it.copy(
                                gifResults = (it.gifResults + newGifs).distinct(),
                                isEnd = !response.payload.hasNext,
                                isLoading = false,
                                error = null
                            )
                        }
                        currentPage = response.payload.currentPage
                    } catch (e: Exception) {
                        _gifResults.update {
                            it.copy(
                                gifResults = emptyList(),
                                isEnd = false,
                                isLoading = false,
                                error = e.toString()
                            )
                        }
                        e.printStackTrace()
                    }
                }
        }
    }

    fun updateSearchQuery(query: String) {
        currentPage = 1
        _searchQuery.value = query
        _gifResults.update {
            it.copy(isLoading = false, gifResults = emptyList(), isEnd = false, error = null)
        }
    }
}

data class GifSelectorState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val gifResults: List<String> = emptyList(),
    val isEnd: Boolean = false
)