package com.got.presentation.characters

import com.got.domain.models.GotCharacter

data class CharactersUiState(
    val characters: Map<Int, GotCharacter> = emptyMap(),
    val shouldShowLoading: Boolean = false,
    val errorMessage: String? = null,
    val showCharacterDetails: GotCharacter? = null,
    val goBackToPreviousActivity: Boolean = false
)