package com.fifa.data.remote

import com.fifa.domain.models.FifaPlayer
import retrofit2.http.GET
import retrofit2.http.Query

interface FifaPlayersApiInterface {
    @GET("/entries")
    suspend fun getFifaPlayers(@Query(value = "description") description: String = "FIFA Ultimate Team items API"): List<FifaPlayer>
}
