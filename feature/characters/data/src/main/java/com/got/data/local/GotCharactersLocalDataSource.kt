package com.got.data.local

import com.got.domain.models.GotCharacter

interface GotCharactersLocalDataSource {
    suspend fun getGotCharacters(): List<GotCharacter>
    suspend fun getGotCharacterDetails(characterId: Int): GotCharacter
    suspend fun searchGotCharacters(query: String): List<GotCharacter>
    suspend fun storeGotCharacters(gotCharacters: List<GotCharacter>)
    suspend fun setBookmark(id: Int, isFavorite: Boolean): Boolean
}
