package com.fifa.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fifa.domain.models.FifaPlayer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class PlayersViewModel: ViewModel() {
    private val _fifaPlayersUiState = MutableStateFlow<PlayersUiState>(PlayersUiState.Empty)
    val fifaPlayersUiState: StateFlow<PlayersUiState> = _fifaPlayersUiState

    fun getFifaPlayers() = viewModelScope.launch {
        _fifaPlayersUiState.value = PlayersUiState.Loading
    }

    sealed class PlayersUiState {
        data class Success(val players: List<FifaPlayer>): PlayersUiState()
        data class Error(val message: String): PlayersUiState()
        object Loading: PlayersUiState()
        object Empty: PlayersUiState()
    }
}
