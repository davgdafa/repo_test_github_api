package com.got.domain.data

import com.got.domain.models.GotPlayer

interface GotPlayersRepository {
    suspend fun getGotPlayers(): List<GotPlayer>
    suspend fun getGotPlayerDetails(playerId: Int): GotPlayer
}
