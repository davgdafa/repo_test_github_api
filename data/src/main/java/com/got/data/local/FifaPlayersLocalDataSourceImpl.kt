package com.got.data.local

import com.got.domain.models.GotPlayer

class GotPlayersLocalDataSourceImpl : GotPlayersLocalDataSource {
    override suspend fun getGotPlayers(): List<GotPlayer> {
        // todo update players or remove this method
        return listOf()
    }

    override suspend fun getGotPlayerDetails(playerId: Int): GotPlayer {
        // todo update using current player
        return GotPlayer(0, "", "", "", "", "", "")
    }
}
