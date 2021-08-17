package com.got.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.got.domain.models.GotCharacter
import com.got.domain.usecases.GetGotCharactersUseCase
import com.got.domain.usecases.SearchCharactersUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

@ExperimentalCoroutinesApi
class CharactersViewModel : ViewModel() {
    private val getGotCharactersUseCase by inject(GetGotCharactersUseCase::class.java)
    private val searchCharactersUseCase by inject(SearchCharactersUseCase::class.java)

    private val _gotCharactersUiState = MutableStateFlow<CharactersUiState>(CharactersUiState.Empty)
    val gotCharactersUiState: StateFlow<CharactersUiState> = _gotCharactersUiState

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        _gotCharactersUiState.value = CharactersUiState.Error(exception.message ?: "There was an unknown error during the process")
    }

    fun getGotCharacters(setFilterOn: Boolean = false) = viewModelScope.launch(errorHandler) {
        _gotCharactersUiState.value = CharactersUiState.Loading
        withContext(Dispatchers.IO) {
            getGotCharactersUseCase().let { listOfCharacters ->
                if (listOfCharacters.isEmpty()) {
                    _gotCharactersUiState.value = CharactersUiState.Error("There were no characters retrieved")
                } else {
                    if (setFilterOn) {
                        _gotCharactersUiState.value = CharactersUiState.Success(listOfCharacters.filter { gotCharacter -> gotCharacter.isFavorite == true })
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
