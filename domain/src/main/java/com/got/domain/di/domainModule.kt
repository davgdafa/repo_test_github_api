package com.got.domain.di

import com.got.domain.usecases.GetGotPlayersUseCase
import com.got.domain.usecases.GetGotPlayersUseCaseImpl
import org.koin.dsl.module

val domainModule = module {
    factory<GetGotPlayersUseCase> { GetGotPlayersUseCaseImpl(get()) }
}
