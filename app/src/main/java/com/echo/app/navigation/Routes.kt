package com.echo.app.navigation

import kotlinx.serialization.Serializable

// Setup & Auth
@Serializable object AuthGraph
@Serializable object WelcomeRoute
@Serializable object LoginRoute

// Main app stuff
@Serializable object MainGraph
@Serializable object FeedRoute
@Serializable object SearchRoute
@Serializable object PostRoute
@Serializable object ChatsRoute
@Serializable object AccountRoute