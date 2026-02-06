package com.souvenotes.typematchups.core.localstorage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.souvenotes.typematchups.core.localstorage.dao.TypeMatchupDao
import com.souvenotes.typematchups.core.localstorage.entity.TypeMatchupEntity

@Database(entities = [TypeMatchupEntity::class], version = 1, exportSchema = false)
abstract class TypeMatchupsDatabase : RoomDatabase() {

    abstract fun typeMatchupDao(): TypeMatchupDao
}