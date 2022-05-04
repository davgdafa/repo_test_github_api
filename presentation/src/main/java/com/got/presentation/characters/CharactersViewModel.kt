package com.got.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.got.domain.models.GotCharacter
import com.got.domain.usecases.GetGotCharactersUseCase
import com.got.domain.usecases.SearchCharactersUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class CharactersViewModel(
    private val getGotCharactersUseCase: GetGotCharactersUseCase,
    private val searchCharactersUseCase: SearchCharactersUseCase
) : ViewModel() {
    private val _gotCharactersUiState = MutableStateFlow<CharactersUiState>(CharactersUiState.Empty)
    val gotCharactersUiState: StateFlow<CharactersUiState> = _gotCharactersUiState

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        _gotCharactersUiState.value = CharactersUiState.Error(exception.message ?: "There was an unknown error during the process")
    }

    fun getGotCharacters(setFilterOn: Boolean = false) = viewModelScope.launch(errorHandler) {
        _gotCharactersUiState.value = CharactersUiState.Loading
        delay(5000)
        withContext(Dispatchers.IO) {
            getGotCharactersUseCase().let { listOfCharacters ->
                if (listOfCharacters.isEmpty()) {
//                    _gotCharactersUiState.value = CharactersUiState.Error("There were no characters retrieved") // TODO should show a text in the layout, not a message
                } else {
                    if (setFilterOn) {
                        _gotCharactersUiState.value =
                            CharactersUiState.Success(listOfCharacters.filter { gotCharacter -> gotCharacter.isFavorite == true })
                    } else {
                        _gotCharactersUiState.value = CharactersUiState.Success(listOfCharacters)
                    }
                }
            }
        }
    }

    fun searchCharacters(query: String) = viewModelScope.launch(errorHandler) {
        withContext(Dispatchers.Default) {
            searchCharactersUseCase(query).let { listOfCharacters ->
                if (listOfCharacters.isEmpty()) {
                    _gotCharactersUiState.value = CharactersUiState.Error("There were no characters retrieved")
                } else {
                    _gotCharactersUiState.value = CharactersUiState.Success(listOfCharacters)
                }
            }
        }
    }

    sealed class CharactersUiState {
        data class Success(val characters: List<GotCharacter>) : CharactersUiState()
        data class Error(val message: String) : CharactersUiState()
        object Loading : CharactersUiState()
        object Empty : CharactersUiState()
    }
}
