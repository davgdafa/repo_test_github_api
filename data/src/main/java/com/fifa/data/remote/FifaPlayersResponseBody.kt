package com.fifa.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class FifaPlayersResponseBody(
    val items: List<PlayerData>?
)
