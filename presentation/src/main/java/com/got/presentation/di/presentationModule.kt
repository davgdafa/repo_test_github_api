package com.got.presentation.di

import com.got.presentation.character.CharacterDetailsViewModel
import com.got.presentation.characters.CharactersViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val presentationModule = module {
    viewModel { CharactersViewModel(get(), get()) }
    viewModel { CharacterDetailsViewModel(get(), get()) }
}
