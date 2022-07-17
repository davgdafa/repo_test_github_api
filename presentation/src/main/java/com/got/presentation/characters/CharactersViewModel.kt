package com.got.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.got.domain.usecases.GetGotCharacterDetailsUseCase
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
    private val getGotCharacterDetailsUseCase: GetGotCharacterDetailsUseCase
) : ViewModel() {
    private val _gotCharactersUiState = MutableStateFlow(CharactersUiState())
    val gotCharactersUiState: StateFlow<CharactersUiState> =
        _gotCharactersUiState.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        _gotCharactersUiState.update {
            it.copy(
                errorMessage = exception.message ?: "There was an unknown error during the process",
                shouldShowLoading = false
            )
        }
    }

    private fun fetchGotCharacters() = viewModelScope.launch(errorHandler) {
        delay(2000)
        withContext(Dispatchers.IO) {
            _gotCharactersUiState.update { it.copy(shouldShowLoading = true) }
            getGotCharactersUseCase().let { listOfCharacters ->
                if (listOfCharacters.isEmpty()) {
                    _gotCharactersUiState.update {
                        it.copy(
                            errorMessage = "There were no characters retrieved",
                            shouldShowLoading = false
                        )
                    } // TODO should show a text in the layout, not a message
                } else {
                    _gotCharactersUiState.update {
                        it.copy(
                            characters = listOfCharacters,
                            shouldShowLoading = false
                        )
                    }
                }
            }
        }
    }

    private suspend fun fetchGotCharacterInList(gotCharacterId: Int) =
        getGotCharactersUseCase().let { listOfCharacters ->
            if (listOfCharacters.isEmpty()) {
                _gotCharactersUiState.update {
                    it.copy(
                        errorMessage = "There were no characters retrieved",
                        shouldShowLoading = false
                    )
                } // TODO should show a text in the layout, not a message
            } else {
                _gotCharactersUiState.update {
                    it.copy(
                        characters = listOfCharacters,
                        shouldShowLoading = false,
                        showCharacterDetails = listOfCharacters[gotCharacterId]
                    )
                }
            }
        }

    private fun filterGotCharacters(query: String, isBookmarkFilterOn: Boolean = false) {
        viewModelScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                _gotCharactersUiState.update { it.copy(shouldShowLoading = true) }
                if (query.isNotBlank()) {
                    searchCharactersUseCase(query)
                } else {
                    getGotCharactersUseCase()
                }.let { listOfCharacters ->
                    if (listOfCharacters.isEmpty()) {
                        _gotCharactersUiState.update {
                            it.copy(
                                characters = emptyMap(),
                                errorMessage = "There were no characters matching the filter criteria",
                                shouldShowLoading = false
                            )
                        }
                    } else {
                        if (isBookmarkFilterOn) {
                            val favoriteCharacters =
                                listOfCharacters.filter { (_, character) -> character.isFavorite }
                            if (favoriteCharacters.isEmpty()) {
                                _gotCharactersUiState.update {
                                    it.copy(
                                        characters = emptyMap(),
                                        errorMessage = "There were no characters matching the filter criteria",
                                        shouldShowLoading = false
                                    )
                                }
                            } else {
                                _gotCharactersUiState.update {
                                    it.copy(
                                        characters = favoriteCharacters,
                                        shouldShowLoading = false
                                    )
                                }
                            }
                        } else {
                            _gotCharactersUiState.update {
                                it.copy(
                                    characters = listOfCharacters,
                                    shouldShowLoading = false
                                )
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
                fetchGotCharacterInList(gotCharacterId)
            }
        }

    private fun setFavoriteForGotCharacterList(gotCharacterId: Int, isFavorite: Boolean = true) =
        viewModelScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                setBookmarkForCharacterUseCase(id = gotCharacterId, isFavorite)
                getGotCharactersUseCase().let { listOfCharacters ->
                    _gotCharactersUiState.update {
                        it.copy(
                            characters = listOfCharacters, // When filters or query are on, we need to update the items
                            shouldShowLoading = false
                        )
                    }
                }
            }
        }

    fun getGotCharacterDetails(gotCharacterId: Int) = viewModelScope.launch(errorHandler) {
        withContext(Dispatchers.IO) {
            getGotCharacterDetailsUseCase(gotCharacterId).let { gotCharacterDetails ->
                _gotCharactersUiState.update {
                    it.copy(
                        characters = it.characters.toMutableMap()
                            .apply { put(gotCharacterDetails.id, gotCharacterDetails) },
                        shouldShowLoading = false
                    )
                }
            }
        }
    }

    fun actionPerformed(action: CharactersUiAction) {
        when (action) {
            is CharactersUiAction.Filter -> filterGotCharacters(
                action.query,
                action.isBookmarkFilterOn
            )
            CharactersUiAction.GetCharacters -> fetchGotCharacters()
            is CharactersUiAction.SetFavoriteGotCharacter -> setFavoriteForGotCharacterList(
                action.gotCharacterId,
                action.isFavorite
            )
            is CharactersUiAction.CharacterClicked -> _gotCharactersUiState.update {
                it.copy(
                    showCharacterDetails = it.characters[action.characterId],
                    shouldShowLoading = false
                )
            }
            is CharactersUiAction.OnBackPressed -> {
                if (_gotCharactersUiState.value.showCharacterDetails != null) {
                    _gotCharactersUiState.update { it.copy(showCharacterDetails = null) }
                } else {
                    _gotCharactersUiState.update { it.copy(goBackToPreviousActivity = true) }
                }
            }
            is CharactersUiAction.CharacterNotFound -> {
                _gotCharactersUiState.update {
                    it.copy(
                        errorMessage = "There was an error, please try again.",
                        shouldShowLoading = false
                    )
                }
            }
            is CharactersUiAction.SetFavoriteGotCharacterForDetails -> setFavoriteForGotCharacter(
                action.gotCharacterId,
                action.isFavorite
            )
        }
    }
}