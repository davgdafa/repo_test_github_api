package com.got.domain.di

import com.got.domain.usecases.GetGotCharacterDetailsUseCase
import com.got.domain.usecases.GetGotCharacterDetailsUseCaseImpl
import com.got.domain.usecases.GetGotCharactersUseCase
import com.got.domain.usecases.GetGotCharactersUseCaseImpl
import com.got.domain.usecases.SearchCharactersUseCase
import com.got.domain.usecases.SearchCharactersUseCaseImpl
import com.got.domain.usecases.SetBookmarkForCharacterUseCase
import com.got.domain.usecases.SetBookmarkForCharacterUseCaseImpl
import org.koin.dsl.module

val domainModule = module {
    factory<GetGotCharactersUseCase> { GetGotCharactersUseCaseImpl(get()) }
    factory<SearchCharactersUseCase> { SearchCharactersUseCaseImpl(get()) }
    factory<GetGotCharacterDetailsUseCase> { GetGotCharacterDetailsUseCaseImpl(get()) }
    factory<SetBookmarkForCharacterUseCase> { SetBookmarkForCharacterUseCaseImpl(get()) }
}
