package com.got.domain.usecases

import com.got.domain.data.GotCharactersRepository
import com.got.domain.models.GotCharacter

internal class SearchCharactersUseCaseImpl(private val repo: GotCharactersRepository) : SearchCharactersUseCase {
    override suspend fun invoke(query: String): Map<Int, GotCharacter> {
        val map = HashMap<Int, GotCharacter>()
        repo.searchGotCharacters(query).forEach { character -> map[character.id] = character }
        return map
    }
}