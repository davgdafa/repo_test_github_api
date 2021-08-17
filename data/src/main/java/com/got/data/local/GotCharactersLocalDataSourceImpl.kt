package com.got.data.local

import com.got.data.local.room.dao.GotCharacterDao
import com.got.data.local.room.entities.GotCharacterEntity
import com.got.domain.models.GotCharacter

class GotCharactersLocalDataSourceImpl(private val database: GotCharacterDao) : GotCharactersLocalDataSource {
    override suspend fun getGotCharacters(): List<GotCharacter> {
        // todo remove default empty attributes, do not add if not valid
        return database.getAll().map { (id, firstName, lastName, fullName, title, family, imageUrl, isFavorite) ->
            GotCharacter(
                id = id,
                firstName = firstName ?: "",
                lastName = lastName ?: "",
                fullName = fullName ?: "",
                title = title ?: "",
                family = family ?: "",
                imageUrl = imageUrl ?: "",
                isFavorite = isFavorite ?: false,
            )
        }
    }

    override suspend fun getGotCharacterDetails(characterId: Int): GotCharacter {
        // todo remove default empty attributes, do not add if not valid
        return database.findGotCharacterById(characterId)
            .let { (id, firstName, lastName, fullName, title, family, imageUrl, isFavorite) ->
                GotCharacter(
                    id = id,
                    firstName = firstName ?: "",
                    lastName = lastName ?: "",
                    fullName = fullName ?: "",
                    title = title ?: "",
                    family = family ?: "",
                    imageUrl = imageUrl ?: "",
                    isFavorite = isFavorite ?: false
                )
            }
    }

    override suspend fun searchGotCharacters(query: String): List<GotCharacter> {
        val storedGotCharacters = getGotCharacters()
        val gotCharacters = ArrayList<GotCharacter>()
        for (gotCharacter in storedGotCharacters) {
            if (gotCharacter.toString().lowercase().contains(query.lowercase())) {
                gotCharacters.add(gotCharacter)
            }
        }
        return gotCharacters
    }

    override suspend fun storeGotCharacters(gotCharacters: List<GotCharacter>) {
        database.insertAll(gotCharacters.map { gotCharacterModel ->
            GotCharacterEntity(
                id = gotCharacterModel.id,
                firstName = gotCharacterModel.firstName,
                lastName = gotCharacterModel.lastName,
                fullName = gotCharacterModel.fullName,
                title = gotCharacterModel.title,
                family = gotCharacterModel.family,
                imageUrl = gotCharacterModel.imageUrl,
                isFavorite = gotCharacterModel.isFavorite
            )
        })
    }

    override suspend fun setBookmark(id: Int, isFavorite: Boolean) {
        database.updateCharacterById(id, isFavorite)
    }
}
