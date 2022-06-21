package com.got.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.got.domain.models.GotCharacter
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
    private val _gotCharactersUiState = MutableStateFlow<CharactersUiState>(CharactersUiState.Empty)
    val gotCharactersUiState: StateFlow<CharactersUiState> = _gotCharactersUiState.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        _gotCharactersUiState.update {
            CharactersUiState.Error(
                exception.message ?: "There was an unknown error during the process"
            )
        }
    }

    private fun fetchGotCharacters() = viewModelScope.launch(errorHandler) {
        _gotCharactersUiState.value = CharactersUiState.Loading
        delay(2000)
        withContext(Dispatchers.IO) {
            getGotCharactersUseCase().let { listOfCharacters ->
                if (listOfCharacters.isEmpty()) {
                    _gotCharactersUiState.update { CharactersUiState.Error("There were no characters retrieved") } // TODO should show a text in the layout, not a message
                } else {
                    _gotCharactersUiState.update { CharactersUiState.Success(listOfCharacters) }
                }
            }
        }
    }

    private fun filterGotCharacters(query: String, isBookmarkFilterOn: Boolean = false) {
        viewModelScope.launch(errorHandler) {
            _gotCharactersUiState.update { CharactersUiState.Loading }
            withContext(Dispatchers.IO) {
                if (query.isNotBlank()) {
                    searchCharactersUseCase(query)
                } else {
                    getGotCharactersUseCase()
                }.let { listOfCharacters ->
                    if (listOfCharacters.isEmpty()) {
                    _gotCharactersUiState.update {
                        CharactersUiState.Error("There were no characters matching the filter criteria")
                    }
                    } else {
                        if (isBookmarkFilterOn) {
                            _gotCharactersUiState.update {
                                CharactersUiState.Success(listOfCharacters.filter { gotCharacter -> gotCharacter.isFavorite == true })
                            }
                        } else {
                            _gotCharactersUiState.update {
                                CharactersUiState.Success(listOfCharacters)
                            }
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

    fun actionPerformed(action: CharactersUiAction) {
        when (action) {
            is CharactersUiAction.Filter -> filterGotCharacters(
                action.query,
                action.isBookmarkFilterOn
            )
            CharactersUiAction.GetCharacters -> fetchGotCharacters()
            is CharactersUiAction.SetFavoriteGotCharacter -> setFavoriteForGotCharacter(
                action.gotCharacterId,
                action.isFavorite
            )
        }
    }


    sealed class CharactersUiState {
        data class Success(val characters: List<GotCharacter>) : CharactersUiState()
        data class Error(val message: String) : CharactersUiState()
        object Loading : CharactersUiState()
        object Empty : CharactersUiState()
    }
}

sealed class CharactersUiAction {
    class Filter(val query: String, val isBookmarkFilterOn: Boolean = false) : CharactersUiAction()
    object GetCharacters : CharactersUiAction()
    class SetFavoriteGotCharacter(
        val gotCharacterId: Int,
        val isFavorite: Boolean = true
    ) : CharactersUiAction()
}