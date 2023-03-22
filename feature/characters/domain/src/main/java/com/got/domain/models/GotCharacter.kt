package com.got.domain.models

data class GotCharacter(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val title: String,
    val family: String,
    val imageUrl: String,
    val isFavorite: Boolean = false
)
