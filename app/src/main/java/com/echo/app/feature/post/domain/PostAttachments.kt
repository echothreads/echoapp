package com.echo.app.feature.post.domain

data class PostAttachments(
    val images: List<String> = emptyList(),
    val gif: String? = null,
    val pollOptions: List<String> = emptyList(),
    val mapLocation: String? = null
) {
    val activeTypes: List<AttachmentType>
        get() = buildList {
            if (images.isNotEmpty()) add(AttachmentType.MEDIA)
            if (gif != null) add(AttachmentType.GIF)
            if (pollOptions.isNotEmpty()) add(AttachmentType.POLL)
            if (mapLocation != null) add(AttachmentType.MAP)
        }

    private fun getConflicts(incoming: AttachmentType): List<AttachmentType> = when (incoming) {
        AttachmentType.GIF -> listOf(AttachmentType.MEDIA, AttachmentType.MAP)
        AttachmentType.MEDIA -> listOf(AttachmentType.GIF)
        AttachmentType.POLL -> listOf(AttachmentType.GIF, AttachmentType.MEDIA, AttachmentType.MAP)
        AttachmentType.MAP -> listOf(AttachmentType.GIF)
    }

    fun getConflictsWith(incoming: AttachmentType): List<AttachmentType> {
        val conflict = getConflicts(incoming)
        return this.activeTypes.filter { conflict.contains(it) }
    }

    fun copyWithoutConflicts(incoming: AttachmentType): PostAttachments {
        val rules = getConflicts(incoming)
        return this.copy(
            images = if (AttachmentType.MEDIA in rules) emptyList() else images,
            gif = if (AttachmentType.GIF in rules) null else gif,
            pollOptions = if (AttachmentType.POLL in rules) emptyList() else pollOptions,
            mapLocation = if (AttachmentType.MAP in rules) null else mapLocation
        )
    }
}

enum class AttachmentType {
    GIF, MEDIA, POLL, MAP
}
