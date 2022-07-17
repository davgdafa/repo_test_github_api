package com.got.presentation.characters

sealed class CharactersListUiAction {
    class Filter(val query: String, val isBookmarkFilterOn: Boolean = false) : CharactersListUiAction()
    object GetCharactersList : CharactersListUiAction()
    class SetFavoriteGotCharacter(
        val gotCharacterId: Int,
        val isFavorite: Boolean = true
    ) : CharactersListUiAction()
    class CharacterClicked(val characterId: Int) : CharactersListUiAction()
    object OnBackPressed : CharactersListUiAction()
}