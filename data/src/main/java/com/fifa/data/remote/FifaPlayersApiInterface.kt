package com.fifa.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface FifaPlayersApiInterface {
    @GET("fifa/ultimate-team/api/fut/item")
    suspend fun getFifaPlayers(): Response<FifaPlayersResponseBody>
}
