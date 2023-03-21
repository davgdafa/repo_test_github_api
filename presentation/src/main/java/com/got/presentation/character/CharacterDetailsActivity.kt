package com.got.presentation.character

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.got.domain.models.GotCharacter
import com.got.presentation.databinding.ActivityCharacterDetailsBinding
import com.got.presentation.utils.ARGUMENT_GOT_CHARACTER_ID
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterDetailsBinding
    private val viewModel: CharacterDetailsViewModel by viewModel()
    private val loader: ProgressBar get() = binding.pbLoadingPlaceholder
    private var gotCharacterId: Int = 0
    private lateinit var currentGotCharacter: GotCharacter
    private val isFavorite: Boolean get() = currentGotCharacter.isFavorite ?: false

    companion object {
        fun getIntent(context: Context, gotCharacterId: Int): Intent =
            Intent(context, CharacterDetailsActivity::class.java).apply {
                putExtra(ARGUMENT_GOT_CHARACTER_ID, gotCharacterId)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gotCharacterId = intent.extras?.getInt(ARGUMENT_GOT_CHARACTER_ID)
            ?: throw IllegalArgumentException("$ARGUMENT_GOT_CHARACTER_ID not passed")

        setFunctionality()
        lifecycleScope.launchWhenResumed {
            viewModel.gotCharacterDetailUiState.collect { gotCharactersUiState ->
                when (gotCharactersUiState) {
                    is CharacterDetailsViewModel.CharacterUiState.Success -> {
                        setGotCharacterInformationInView(gotCharactersUiState.character)
                        loader.hide()
                    }
                    is CharacterDetailsViewModel.CharacterUiState.Error -> {
                        loader.hide()
                        Snackbar.make(binding.root, gotCharactersUiState.message, Snackbar.LENGTH_SHORT).show()
                        finish()
                    }
                    is CharacterDetailsViewModel.CharacterUiState.Loading -> {
                        loader.show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setGotCharacterInformationInView(gotCharacter: GotCharacter) {
        currentGotCharacter = gotCharacter
        binding.apply {
            Glide.with(binding.root)
                .load(gotCharacter.imageUrl)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pbLoadingPlaceholder.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pbLoadingPlaceholder.visibility = View.GONE
                        return false
                    }
                })
                .into(ivCharacterProfile)
            tvCharacterFirstName.text = gotCharacter.firstName
            tvCharacterLastName.text = gotCharacter.lastName
            tvCharacterFullName.text = gotCharacter.fullName
            tvCharacterTitle.text = gotCharacter.title
            tvCharacterFamily.text = gotCharacter.family
            updateBookmarkButtonText(gotCharacter.isFavorite ?: false)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getGotCharacterDetails(gotCharacterId)
    }

    private fun setFunctionality() {
        binding.btnBookmark.setOnClickListener {
            viewModel.setBookmark(currentGotCharacter.id, isFavorite = !isFavorite)
        }
    }

    private fun updateBookmarkButtonText(isFavorite: Boolean) {
        if (isFavorite) {
            binding.btnBookmark.text = "Remove Favorite"
        } else {
            binding.btnBookmark.text = "Set favorite"
        }
    }

    private fun ProgressBar.show() {
        visibility = View.VISIBLE
    }

    private fun ProgressBar.hide() {
        visibility = View.GONE
    }
}
