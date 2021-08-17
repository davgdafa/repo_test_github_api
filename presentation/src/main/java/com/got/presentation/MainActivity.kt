package com.got.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.got.presentation.ui.theme.GotCharactersAppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class MainActivity : ComponentActivity() {
    private val viewModel: PlayersViewModel by viewModels()

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MyScreenContent {
                    viewModel.getGotPlayers()
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.gotPlayersUiState.collect { gotPlayersUiState ->
                when(gotPlayersUiState) {
                    is PlayersViewModel.PlayersUiState.Success -> {
                        // todo update ui with got players
                    }
                    is PlayersViewModel.PlayersUiState.Error -> {
                        // todo show api error
                    }
                    is PlayersViewModel.PlayersUiState.Loading -> {
                        // todo show loader
                    }
                    else -> Unit
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    GotCharactersAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}

@Composable
fun MyScreenContent(onClickAction: () -> Unit) {
    GetGotPlayersButton(onClickAction)
}

@Composable
fun GetGotPlayersButton(onClickAction: () -> Unit) {
    Button(onClick = onClickAction) {
        Text(text = "Get Got Players")
    }
}

@Composable
fun showLoader() {
    CircularProgressIndicator()
}

@Composable
fun hideLoader() {
    CircularProgressIndicator()
}

@Composable
fun GetPlayersButton(buttonAction: () -> Unit) {
    Button(
        onClick = buttonAction
    ) {}
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp {
        MyScreenContent {}
    }
}