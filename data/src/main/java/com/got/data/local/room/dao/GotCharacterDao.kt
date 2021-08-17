package com.got.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.got.data.local.room.entities.GotCharacterEntity

@Dao
interface GotCharacterDao {
    @Query("SELECT * FROM gotcharacterentity")
    fun getAll(): List<GotCharacterEntity>

    @Insert
    fun insertAll(characters: List<GotCharacterEntity>)

    @Query("SELECT * FROM gotcharacterentity WHERE id LIKE :id LIMIT 1")
    fun findGotCharacterById(id: Int): GotCharacterEntity

    @Query("UPDATE gotcharacterentity SET is_favorite = :isFavorite WHERE id = :id")
    fun updateCharacterById(id: Int, isFavorite: Boolean)
}
