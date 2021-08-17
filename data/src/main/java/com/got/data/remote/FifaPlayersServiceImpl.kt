package com.got.data.remote

import com.got.data.remote.GotPlayersResponseMapper.map
import com.got.domain.models.GotPlayer

class GotPlayersServiceImpl(private val api: GotPlayersApiInterface) : GotPlayersService {
    override suspend fun getGotPlayers(): List<GotPlayer> =
        api.getGotPlayers().let { apiResponse ->
            if (apiResponse.isSuccessful) {
                map(apiResponse.body())
            } else {
                throw Throwable("The response was not successful")
            }
        }
}
