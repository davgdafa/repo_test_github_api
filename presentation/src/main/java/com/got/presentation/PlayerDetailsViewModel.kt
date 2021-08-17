package com.got.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.got.domain.models.GotPlayer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class PlayerDetailsViewModel: ViewModel() {
    private val _gotPlayerDetailUiState = MutableStateFlow<PlayerUiState>(PlayerUiState.Empty)
    val gotPlayerDetailUiState: StateFlow<PlayerUiState> = _gotPlayerDetailUiState

    fun getGotPlayerDetails() = viewModelScope.launch {
        _gotPlayerDetailUiState.value = PlayerUiState.Loading
    }

    sealed class PlayerUiState {
        data class Success(val player: GotPlayer): PlayerUiState()
        data class Error(val message: String): PlayerUiState()
        object Loading: PlayerUiState()
        object Empty: PlayerUiState()
    }
}
