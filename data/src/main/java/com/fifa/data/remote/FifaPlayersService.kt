package com.fifa.data.remote

import com.fifa.domain.models.FifaPlayer

interface FifaPlayersService {
    suspend fun getFifaPlayers(): List<FifaPlayer>
}
