package com.souvenotes.typematchups.core.localstorage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.souvenotes.typematchups.core.localstorage.entity.TypeMatchupEntity

@Dao
interface TypeMatchupDao {

    @Query("SELECT * FROM type_matchups WHERE matchupKey = :matchupKey")
    suspend fun getTypeMatchupForMatchupKey(matchupKey: String): TypeMatchupEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTypeMatchup(typeMatchup: TypeMatchupEntity)
}