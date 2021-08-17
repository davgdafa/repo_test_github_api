package com.fifa.domain.di

import com.fifa.domain.usecases.GetFifaPlayersUseCase
import com.fifa.domain.usecases.GetFifaPlayersUseCaseImpl
import org.koin.dsl.module

val domainModule = module {
    factory<GetFifaPlayersUseCase> { GetFifaPlayersUseCaseImpl(get()) }
}
