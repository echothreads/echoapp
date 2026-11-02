package com.echo.app.di

import com.echo.app.shared.data.EchoApi
import com.echo.app.shared.data.EchoExternalApi
import com.echo.app.shared.network.ktorClient
import com.echo.app.ui.widgets.gif.GifBottomSheetViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::ktorClient)
    singleOf(::EchoExternalApi)
    singleOf(::EchoApi)

    viewModelOf(::GifBottomSheetViewModel)
}