package com.got.presentation.characters

sealed class CharacterDetailsUiAction {
    class SetFavoriteGotCharacter(
        val gotCharacterId: Int,
        val isFavorite: Boolean = false
    ) : CharacterDetailsUiAction()
}