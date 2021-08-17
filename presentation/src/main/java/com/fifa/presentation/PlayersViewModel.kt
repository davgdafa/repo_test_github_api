package com.fifa.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fifa.domain.models.FifaPlayer
import com.fifa.domain.usecases.GetFifaPlayersUseCase
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
    private val getFifaPlayersUseCase by inject(GetFifaPlayersUseCase::class.java)
    private val _fifaPlayersUiState = MutableStateFlow<PlayersUiState>(PlayersUiState.Empty)
    val fifaPlayersUiState: StateFlow<PlayersUiState> = _fifaPlayersUiState

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        _fifaPlayersUiState.value = PlayersUiState.Error(exception.message ?: "There was an unknown error during the process")
    }

    fun getFifaPlayers() = viewModelScope.launch(errorHandler) {
        _fifaPlayersUiState.value = PlayersUiState.Loading
        withContext(Dispatchers.IO) {
            getFifaPlayersUseCase().let { listOfPlayers ->
                if (listOfPlayers.isEmpty()) {
                    _fifaPlayersUiState.value = PlayersUiState.Error("There were no players retrieved")
                } else {
                    _fifaPlayersUiState.value = PlayersUiState.Success(listOfPlayers)
                }
            }
        }
    }

    sealed class PlayersUiState {
        data class Success(val players: List<FifaPlayer>) : PlayersUiState()
        data class Error(val message: String) : PlayersUiState()
        object Loading : PlayersUiState()
        object Empty : PlayersUiState()
    }
}
