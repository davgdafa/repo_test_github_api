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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.got.domain.models.GotCharacter
import com.got.presentation.R
//import com.got.presentation.character.CharacterDetailsActivity
import com.got.presentation.characters.ui.theme.GotCharactersAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersComposeActivity : ComponentActivity() {
    private val viewModel: CharactersViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GotCharactersAppTheme {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SearchBarStateful(viewModel::actionPerformed)
                    CharactersViewStateful()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.actionPerformed(CharactersUiAction.GetCharacters)
    }

    @Composable
    fun CharactersViewStateful() {
        when (val gotCharacterUiState = viewModel.gotCharactersUiState.collectAsState().value) {
            is CharactersViewModel.CharactersUiState.Success -> {
                val gotCharacters by remember {
                    mutableStateOf(gotCharacterUiState.characters)
                }
                CharactersListView(characters = gotCharacters)
            }
            is CharactersViewModel.CharactersUiState.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { viewModel.actionPerformed(CharactersUiAction.GetCharacters) }) {
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
            is CharactersViewModel.CharactersUiState.Loading -> {
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
            else -> Unit
        }
    }

    @Composable
    fun CharactersListView(characters: List<GotCharacter>) {
        LazyColumn {
            items(items = characters, key = { character -> character.id }) {
                CharacterRow(it)
            }
        }
    }

    @Composable
    fun SearchBarStateful(actionPerformed: (CharactersUiAction) -> Unit) {
        var isFavoritesFiltersOn by remember { mutableStateOf(false) }
        var queryValue by remember { mutableStateOf("") }

        SearchBar(
            actionPerformed = actionPerformed,
            isFavoritesFiltersOn = isFavoritesFiltersOn,
            { isFavoritesFiltersOn = it },
            queryValue = queryValue,
            { queryValue = it }
        )
    }

    @Composable
    fun SearchBar(
        actionPerformed: (CharactersUiAction) -> Unit,
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
                        actionPerformed(CharactersUiAction.Filter(queryValue, isFavoritesFiltersOn))
                    } else if (it.isBlank()) {
                        actionPerformed(CharactersUiAction.Filter(it, isFavoritesFiltersOn))
                    }
                    updateQueryValue(it)
                },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            updateFavoritesFiltersOn(!isFavoritesFiltersOn)
                            actionPerformed(
                                CharactersUiAction.Filter(
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
    fun CharacterRow(character: GotCharacter) {
        Card(
            elevation = 6.dp,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(8.dp),
            onClick = { /*launchGotCharacterDetailsActivity(character.id)*/ }
        ) {
            Column {
                Row {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredHeight(300.dp)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize(),
                            model = character.imageUrl,
                            contentScale = ContentScale.FillWidth,
                            contentDescription = "Contact profile picture",
                            placeholder = painterResource(R.drawable.ic_favorite)
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
    }

//    private fun launchGotCharacterDetailsActivity(gotCharacterId: Int) {
//        startActivity(
//            CharacterDetailsActivity.getIntent(
//                this@CharactersComposeActivity,
//                gotCharacterId
//            )
//        )
//    }

    @Preview
    @Composable
    fun DefaultPreview() {
        GotCharactersAppTheme {
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
            Column(modifier = Modifier.fillMaxWidth()) {
                SearchBar(
                    actionPerformed = {},
                    isFavoritesFiltersOn = true,
                    updateFavoritesFiltersOn = {},
                    queryValue = "Targaryen",
                    updateQueryValue = {}
                )
                CharactersListView(characters = listOf(character1, character2))
            }
        }
    }
}
