package com.got.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.got.domain.models.GotPlayer
import com.got.domain.usecases.GetGotPlayersUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

@ExperimentalCoroutinesApi
class PlayersViewModel : ViewModel() {
    private val getGotPlayersUseCase by inject(GetGotPlayersUseCase::class.java)
    private val _gotPlayersUiState = MutableStateFlow<PlayersUiState>(PlayersUiState.Empty)
    val gotPlayersUiState: StateFlow<PlayersUiState> = _gotPlayersUiState

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        _gotPlayersUiState.value = PlayersUiState.Error(exception.message ?: "There was an unknown error during the process")
    }

    fun getGotPlayers() = viewModelScope.launch(errorHandler) {
        _gotPlayersUiState.value = PlayersUiState.Loading
        withContext(Dispatchers.IO) {
            getGotPlayersUseCase().let { listOfPlayers ->
                if (listOfPlayers.isEmpty()) {
                    _gotPlayersUiState.value = PlayersUiState.Error("There were no players retrieved")
                } else {
                    _gotPlayersUiState.value = PlayersUiState.Success(listOfPlayers)
                }
            }
        }
    }

    sealed class PlayersUiState {
        data class Success(val players: List<GotPlayer>) : PlayersUiState()
        data class Error(val message: String) : PlayersUiState()
        object Loading : PlayersUiState()
        object Empty : PlayersUiState()
    }
}
