package com.echo.app.ui.widgets.poll

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FeedPollView(pollOptions: List<String>, modifier: Modifier = Modifier, static: Boolean = false) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier) {
        pollOptions.forEachIndexed { index, option ->
            val isSelected = static && selectedIndex == index
            if (static) {
                PollOption(
                    optionText = option,
                    votes = 0,
                    totalVotes = 0,
                    isSelected,
                    onVote = {
                        Unit
                    }
                )
            } else {
                val pollOption = PollOptionDto(text = "hi", votes = 5, isSelected = false)
                PollOption(
                    optionText = pollOption.text,
                    votes = pollOption.votes,
                    totalVotes = 500,
                    pollOption.isSelected,
                    onVote = {
                        selectedIndex = index
                    }
                )
            }
            Spacer(Modifier.height(4.dp))
        }
    }
}