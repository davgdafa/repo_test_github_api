package com.got.domain.usecases

interface SetBookmarkForCharacterUseCase {
    suspend operator fun invoke(id: Int, isFavorite: Boolean): Boolean
}
