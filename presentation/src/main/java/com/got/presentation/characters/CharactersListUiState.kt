package com.got.presentation.characters

import com.got.domain.models.GotCharacter

data class CharactersListUiState(
    val characters: List<GotCharacter> = emptyList(),
    val shouldShowLoading: Boolean = false,
    val errorMessage: String? = null,
    val showCharacterDetails: Int? = null,
    val goBackToPreviousActivity: Boolean = false
)