package com.got.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.got.domain.usecases.GetGotCharactersUseCase
import com.got.domain.usecases.SearchCharactersUseCase
import com.got.domain.usecases.SetBookmarkForCharacterUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharactersViewModel(
    private val getGotCharactersUseCase: GetGotCharactersUseCase,
    private val searchCharactersUseCase: SearchCharactersUseCase,
    private val setBookmarkForCharacterUseCase: SetBookmarkForCharacterUseCase,
) : ViewModel() {
    private val _gotCharactersListUiState = MutableStateFlow(CharactersListUiState())
    val gotCharactersListUiState: StateFlow<CharactersListUiState> = _gotCharactersListUiState.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        _gotCharactersListUiState.update { it.copy(errorMessage = exception.message ?: "There was an unknown error during the process") }
    }

    private fun fetchGotCharacters() = viewModelScope.launch(errorHandler) {
        _gotCharactersListUiState.update { it.copy(shouldShowLoading = true) }
        delay(2000)
        withContext(Dispatchers.IO) {
            getGotCharactersUseCase().let { listOfCharacters ->
                if (listOfCharacters.isEmpty()) {
                    _gotCharactersListUiState.update { it.copy(errorMessage = "There were no characters retrieved") } // TODO should show a text in the layout, not a message
                } else {
                    _gotCharactersListUiState.update { it.copy(characters = listOfCharacters) }
                }
            }
        }
    }

    private fun filterGotCharacters(query: String, isBookmarkFilterOn: Boolean = false) {
        viewModelScope.launch(errorHandler) {
            _gotCharactersListUiState.update { it.copy(shouldShowLoading = true) }
            withContext(Dispatchers.IO) {
                if (query.isNotBlank()) {
                    searchCharactersUseCase(query)
                } else {
                    getGotCharactersUseCase()
                }.let { listOfCharacters ->
                    if (listOfCharacters.isEmpty()) {
                        _gotCharactersListUiState.update { it.copy(errorMessage = "There were no characters matching the filter criteria") }
                    } else {
                        if (isBookmarkFilterOn) {
                            _gotCharactersListUiState.update { it.copy(characters = listOfCharacters.filter { gotCharacter -> gotCharacter.isFavorite == true }) }
                        } else {
                            _gotCharactersListUiState.update { it.copy(characters = listOfCharacters) }
                        }
                    }
                }
            }
        }
    }

    private fun setFavoriteForGotCharacter(gotCharacterId: Int, isFavorite: Boolean = true) =
        viewModelScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                setBookmarkForCharacterUseCase(id = gotCharacterId, isFavorite)
            }
        }

    fun charactersListAction(action: CharactersListUiAction) {
        when (action) {
            is CharactersListUiAction.Filter -> filterGotCharacters(
                action.query,
                action.isBookmarkFilterOn
            )
            CharactersListUiAction.GetCharactersList -> fetchGotCharacters()
            is CharactersListUiAction.SetFavoriteGotCharacter -> setFavoriteForGotCharacter(
                action.gotCharacterId,
                action.isFavorite
            )
            is CharactersListUiAction.CharacterClicked -> _gotCharactersListUiState.update { it.copy(showCharacterDetails = action.characterId) }
            is CharactersListUiAction.OnBackPressed -> {
                if (_gotCharactersListUiState.value.showCharacterDetails != null) {
                    _gotCharactersListUiState.update { it.copy(showCharacterDetails = null) }
                } else {
                    _gotCharactersListUiState.update { it.copy(goBackToPreviousActivity = true) }
                }
            }
        }
    }

    fun characterDetailsAction(action: CharacterDetailsUiAction) {
        when (action) {
            is CharacterDetailsUiAction.SetFavoriteGotCharacter -> setFavoriteForGotCharacter(
                action.gotCharacterId,
                action.isFavorite
            )
        }
    }
}