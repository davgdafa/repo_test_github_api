package com.fifa.domain.data

import com.fifa.domain.models.FifaPlayer

interface FifaPlayersRepository {
    suspend fun getFifaPlayers(): List<FifaPlayer>
    suspend fun getFifaPlayerDetails(playerId: Int): FifaPlayer
}
