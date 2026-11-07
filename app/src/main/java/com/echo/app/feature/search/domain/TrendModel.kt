package com.echo.app.feature.search.domain

data class TrendModel(
    val name: String,
    val echoes: Int,
    val ranking: Short,
    val imageUrl: String? = null
)