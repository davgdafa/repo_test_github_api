package com.got.domain.usecases

import com.got.domain.models.GotCharacter

interface GetGotCharacterDetailsUseCase {
    suspend operator fun invoke(gotCharacterId: Int): GotCharacter
}

