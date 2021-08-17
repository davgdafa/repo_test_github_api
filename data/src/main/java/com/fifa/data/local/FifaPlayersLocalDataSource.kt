package com.fifa.data.local

import com.fifa.domain.models.FifaPlayer

interface FifaPlayersLocalDataSource {
    suspend fun getFifaPlayers(): List<FifaPlayer>
    suspend fun getFifaPlayerDetails(playerId: Int): FifaPlayer
}
