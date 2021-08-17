package com.fifa.data.local

import com.fifa.domain.models.FifaPlayer

class FifaPlayersLocalDataSourceImpl : FifaPlayersLocalDataSource {
    override suspend fun getFifaPlayers(): List<FifaPlayer> {
        // todo update players or remove this method
        return listOf()
    }

    override suspend fun getFifaPlayerDetails(playerId: Int): FifaPlayer {
        // todo update using current player
        return FifaPlayer("", "", "", 0, "", "", "", "", "")
    }
}
