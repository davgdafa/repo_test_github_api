package com.got.presentation

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.got.presentation.databinding.ActivityMain2Binding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private val viewModel: PlayersViewModel by viewModels()
    private val loader: ProgressBar get() = binding.pbLoadingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setFunctionality()
        lifecycleScope.launchWhenStarted {
            viewModel.gotPlayersUiState.collect { gotPlayersUiState ->
                when (gotPlayersUiState) {
                    is PlayersViewModel.PlayersUiState.Success -> {
                        binding.rvPlayersList.adapter = PlayersAdapter(gotPlayersUiState.players)
                        loader.hide()
                    }
                    is PlayersViewModel.PlayersUiState.Error -> {
                        loader.hide()
                        Snackbar.make(binding.root, gotPlayersUiState.message, Snackbar.LENGTH_LONG).show()
                    }
                    is PlayersViewModel.PlayersUiState.Loading -> {
                        loader.show()
                        Snackbar.make(binding.root, "Loading players", Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getGotPlayers()
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

    private fun ProgressBar.show() {
        visibility = View.VISIBLE
    }

    private fun ProgressBar.hide() {
        visibility = View.GONE
    }
}
