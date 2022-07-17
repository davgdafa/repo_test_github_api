package com.got.domain.di

import com.got.domain.usecases.GetGotCharacterDetailsUseCase
import com.got.domain.usecases.GetGotCharacterDetailsUseCaseImpl
import com.got.domain.usecases.GetGotCharactersUseCase
import com.got.domain.usecases.GetGotCharactersUseCaseImpl
import com.got.domain.usecases.SearchCharactersUseCase
import com.got.domain.usecases.SearchCharactersUseCaseImpl
import com.got.domain.usecases.SetBookmarkForCharacterUseCase
import com.got.domain.usecases.SetBookmarkForCharacterUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::GetGotCharactersUseCaseImpl) { bind<GetGotCharactersUseCase>() }
    factoryOf(::SearchCharactersUseCaseImpl) { bind<SearchCharactersUseCase>() }
    factoryOf(::GetGotCharacterDetailsUseCaseImpl) { bind<GetGotCharacterDetailsUseCase>() }
    factoryOf(::SetBookmarkForCharacterUseCaseImpl) { bind<SetBookmarkForCharacterUseCase>() }
}
