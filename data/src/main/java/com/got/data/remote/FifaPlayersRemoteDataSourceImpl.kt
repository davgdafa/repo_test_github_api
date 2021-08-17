package com.got.data.remote

import com.got.domain.models.GotPlayer

class GotPlayersRemoteDataSourceImpl(private val service: GotPlayersService) : GotPlayersRemoteDataSource {
    override suspend fun getGotPlayers(): List<GotPlayer> = service.getGotPlayers()
}
