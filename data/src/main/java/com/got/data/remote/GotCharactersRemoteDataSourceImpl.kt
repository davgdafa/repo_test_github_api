package com.got.data.remote

import com.got.domain.models.GotCharacter

class GotCharactersRemoteDataSourceImpl(private val service: GotCharactersService) : GotCharactersRemoteDataSource {
    override suspend fun getGotCharacters(): List<GotCharacter> = service.getGotCharacters()
}
