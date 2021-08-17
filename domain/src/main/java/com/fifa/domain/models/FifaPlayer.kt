package com.fifa.domain.models

data class FifaPlayer(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val position: String,
    val rating: Int,
    val birthdate: String,
    val age: Int,
    val weight: Int,
    val height: Int,
    val clubName: String,
    val nationality: String,
    val leagueName: String,
)
