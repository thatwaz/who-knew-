package com.thatwaz.whoknew.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.thatwaz.whoknew.data.network.TriviaApiService


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://opentdb.com/"

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Singleton
    @Provides
    fun provideTriviaApiService(retrofit: Retrofit): TriviaApiService =
        retrofit.create(TriviaApiService::class.java)
}
