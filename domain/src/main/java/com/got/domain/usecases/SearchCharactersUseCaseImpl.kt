package com.got.domain.usecases

import com.got.domain.data.GotCharactersRepository
import com.got.domain.models.GotCharacter

internal class SearchCharactersUseCaseImpl(private val repo: GotCharactersRepository) : SearchCharactersUseCase {
    override suspend fun invoke(query: String): List<GotCharacter> = repo.searchGotCharacters(query)
}