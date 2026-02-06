package com.souvenotes.typematchups.core.data.di

import com.souvenotes.typematchups.core.data.repository.TypeMatchupRepository
import com.souvenotes.typematchups.core.data.repository.TypeMatchupRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TypeMatchupModule {

    @Binds
    @Singleton
    fun bindsTypeMatchupRepository(typeMatchupRepositoryImpl: TypeMatchupRepositoryImpl): TypeMatchupRepository
}