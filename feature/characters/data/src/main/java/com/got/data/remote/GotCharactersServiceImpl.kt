package com.got.data.remote

import com.got.data.remote.GotCharactersResponseMapper.map
import com.got.domain.models.GotCharacter

class GotCharactersServiceImpl(private val api: GotCharactersApiInterface) : GotCharactersService {
    override suspend fun getGotCharacters(): List<GotCharacter> =
        api.getGotCharacters().let { apiResponse ->
            if (apiResponse.isSuccessful) {
                map(apiResponse.body())
            } else {
                throw Throwable("The response was not successful")
            }
        }
}
