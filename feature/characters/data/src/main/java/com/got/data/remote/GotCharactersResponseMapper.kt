package com.got.data.remote

import com.got.domain.models.GotCharacter

object GotCharactersResponseMapper {
    fun map(gotCharactersList: List<CharacterData>?): List<GotCharacter> {
        val listOfGotCharacters = ArrayList<GotCharacter>()
        gotCharactersList?.let { listCharacters ->
            for (characterData in listCharacters) {
                characterData.let character@{ (id, first, last, full, title, family, imageUrl) ->
                    listOfGotCharacters.add(
                        GotCharacter(
                            id = id ?: return@character,
                            firstName = first ?: return@character,
                            lastName = last ?: return@character,
                            fullName = full ?: return@character,
                            title = title ?: return@character,
                            family = family ?: return@character,
                            imageUrl = imageUrl ?: return@character
                        )
                    )
                }
            }
        }
        return listOfGotCharacters
    }
}
