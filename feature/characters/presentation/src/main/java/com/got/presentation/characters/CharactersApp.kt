package com.got.presentation.characters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

@Composable
internal fun GotCharacterApp(
    state: CharactersUiState,
    action: (CharactersUiAction) -> Unit
) {
    var isFavoritesFiltersOn by rememberSaveable { mutableStateOf(false) }
    var queryValue by remember { mutableStateOf("") }

    if (state.showCharacterDetails != null && state.characters.isNotEmpty()) {
        val character by remember { mutableStateOf(state.showCharacterDetails) }
        var isFavorite by remember { mutableStateOf(character.isFavorite) }
        isFavorite = character.isFavorite

        GotCharacterDetails(
            character = character,
            height = 500.dp,
            isFavorite = isFavorite,
            setFavorite = {
                action(
                    CharactersUiAction.SetFavoriteGotCharacterForDetails(
                        character.id,
                        !isFavorite
                    )
                )
            }
        )
    } else {
        GotCharactersListScreen(
            queryValue,
            { queryValue = it },
            isFavoritesFiltersOn,
            { isFavoritesFiltersOn = it },
            state,
            action
        )
    }
}