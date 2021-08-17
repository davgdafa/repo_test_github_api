package com.got.data.remote

import com.got.domain.models.GotCharacter

interface GotCharactersRemoteDataSource {
    suspend fun getGotCharacters(): List<GotCharacter>
}
