package com.fifa.data.remote

import com.fifa.domain.models.FifaPlayer

class FifaPlayersRemoteDataSourceImpl(private val service: FifaPlayersService) : FifaPlayersRemoteDataSource {
    override suspend fun getFifaPlayers(): List<FifaPlayer> = service.getFifaPlayers()
}
