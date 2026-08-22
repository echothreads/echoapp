package com.echo.app.ui.widgets.poll

data class PostPollDto(
    val totalVotes: Long,
    val voted: String?,
    val closed: Boolean,
    val options: List<PostPollOptionsDto>
)

data class PostPollOptionsDto(
    val id: String,
    val name: String,
    val votes: Long?
)