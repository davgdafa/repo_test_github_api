package com.got.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class CharacterData(
    val id: Int?,
    val firstName: String?,
    val lastName: String?,
    val fullName: String?,
    val title: String?,
    val family: String?,
    val imageUrl: String?
)
