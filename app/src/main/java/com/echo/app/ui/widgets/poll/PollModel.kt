package com.echo.app.ui.widgets.poll

data class PostPollModel(
    val totalVotes: Long,
    val voted: String?,
    val closed: Boolean,
    val options: List<PostPollOptionsModel>
) {
    companion object {
        fun fromDto(dto: PostPollDto): PostPollModel {
            return PostPollModel(
                totalVotes = dto.totalVotes,
                voted = dto.voted,
                closed = dto.closed,
                options = dto.options.map {
                    PostPollOptionsModel(
                        id = it.id,
                        name = it.name,
                        votes = it.votes
                    )
                }
            )
        }
    }
}

data class PostPollOptionsModel(
    val id: String,
    val name: String,
    val votes: Long?
)