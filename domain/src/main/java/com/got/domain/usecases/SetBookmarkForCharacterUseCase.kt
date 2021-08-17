package com.got.domain.usecases

import com.got.domain.models.GotCharacter

interface SetBookmarkForCharacterUseCase {
    suspend operator fun invoke(id: Int, isFavorite: Boolean)
}
