package com.got.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface GotPlayersApiInterface {
    @GET("api/v2/Characters")
    suspend fun getGotPlayers(): Response<List<PlayerData>?>
}
