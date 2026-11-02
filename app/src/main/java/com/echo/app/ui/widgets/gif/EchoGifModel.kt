package com.echo.app.ui.widgets.gif

import com.echo.app.shared.data.klipy.KlipySearchResponse

data class EchoGifModel(
    val url: String,
    val width: Int? = null,
    val height: Int? = null,
    val size: Int? = null
) {
    companion object {
        fun fromDto(dto: KlipySearchResponse.GifItem): EchoGifModel {
            return EchoGifModel(
                url = dto.file.md.gif.url,
                width = dto.file.md.gif.width,
                height = dto.file.md.gif.height,
                size = dto.file.md.gif.size
            )
        }
    }
}
