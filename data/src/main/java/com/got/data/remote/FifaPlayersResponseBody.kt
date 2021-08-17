package com.got.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class GotPlayersResponseBody(
    val items: List<PlayerData>?
)
