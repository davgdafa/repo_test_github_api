package com.got.data.local

import com.got.domain.models.GotPlayer

interface GotPlayersLocalDataSource {
    suspend fun getGotPlayers(): List<GotPlayer>
    suspend fun getGotPlayerDetails(playerId: Int): GotPlayer
}
