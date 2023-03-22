package com.got.presentation.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.got.domain.models.GotCharacter
import com.got.domain.usecases.GetGotCharacterDetailsUseCase
import com.got.domain.usecases.SetBookmarkForCharacterUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterDetailsViewModel(
    private val getGotCharacterDetailsUseCase: GetGotCharacterDetailsUseCase,
    private val setBookmarkForCharacterUseCase: SetBookmarkForCharacterUseCase,
): ViewModel() {
    private val _gotCharacterDetailUiState = MutableStateFlow<CharacterUiState>(CharacterUiState.Empty)
    val gotCharacterDetailUiState: StateFlow<CharacterUiState> = _gotCharacterDetailUiState.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        _gotCharacterDetailUiState.value = CharacterUiState.Error(exception.message ?: "There was an unknown error during the process")
    }

    fun getGotCharacterDetails(gotCharacterId: Int) = viewModelScope.launch(errorHandler) {
        _gotCharacterDetailUiState.value = CharacterUiState.Loading
        withContext(Dispatchers.IO) {
            getGotCharacterDetailsUseCase(gotCharacterId).let { gotCharacterDetails ->
                _gotCharacterDetailUiState.value = CharacterUiState.Success(gotCharacterDetails)
            }
        }
    }

    fun setBookmark(id: Int, isFavorite: Boolean) {
        val errorHandler = CoroutineExceptionHandler { _, _ ->
            _gotCharacterDetailUiState.update {
                CharacterUiState.Error("There was an error setting your favorite choice character")
            }
        }
        viewModelScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                if (setBookmarkForCharacterUseCase(id, isFavorite)) {
                    getGotCharacterDetails(id)
                }
            }
        }
    }

    sealed class CharacterUiState {
        data class Success(val character: GotCharacter): CharacterUiState()
        data class Error(val message: String): CharacterUiState()
        object Loading: CharacterUiState()
        object Empty: CharacterUiState()
    }
}
