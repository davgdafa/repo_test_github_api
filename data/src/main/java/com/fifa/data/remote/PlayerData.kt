package com.fifa.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class PlayerData(
    val commonName: String?,
    val league: LeagueData?,
    val nation: NationData?,
    val club: ClubData?,
    val headshot: ImageData?,
    val height: Int?,
    val weight: Int?,
    val birthdate: String?,
    val age: Int?,
    val positionFull: String?,
    val baseId: Int?,
    val rating: Int?
)