package com.got.domain.usecases

import com.got.domain.models.GotCharacter

interface SearchCharactersUseCase {
    suspend operator fun invoke(query: String): List<GotCharacter>
}
