package com.got.presentation.characters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import com.got.presentation.characters.ui.theme.GotCharactersAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersActivity : ComponentActivity() {
    private val viewModel: CharactersViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = viewModel.gotCharactersUiState.collectAsState().value

            if (state.goBackToPreviousActivity) {
                super.onBackPressed()
                return@setContent
            }

            GotCharactersAppTheme {
                GotCharacterApp(
                    state,
                    viewModel::actionPerformed
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.actionPerformed(CharactersUiAction.GetCharacters)
    }

    override fun onBackPressed() {
        viewModel.actionPerformed(CharactersUiAction.OnBackPressed)
    }
}
