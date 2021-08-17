package com.fifa.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fifa.presentation.databinding.ActivityMain2Binding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private val viewModel: PlayersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setFunctionality()
        lifecycleScope.launchWhenStarted {
            viewModel.fifaPlayersUiState.collect { fifaPlayersUiState ->
                when(fifaPlayersUiState) {
                    is PlayersViewModel.PlayersUiState.Success -> {
                        Snackbar.make(binding.root, "It worked. Players: ${fifaPlayersUiState.players}", Snackbar.LENGTH_LONG).show()
                        // todo update ui with fifa players
                    }
                    is PlayersViewModel.PlayersUiState.Error -> {
                        Snackbar.make(binding.root, fifaPlayersUiState.message, Snackbar.LENGTH_LONG).show()
                        // todo show api error
                    }
                    is PlayersViewModel.PlayersUiState.Loading -> {
                        Snackbar.make(binding.root, "Loading players", Snackbar.LENGTH_LONG).show()
                        // todo show loader
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setFunctionality() {
        binding.btnGetPlayers.setOnClickListener {
            viewModel.getFifaPlayers()
        }
    }
}
