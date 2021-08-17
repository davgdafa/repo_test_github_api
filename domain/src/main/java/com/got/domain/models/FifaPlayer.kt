package com.got.domain.models

data class GotPlayer(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val title: String,
    val family: String,
    val imageUrl: String
)
