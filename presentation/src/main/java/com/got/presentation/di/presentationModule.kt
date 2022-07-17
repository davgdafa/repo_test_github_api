package com.got.presentation.di

import com.got.presentation.characters.CharactersViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::CharactersViewModel)
}
