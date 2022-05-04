package com.got.presentation.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.got.domain.models.GotCharacter
import com.got.domain.usecases.GetGotCharacterDetailsUseCase
import com.got.domain.usecases.SetBookmarkForCharacterUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterDetailsViewModel(
    private val getGotCharacterDetailsUseCase: GetGotCharacterDetailsUseCase,
    private val setBookmarkForCharacterUseCase: SetBookmarkForCharacterUseCase,
): ViewModel() {
    private val _gotCharacterDetailUiState = MutableStateFlow<CharacterUiState>(CharacterUiState.Empty)
    val gotCharacterDetailUiState: StateFlow<CharacterUiState> = _gotCharacterDetailUiState

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        _gotCharacterDetailUiState.value = CharacterUiState.Error(exception.message ?: "There was an unknown error during the process")
    }

    fun getGotCharacterDetails(gotCharacterId: Int) = viewModelScope.launch(errorHandler) {
        _gotCharacterDetailUiState.value = CharacterUiState.Loading
        withContext(Dispatchers.Default) {
            getGotCharacterDetailsUseCase(gotCharacterId).let { gotCharacterDetails ->
                _gotCharacterDetailUiState.value = CharacterUiState.Success(gotCharacterDetails)
            }
        }
    }

    fun setBookmark(id: Int, isFavorite: Boolean) {
        val errorHandler = CoroutineExceptionHandler { _, _ -> }
        GlobalScope.launch(errorHandler) {
            setBookmarkForCharacterUseCase(id, isFavorite)
        }
    }

    sealed class CharacterUiState {
        data class Success(val character: GotCharacter): CharacterUiState()
        data class Error(val message: String): CharacterUiState()
        object Loading: CharacterUiState()
        object Empty: CharacterUiState()
    }
}
