package com.got.data.remote

import com.got.domain.models.GotPlayer

interface GotPlayersRemoteDataSource {
    suspend fun getGotPlayers(): List<GotPlayer>
}
