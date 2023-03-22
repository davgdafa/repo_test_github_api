package com.got.domain.usecases

import com.got.domain.data.GotCharactersRepository
import com.got.domain.models.GotCharacter

internal class GetGotCharacterDetailsUseCaseImpl(private val repo: GotCharactersRepository) : GetGotCharacterDetailsUseCase {
    override suspend fun invoke(gotCharacterId: Int): GotCharacter = repo.getGotCharacterDetails(gotCharacterId)
}
