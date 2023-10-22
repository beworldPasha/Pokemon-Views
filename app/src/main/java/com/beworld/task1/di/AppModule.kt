package com.beworld.task1.di


import com.beworld.task1.data.local.database.dao.PokemonDao
import com.beworld.task1.data.remote.PokemonApi
import com.beworld.task1.data.repository.PokemonRepositoryImpl
import com.beworld.task1.domain.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePokemonApi(): PokemonApi {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)
    }

    @Provides
    @Singleton
    fun providePokemonRepository(api: PokemonApi, dao: PokemonDao): PokemonRepository =
        PokemonRepositoryImpl(api, dao)
}