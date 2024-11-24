package com.thatwaz.whoknew.di

import com.thatwaz.whoknew.data.repository.TriviaRepository
import com.thatwaz.whoknew.data.repository.TriviaRepositoryImpl


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTriviaRepository(
        impl: TriviaRepositoryImpl
    ): TriviaRepository
}
