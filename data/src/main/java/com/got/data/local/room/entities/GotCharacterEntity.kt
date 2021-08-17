package com.got.data.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GotCharacterEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "full_name") val fullName: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "family") val family: String?,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean?
)
