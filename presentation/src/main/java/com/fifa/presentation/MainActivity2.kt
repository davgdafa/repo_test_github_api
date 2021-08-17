package com.fifa.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.fifa.presentation.databinding.ActivityMain2Binding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private val viewModel: PlayersViewModel by viewModels()
    private val loader: ContentLoadingProgressBar get() = binding.cpbLoadingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setFunctionality()
        lifecycleScope.launchWhenStarted {
            viewModel.fifaPlayersUiState.collect { fifaPlayersUiState ->
                when (fifaPlayersUiState) {
                    is PlayersViewModel.PlayersUiState.Success -> {
                        binding.rvPlayersList.adapter = PlayersAdapter(fifaPlayersUiState.players)
                        loader.hide2()
                    }
                    is PlayersViewModel.PlayersUiState.Error -> {
                        loader.hide2()
                        Snackbar.make(binding.root, fifaPlayersUiState.message, Snackbar.LENGTH_LONG).show()
                    }
                    is PlayersViewModel.PlayersUiState.Loading -> {
                        loader.show2()
                        Snackbar.make(binding.root, "Loading players", Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.cpbLoadingBar.visibility = View.VISIBLE
        viewModel.getFifaPlayers()
    }

    private fun setFunctionality() {
        binding.rvPlayersList.apply {
            val layoutManagerVariable = LinearLayoutManager(applicationContext)
            layoutManager = layoutManagerVariable
            itemAnimator = DefaultItemAnimator().apply { supportsChangeAnimations = false }
            // addItemDecoration(RecyclerViewDividerItemDecoration(this.context, layoutManagerVariable.orientation, 0))
            setItemViewCacheSize(20)
        }
    }

    private fun ContentLoadingProgressBar.show2() {
        visibility = View.VISIBLE
    }

    private fun ContentLoadingProgressBar.hide2() {
        visibility = View.GONE
    }
}

