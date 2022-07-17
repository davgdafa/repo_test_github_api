package com.got.presentation.characters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.got.domain.models.GotCharacter
import com.got.presentation.R
import com.got.presentation.characters.ui.theme.GotCharactersAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersComposeActivity : ComponentActivity() {
    private val viewModel: CharactersViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = viewModel.gotCharactersListUiState.collectAsState().value
            val action = viewModel::charactersListAction

            if (state.goBackToPreviousActivity) {
                super.onBackPressed()
                return@setContent
            }

            GotCharactersAppTheme {
                GotCharacterApp(state = state, action)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.charactersListAction(CharactersListUiAction.GetCharactersList)
    }

    override fun onBackPressed() {
        viewModel.charactersListAction(CharactersListUiAction.OnBackPressed)
    }

    @Composable
    private fun GotCharacterApp(state: CharactersListUiState, action: (CharactersListUiAction) -> Unit) {
        if (state.showCharacterDetails != null && state.characters.isNotEmpty()) {
            GotCharacterDetails(character = state.characters[state.showCharacterDetails], height = 500.dp)
        } else {
            GotCharactersListScreen(state, action)
        }
    }

    @Composable
    private fun GotCharactersListScreen(state: CharactersListUiState, action: (CharactersListUiAction) -> Unit) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SearchBarStateful(state, action)
            CharactersViewStateful(state, action)
        }
    }

    @Composable
    private fun GotCharacterDetailsScreen(state: CharactersListUiState, action: (CharacterDetailsUiAction) -> Unit) {
        GotCharactersAppTheme {
            Column(modifier = Modifier.fillMaxSize()) {
                GotCharacterDetails(state.characters[state.showCharacterDetails!!]) // TODO should not be passed as null
            }
        }
    }

    @Composable
    fun CharactersViewStateful(state: CharactersListUiState, action: (CharactersListUiAction) -> Unit) {
        when {
            state.shouldShowLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = stringResource(R.string.characters_loading_text))
                }
            }
            state.characters.isNotEmpty() -> {
                val gotCharacters by remember {
                    mutableStateOf(state.characters)
                }
                CharactersListView(characters = gotCharacters, action)
            }
            !state.errorMessage.isNullOrBlank() -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { action(CharactersListUiAction.GetCharactersList) }) {
                        Text(text = "Try again")
                    }
                    Text(
                        text = "Game Of Thrones characters will show up here",
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxSize()
                            .align(alignment = Alignment.CenterHorizontally),
                    )
                }
            }
            else -> {}
        }
    }

    @Composable
    fun CharactersListView(characters: List<GotCharacter>, action: (CharactersListUiAction) -> Unit) {
        LazyColumn {
            items(items = characters, key = { character -> character.id }) {
                CharacterRow(it, action)
            }
        }
    }

    @Composable
    fun SearchBarStateful(state: CharactersListUiState, action: (CharactersListUiAction) -> Unit) {
        var isFavoritesFiltersOn by remember { mutableStateOf(false) }
        var queryValue by remember { mutableStateOf("") }

        SearchBar(
            actionPerformed = action,
            isFavoritesFiltersOn = isFavoritesFiltersOn,
            { isFavoritesFiltersOn = it },
            queryValue = queryValue,
            { queryValue = it }
        )
    }

    @Composable
    fun SearchBar(
        actionPerformed: (CharactersListUiAction) -> Unit,
        isFavoritesFiltersOn: Boolean,
        updateFavoritesFiltersOn: (Boolean) -> Unit,
        queryValue: String,
        updateQueryValue: (String) -> Unit
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = queryValue,
                maxLines = 1,
                onValueChange = {
                    if (it.length > 3) {
                        actionPerformed(CharactersListUiAction.Filter(queryValue, isFavoritesFiltersOn))
                    }
                    updateQueryValue(it)
                },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            updateFavoritesFiltersOn(!isFavoritesFiltersOn)
                            actionPerformed(
                                CharactersListUiAction.Filter(
                                    queryValue,
                                    !isFavoritesFiltersOn
                                )
                            )
                        },
                        painter = painterResource(R.drawable.ic_favorite),
                        tint = if (isFavoritesFiltersOn) Color.Red else Color.Gray,
                        contentDescription = null
                    )
                }
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun CharacterRow(character: GotCharacter, action: (CharactersListUiAction) -> Unit) {
        Card(
            elevation = 6.dp,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(8.dp),
            onClick = { action(CharactersListUiAction.CharacterClicked(character.id)) }
        ) {
            GotCharacterDetails(character)
        }
    }

    @Composable
    private fun GotCharacterDetails(character: GotCharacter, height: Dp = 300.dp) {
        Column {
            Row {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(height)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize(),
                        model = character.imageUrl,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        contentDescription = "Contact profile picture",
                        placeholder = painterResource(R.drawable.ic_filter)
                    )
                    if (character.isFavorite == true) {
                        Icon(
                            modifier = Modifier
                                .requiredSize(45.dp)
                                .align(Alignment.TopEnd),
                            painter = painterResource(R.drawable.ic_favorite),
                            tint = Color.Red,
                            contentDescription = null
                        )
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(text = "First name", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.requiredHeight(8.dp))
                    Text(text = "Last name", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.requiredHeight(8.dp))
                    Text(text = "Full name", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.requiredHeight(8.dp))
                    Text(text = "Title", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.requiredHeight(8.dp))
                    Text(text = "Family", fontWeight = FontWeight.Bold)
                }

                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = character.firstName)
                    Spacer(modifier = Modifier.requiredHeight(8.dp))
                    Text(text = character.lastName)
                    Spacer(modifier = Modifier.requiredHeight(8.dp))
                    Text(text = character.fullName)
                    Spacer(modifier = Modifier.requiredHeight(8.dp))
                    Text(text = character.title)
                    Spacer(modifier = Modifier.requiredHeight(8.dp))
                    Text(text = character.family)
                }
            }
        }
    }

    @Preview
    @Composable
    fun PreviewCharactersListScreen() {
        val character1 = GotCharacter(
            12321,
            "first",
            "last",
            "first and last",
            "title",
            "family",
            imageUrl = "no image"
        )
        val character2 = GotCharacter(
            123212,
            "first2",
            "last2",
            "first and last2",
            "title2",
            "family2",
            imageUrl = "no image2"
        )

        GotCharactersListScreen(CharactersListUiState(characters = listOf(character1, character2)), {})
    }

    @Preview
    @Composable
    private fun CharacterDetailsScreen() {
        val character1 = GotCharacter(
            12321,
            "first",
            "last",
            "first and last",
            "title",
            "family",
            imageUrl = "no image"
        )
        GotCharacterDetailsScreen(CharactersListUiState(characters = listOf(character1), showCharacterDetails = 0)) {}
    }

}
