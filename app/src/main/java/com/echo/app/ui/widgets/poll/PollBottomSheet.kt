package com.echo.app.ui.widgets.poll

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echo.app.R
import com.echo.app.ui.theme.EchoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PollBottomSheet(
    onDismiss: () -> Unit,
    onSelect: (List<String>) -> Unit,
    existingOptions: List<String> = emptyList()
) {
    val pollOptions = remember { mutableStateListOf("", "") }
    val maxOptions = 10
    val scrollState = rememberScrollState()
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp)
            .fillMaxWidth()
            .animateContentSize()
            .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(painterResource(R.drawable.ic_chart),
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null)

            Text(stringResource(R.string.poll_creation),
                modifier = Modifier.padding(bottom = 8.dp))

            pollOptions.forEachIndexed { index, pollValue ->
                Column {
                    var isFieldFocused by remember { mutableStateOf(false) }

                    OutlinedTextField(value = pollValue,
                        onValueChange = { new -> if(new.length <= 100) pollOptions[index] = new },
                        label = { Text(stringResource(R.string.poll_option, index + 1)) },
                        trailingIcon =
                            if (index >= 2) {
                                {
                                    IconButton(onClick = { pollOptions.removeAt(index) }) {
                                        Icon(
                                            painterResource(R.drawable.ic_close),
                                            contentDescription = "Remove"
                                        )
                                    }
                                }
                            } else {
                                null
                            },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp, start = 4.dp, end = 4.dp)
                            .onFocusChanged { focusState ->
                                isFieldFocused = focusState.isFocused
                            },
                    )

                    if (isFieldFocused) {
                        Text("${pollValue.length}/100",
                            modifier = Modifier.padding(start = 4.dp),
                            fontSize = 12.sp,
                            color = if (pollValue.length >= 100) MaterialTheme.colorScheme.error else Color.Unspecified)
                    }
                }
            }
            val shouldDisable = pollOptions.size >= maxOptions
            TextButton(
                modifier = Modifier.padding(start = 8.dp).align(Alignment.Start),
                onClick = {
                    if (!shouldDisable) pollOptions.add("")
                },
                enabled = !shouldDisable
            ) {
                Text(stringResource(R.string.add_option))
            }

            Button(onClick = {
                onSelect(
                    pollOptions.toList()
                )
            }) {
                Text(stringResource(R.string.create))
            }
        }
    }
}

@Preview(
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun PollBottomSheetPreview() {
    EchoTheme {
        Scaffold {innerPadding ->
            Box(Modifier.padding(innerPadding)) {
                PollBottomSheet(
                    onDismiss = {},
                    onSelect = {}
                )
            }
        }
    }
}