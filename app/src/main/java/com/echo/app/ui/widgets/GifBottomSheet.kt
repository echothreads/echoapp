package com.echo.app.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.echo.app.ui.theme.EchoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GifPickerBottomSheet(
    onDismiss: () -> Unit,
    onGifSelected: (String) -> Unit,
    viewModel: GifBottomSheetViewModel = viewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val gifState by viewModel.gifResults.collectAsState()

    LaunchedEffect(true) {
        viewModel.getPopularGifs()
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp)) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                label = { Text("Search KLIPY...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxHeight(0.8f)
            ) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    when {
                        gifState.isLoading -> {
                            Box(modifier = Modifier.fillMaxWidth().height(200.dp),
                                contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }

                        gifState.error != null -> {
                            Box(modifier = Modifier.fillMaxWidth().height(200.dp),
                                contentAlignment = Alignment.Center) {
                                Text(gifState.error!!,
                                    color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }

                items(gifState.gifResults,
                    key = { gifUrl -> gifUrl }) { gifUrl ->
                    AsyncImage(
                        model = gifUrl,
                        contentDescription = "GIF",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onGifSelected(gifUrl) }
                    )
                }
            }
        }
    }
}

@Preview(
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun GifPickerBottomSheetPreview() {
    EchoTheme {
        Scaffold() { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                GifPickerBottomSheet(
                    onDismiss = {
                    },
                    onGifSelected = {}
                )
            }
        }
    }
}