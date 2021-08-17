package com.got.data.remote

import com.got.domain.models.GotPlayer

interface GotPlayersService {
    suspend fun getGotPlayers(): List<GotPlayer>
}
