package com.got.data

import com.got.data.local.GotCharactersLocalDataSource
import com.got.data.remote.GotCharactersRemoteDataSource
import com.got.domain.data.GotCharactersRepository
import com.got.domain.models.GotCharacter

internal class GotCharactersRepositoryImpl(
    private val remotely: GotCharactersRemoteDataSource,
    private val locally: GotCharactersLocalDataSource
) : GotCharactersRepository {
    override suspend fun getGotCharacters(): List<GotCharacter> =
        locally.getGotCharacters().let { cachedGotCharacters ->
            if (cachedGotCharacters.isEmpty()) {
                remotely.getGotCharacters().also { locally.storeGotCharacters(it) }
            } else {
                cachedGotCharacters
            }
        }

    override suspend fun getGotCharacterDetails(characterId: Int): GotCharacter {
        return locally.getGotCharacterDetails(characterId)
    }

    override suspend fun searchGotCharacters(query: String): List<GotCharacter> {
        return locally.searchGotCharacters(query)
    }

    override suspend fun setBookmark(id: Int, isFavorite: Boolean): Boolean =
        locally.setBookmark(id, isFavorite)
}
