package com.fifa.data.remote

import com.fifa.domain.models.FifaPlayer

class FifaPlayersServiceImpl(private val api: FifaPlayersApiInterface) : FifaPlayersService {
    override suspend fun getFifaPlayers(): List<FifaPlayer> {
        return api.getFifaPlayers()
    }
}
