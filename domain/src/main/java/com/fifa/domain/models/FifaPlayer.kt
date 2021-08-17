package com.fifa.domain.models

data class FifaPlayer(
    val name: String,
    val imageUrl: String,
    val position: String,
    val rating: Int,
    val birthdate: String,
    val weight: String,
    val height: String,
    val currentTeamName: String,
    val nationality: String
)
