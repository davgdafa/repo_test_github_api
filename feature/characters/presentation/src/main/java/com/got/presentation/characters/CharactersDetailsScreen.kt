package com.got.presentation.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.got.domain.models.GotCharacter
import com.got.presentation.R

@Composable
internal fun GotCharacterDetails(
    character: GotCharacter,
    height: Dp = 300.dp,
    isFavorite: Boolean,
    setFavorite: () -> Unit
) {
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
                if (isFavorite) {
                    Icon(
                        modifier = Modifier
                            .requiredSize(45.dp)
                            .align(Alignment.TopEnd)
                            .clickable {
                                setFavorite()
                            },
                        painter = painterResource(R.drawable.ic_favorite),
                        tint = Color.Red,
                        contentDescription = null
                    )
                } else {
                    Button(
                        onClick = { setFavorite() }, modifier = Modifier
                            .align(Alignment.TopEnd).padding(end = 8.dp)
                    ) {
                        Text(text = "Set Favorite")
                    }
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
private fun CharacterDetailsScreenPreview() {
    val character1 = GotCharacter(
        12321,
        "first",
        "last",
        "first and last",
        "title",
        "family",
        imageUrl = "no image"
    )
    GotCharacterDetails(
        character = character1,
        isFavorite = false,
        setFavorite = {}
    )
}