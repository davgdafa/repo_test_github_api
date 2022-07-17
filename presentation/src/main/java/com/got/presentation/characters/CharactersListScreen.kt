package com.got.presentation.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.got.domain.models.GotCharacter
import com.got.presentation.R

@Composable
internal fun GotCharactersListScreen(
    queryValue: String,
    setQueryValue: (String) -> Unit,
    isFavoritesFiltersOn: Boolean,
    setIsFavoriteFilterOn: (Boolean) -> Unit,
    state: CharactersUiState,
    action: (CharactersUiAction) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SearchBarStateful(
            queryValue,
            setQueryValue,
            isFavoritesFiltersOn,
            setIsFavoriteFilterOn,
            action
        )
        CharactersViewStateful(state, action, isFavoritesFiltersOn)
    }
}

@Composable
fun CharactersViewStateful(
    state: CharactersUiState,
    action: (CharactersUiAction) -> Unit,
    isFavoritesFiltersOn: Boolean
) {
    var gotCharacters by remember {
        mutableStateOf(state.characters)
    }
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
            gotCharacters = state.characters
            CharactersListView(characters = gotCharacters.values.toList(), action)
        }
        !state.errorMessage.isNullOrBlank() -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (!isFavoritesFiltersOn) {
                    Button(modifier = Modifier.padding(top = 8.dp), onClick = { action(CharactersUiAction.GetCharacters) }) {
                        Text(text = "Try again")
                    }
                }
                Text(
                    text = state.errorMessage,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .align(alignment = Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
            }
        }
        else -> {}
    }
}

@Composable
fun CharactersListView(
    characters: List<GotCharacter>,
    action: (CharactersUiAction) -> Unit
) {
    LazyColumn {
        itemsIndexed(items = characters) { index, character ->
            CharacterRow(character, action)
        }
    }
}

@Composable
fun SearchBarStateful(
    queryValue: String,
    setQueryValue: (String) -> Unit,
    isFavoritesFiltersOn: Boolean,
    setIsFavoriteFilterOn: (Boolean) -> Unit,
    action: (CharactersUiAction) -> Unit
) {
    SearchBar(
        actionPerformed = action,
        isFavoritesFiltersOn = isFavoritesFiltersOn,
        updateFavoritesFiltersOn = { setIsFavoriteFilterOn(it) },
        queryValue = queryValue,
        updateQueryValue = { setQueryValue(it) }
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
                actionPerformed(
                    CharactersUiAction.Filter(
                        queryValue,
                        isFavoritesFiltersOn
                    )
                )
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
fun CharacterRow(character: GotCharacter, action: (CharactersUiAction) -> Unit) {
    var isFavorite by remember { mutableStateOf(character.isFavorite) }
    isFavorite = character.isFavorite

    Card(
        elevation = 6.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(8.dp),
        onClick = { action(CharactersUiAction.CharacterClicked(character.id)) }
    ) {
        GotCharacterDetails(
            character,
            isFavorite = isFavorite
        ) {
            action(CharactersUiAction.SetFavoriteGotCharacter(character.id, !isFavorite))
        }
    }
}

@Preview
@Composable
fun CharactersListScreenPreview() {
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

    GotCharactersListScreen(
        "",
        {},
        false,
        {},
        state = CharactersUiState(
            characters = mapOf(
                character1.id to character1,
                character2.id to character2
            )
        ),
        action = {}
    )
}

@Preview
@Composable
fun EmptyCharactersListScreenPreview() {
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

    GotCharactersListScreen(
        "",
        {},
        false,
        {},
        state = CharactersUiState(
            characters = mapOf(),
            errorMessage = "Try again"
        ),
        action = {}
    )
}