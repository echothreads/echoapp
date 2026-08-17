package com.echo.app.ui.widgets.poll

data class PollDto(
    val totalVotes: Long,
    val pollOptions: List<PollOptionDto>
)

data class PollOptionDto(
    val text: String,
    val votes: Long,
    val isSelected: Boolean
)
