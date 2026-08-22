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
fun FeedPollView(pollOptions: PostPollModel, onVote: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier) {
        pollOptions.options.forEach { option ->
            val isSelected = option.id == pollOptions.voted
            PollOption(
                optionText = option.name,
                votes = option.votes,
                totalVotes = pollOptions.totalVotes,
                isSelected,
                onVote = {
                    if (!pollOptions.closed && pollOptions.voted == null) {
                        onVote(option.id)
                    }
                }
            )
            Spacer(Modifier.height(4.dp))
        }
    }
}

// Preview for post screen
@Composable
fun FeedPollView(
    pollOptions: List<String>,
    modifier: Modifier = Modifier
) {
    val fakeModel = PostPollModel(
        totalVotes = 0,
        voted = null,
        closed = false,
        options = pollOptions.mapIndexed { index, text ->
            PostPollOptionsModel(
                id = index.toString(),
                name = text,
                votes = 0
            )
        }
    )

    FeedPollView(
        pollOptions = fakeModel,
        onVote = { /* Do nothing */ },
        modifier = modifier
    )
}