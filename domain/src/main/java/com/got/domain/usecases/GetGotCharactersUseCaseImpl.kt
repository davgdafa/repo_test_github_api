package com.got.domain.usecases

import com.got.domain.data.GotCharactersRepository
import com.got.domain.models.GotCharacter

internal class GetGotCharactersUseCaseImpl(private val repo: GotCharactersRepository) : GetGotCharactersUseCase {
    override suspend fun invoke(): List<GotCharacter> = repo.getGotCharacters()
}
