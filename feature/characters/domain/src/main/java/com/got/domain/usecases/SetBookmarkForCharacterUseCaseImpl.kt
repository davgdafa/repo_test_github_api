package com.got.domain.usecases

import com.got.domain.data.GotCharactersRepository

class SetBookmarkForCharacterUseCaseImpl(private val repo: GotCharactersRepository) : SetBookmarkForCharacterUseCase {
    override suspend fun invoke(id: Int, isFavorite: Boolean): Boolean = repo.setBookmark(id, isFavorite)
}
