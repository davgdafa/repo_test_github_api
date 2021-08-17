package com.fifa.data.remote

import com.fifa.domain.models.FifaPlayer

interface FifaPlayersRemoteDataSource {
    suspend fun getFifaPlayers(): List<FifaPlayer>
}
