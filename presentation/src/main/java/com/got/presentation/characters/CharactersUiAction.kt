package com.got.presentation.characters

sealed class CharactersUiAction {
    class Filter(val query: String, val isBookmarkFilterOn: Boolean = false) : CharactersUiAction()
    object GetCharacters : CharactersUiAction()
    class SetFavoriteGotCharacter(
        val gotCharacterId: Int,
        val isFavorite: Boolean = true
    ) : CharactersUiAction()
    class CharacterClicked(val characterId: Int) : CharactersUiAction()
    object OnBackPressed : CharactersUiAction()
    object CharacterNotFound : CharactersUiAction()

    class SetFavoriteGotCharacterForDetails(
        val gotCharacterId: Int,
        val isFavorite: Boolean = false
    ) : CharactersUiAction()
}