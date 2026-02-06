package com.souvenotes.typematchups.core.localstorage.di

import android.content.Context
import androidx.room.Room
import com.souvenotes.typematchups.core.localstorage.TypeMatchupsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {

    @Singleton
    @Provides
    fun provideZelDexDatabase(@ApplicationContext context: Context): TypeMatchupsDatabase {
        return Room.databaseBuilder(
            context,
            TypeMatchupsDatabase::class.java,
            "type_matchups_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideTypeMatchupDao(database: TypeMatchupsDatabase) = database.typeMatchupDao()
}