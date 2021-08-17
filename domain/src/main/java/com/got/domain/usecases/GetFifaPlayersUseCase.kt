package com.got.domain.usecases

import com.got.domain.models.GotPlayer

interface GetGotPlayersUseCase {
    suspend operator fun invoke(): List<GotPlayer>
}
