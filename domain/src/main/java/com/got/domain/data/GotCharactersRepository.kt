package com.got.domain.data

import com.got.domain.models.GotCharacter

interface GotCharactersRepository {
    suspend fun getGotCharacters(): List<GotCharacter>
    suspend fun getGotCharacterDetails(characterId: Int): GotCharacter
    suspend fun searchGotCharacters(query: String): List<GotCharacter>
    suspend fun setBookmark(id: Int, isFavorite: Boolean): Boolean
}
