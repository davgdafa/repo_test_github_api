package com.fifa.data.remote

import com.fifa.data.remote.FifaPlayersResponseMapper.map
import com.fifa.domain.models.FifaPlayer

class FifaPlayersServiceImpl(private val api: FifaPlayersApiInterface) : FifaPlayersService {
    override suspend fun getFifaPlayers(): List<FifaPlayer> =
        api.getFifaPlayers().let { apiResponse ->
            if (apiResponse.isSuccessful) {
                map(apiResponse.body())
            } else {
                throw Throwable("The response was not successful")
            }
        }
}
