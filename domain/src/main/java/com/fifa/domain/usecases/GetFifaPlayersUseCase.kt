package com.fifa.domain.usecases

import com.fifa.domain.models.FifaPlayer

interface GetFifaPlayersUseCase {
    suspend operator fun invoke(): List<FifaPlayer>
}
