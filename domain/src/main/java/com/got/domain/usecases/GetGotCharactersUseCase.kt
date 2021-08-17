package com.got.domain.usecases

import com.got.domain.models.GotCharacter

interface GetGotCharactersUseCase {
    suspend operator fun invoke(): List<GotCharacter>
}

