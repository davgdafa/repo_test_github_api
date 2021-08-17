package com.got.presentation.characters

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.got.presentation.character.CharacterDetailsActivity
import com.got.presentation.databinding.ActivityCharactersBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class CharactersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharactersBinding
    private val viewModel: CharactersViewModel by viewModels()
    private val loader: ProgressBar get() = binding.pbLoadingBar
    private var isFavoritesFiltersOn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharactersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFunctionality()
        lifecycleScope.launchWhenStarted {
            viewModel.gotCharactersUiState.collect { gotCharactersUiState ->
                when (gotCharactersUiState) {
                    is CharactersViewModel.CharactersUiState.Success -> {
                        hideEmptyListLayout()
                        binding.rvCharactersList.adapter =
                            CharactersAdapter(gotCharactersUiState.characters, ::launchGotCharacterDetailsActivity)
                        loader.hide()
                    }
                    is CharactersViewModel.CharactersUiState.Error -> {
                        showEmptyListLayout()
                        loader.hide()
                        Snackbar.make(binding.root, gotCharactersUiState.message, Snackbar.LENGTH_SHORT).show()
                    }
                    is CharactersViewModel.CharactersUiState.Loading -> {
                        loader.show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showEmptyListLayout() {
        binding.tvEmptyList.visibility = View.VISIBLE
        binding.btnRetry.visibility = View.VISIBLE
        binding.rvCharactersList.visibility = View.GONE
    }

    private fun hideEmptyListLayout() {
        binding.tvEmptyList.visibility = View.GONE
        binding.btnRetry.visibility = View.GONE
        binding.rvCharactersList.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        binding.svSearchBar.setQuery("", false)
        viewModel.getGotCharacters()
    }

    private fun setFunctionality() {
        binding.rvCharactersList.apply {
            val layoutManagerVariable = LinearLayoutManager(applicationContext)
            layoutManager = layoutManagerVariable
            itemAnimator = DefaultItemAnimator().apply { supportsChangeAnimations = false }
            setItemViewCacheSize(20)
        }
        binding.btnRetry.setOnClickListener {
            viewModel.getGotCharacters()
        }
        binding.svSearchBar.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchCharacters(query) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.searchCharacters(newText) }
                return true
            }
        })
        binding.ivFilter.setOnClickListener {
            viewModel.getGotCharacters(!isFavoritesFiltersOn)
            isFavoritesFiltersOn = !isFavoritesFiltersOn
            if (isFavoritesFiltersOn) {
                Snackbar.make(binding.root, "Favorites filter on", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.root, "Favorites filter off", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun launchGotCharacterDetailsActivity(gotCharacterId: Int) {
        startActivity(CharacterDetailsActivity.getIntent(this@CharactersActivity, gotCharacterId))
    }

    private fun ProgressBar.show() {
        visibility = View.VISIBLE
    }

    private fun ProgressBar.hide() {
        visibility = View.GONE
    }
}
