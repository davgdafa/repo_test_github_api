package com.got.domain.usecases

import com.got.domain.data.GotPlayersRepository
import com.got.domain.models.GotPlayer

internal class GetGotPlayersUseCaseImpl(private val repo: GotPlayersRepository) : GetGotPlayersUseCase {
    override suspend fun invoke(): List<GotPlayer> = repo.getGotPlayers()
}
