package com.fifa.data

import com.fifa.data.local.FifaPlayersLocalDataSource
import com.fifa.data.remote.FifaPlayersRemoteDataSource
import com.fifa.domain.data.FifaPlayersRepository
import com.fifa.domain.models.FifaPlayer

internal class FifaPlayersRepositoryImpl(
    private val remotely: FifaPlayersRemoteDataSource,
    private val locally: FifaPlayersLocalDataSource
) : FifaPlayersRepository {
    override suspend fun getFifaPlayers(): List<FifaPlayer> {
        return remotely.getFifaPlayers()
    }

    override suspend fun getFifaPlayerDetails(playerId: Int): FifaPlayer {
        return locally.getFifaPlayerDetails(playerId)
    }
}
