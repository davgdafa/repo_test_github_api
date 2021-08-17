package com.fifa.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fifa.domain.models.FifaPlayer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class PlayerDetailsViewModel: ViewModel() {
    private val _fifaPlayerDetailUiState = MutableStateFlow<PlayerUiState>(PlayerUiState.Empty)
    val fifaPlayerDetailUiState: StateFlow<PlayerUiState> = _fifaPlayerDetailUiState

    fun getFifaPlayerDetails() = viewModelScope.launch {
        _fifaPlayerDetailUiState.value = PlayerUiState.Loading
    }

    sealed class PlayerUiState {
        data class Success(val player: FifaPlayer): PlayerUiState()
        data class Error(val message: String): PlayerUiState()
        object Loading: PlayerUiState()
        object Empty: PlayerUiState()
    }
}
