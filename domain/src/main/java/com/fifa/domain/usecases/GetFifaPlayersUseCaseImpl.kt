package com.fifa.domain.usecases

import com.fifa.domain.data.FifaPlayersRepository
import com.fifa.domain.models.FifaPlayer

internal class GetFifaPlayersUseCaseImpl(private val repo: FifaPlayersRepository) : GetFifaPlayersUseCase {
    override suspend fun invoke(): List<FifaPlayer> = repo.getFifaPlayers()
}
