package com.got.data.remote

import com.got.domain.models.GotCharacter

interface GotCharactersService {
    suspend fun getGotCharacters(): List<GotCharacter>
}
