package com.got.data

import com.got.data.local.GotPlayersLocalDataSource
import com.got.data.remote.GotPlayersRemoteDataSource
import com.got.domain.data.GotPlayersRepository
import com.got.domain.models.GotPlayer

internal class GotPlayersRepositoryImpl(
    private val remotely: GotPlayersRemoteDataSource,
    private val locally: GotPlayersLocalDataSource
) : GotPlayersRepository {
    override suspend fun getGotPlayers(): List<GotPlayer> {
        return remotely.getGotPlayers()
    }

    override suspend fun getGotPlayerDetails(playerId: Int): GotPlayer {
        return locally.getGotPlayerDetails(playerId)
    }
}
