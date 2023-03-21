package com.got.presentation.characters

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.got.presentation.character.CharacterDetailsActivity
import com.got.presentation.databinding.ActivityCharactersBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharactersBinding
    private val viewModel: CharactersViewModel by viewModel()
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
                        loader.hide().also { binding.tvLoadingText.visibility = View.GONE }
                    }
                    is CharactersViewModel.CharactersUiState.Error -> {
                        showEmptyListLayout()
                        loader.hide().also { binding.tvLoadingText.visibility = View.GONE }
                        Snackbar.make(binding.root, gotCharactersUiState.message, Snackbar.LENGTH_SHORT).show()
                    }
                    is CharactersViewModel.CharactersUiState.Loading -> {
                        hideEmptyListLayout()
                        loader.show().also { binding.tvLoadingText.visibility = View.VISIBLE }
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
        viewModel.actionPerformed(CharactersUiAction.GetCharacters)
    }

    private fun setFunctionality() {
        binding.rvCharactersList.apply {
            val layoutManagerVariable = LinearLayoutManager(applicationContext)
            layoutManager = layoutManagerVariable
            itemAnimator = DefaultItemAnimator().apply { supportsChangeAnimations = false }
            setItemViewCacheSize(20)
        }
        binding.btnRetry.setOnClickListener {
            viewModel.actionPerformed(CharactersUiAction.GetCharacters)
        }
        binding.svSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.takeIf { it.isNotBlank() }?.let { viewModel.actionPerformed(CharactersUiAction.Filter(query)) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.takeIf { it.isNotBlank() }?.let { viewModel.actionPerformed(CharactersUiAction.Filter(newText)) }
                return true
            }
        })
        binding.ivFilter.setOnClickListener {
            viewModel.actionPerformed(CharactersUiAction.Filter("", !isFavoritesFiltersOn))
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
