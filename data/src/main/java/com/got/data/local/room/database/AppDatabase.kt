package com.got.data.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.got.data.local.room.dao.GotCharacterDao
import com.got.data.local.room.entities.GotCharacterEntity

@Database(entities = [GotCharacterEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun gotCharacterDao(): GotCharacterDao
}
