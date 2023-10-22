package com.beworld.task1.pokemon_repository

import com.beworld.task1.api.bulbasaurExpectedResponse
import com.beworld.task1.data.remote.PokemonApi
import com.beworld.task1.data.repository.PokemonRepositoryImpl
import com.beworld.task1.domain.repository.PokemonRepository
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.junit.Assert.*
import org.junit.Test

class PokemonRepositoryUnitTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: PokemonApi

    private lateinit var repository: PokemonRepository
    private lateinit var fakeRepository: FakePokemonRepository

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        api = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)

        repository = PokemonRepositoryImpl(api)
        fakeRepository = FakePokemonRepository()
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getPokemons_firstOfTwoPokemons_listOfAllPokemonsOfPokemonDto() {
        val response = """
            {"count": 1292, "next": "https://pokeapi.co/api/v2/pokemon/?offset=20&limit=20", "previous": null, "results": [{"name": "bulbasaur", "url": "https://pokeapi.co/api/v2/pokemon/1/" }, {"name": "ivysaur", "url": "https://pokeapi.co/api/v2/pokemon/2/" }, {"name": "venusaur", "url": "https://pokeapi.co/api/v2/pokemon/3/" }, {"name": "charmander", "url": "https://pokeapi.co/api/v2/pokemon/4/" }, {"name": "charmeleon", "url": "https://pokeapi.co/api/v2/pokemon/5/" }, {"name": "charizard", "url": "https://pokeapi.co/api/v2/pokemon/6/" }, {"name": "squirtle", "url": "https://pokeapi.co/api/v2/pokemon/7/" }, {"name": "wartortle", "url": "https://pokeapi.co/api/v2/pokemon/8/" }, {"name": "blastoise", "url": "https://pokeapi.co/api/v2/pokemon/9/" }, {"name": "caterpie", "url": "https://pokeapi.co/api/v2/pokemon/10/" }, {"name": "metapod", "url": "https://pokeapi.co/api/v2/pokemon/11/" }, {"name": "butterfree", "url": "https://pokeapi.co/api/v2/pokemon/12/" }, {"name": "weedle", "url": "https://pokeapi.co/api/v2/pokemon/13/" }, {"name": "kakuna", "url": "https://pokeapi.co/api/v2/pokemon/14/" }, {"name": "beedrill", "url": "https://pokeapi.co/api/v2/pokemon/15/" }, {"name": "pidgey", "url": "https://pokeapi.co/api/v2/pokemon/16/" }, {"name": "pidgeotto", "url": "https://pokeapi.co/api/v2/pokemon/17/" }, {"name": "pidgeot", "url": "https://pokeapi.co/api/v2/pokemon/18/" }, {"name": "rattata", "url": "https://pokeapi.co/api/v2/pokemon/19/" }, {"name": "raticate", "url": "https://pokeapi.co/api/v2/pokemon/20/" } ]}
        """.trimIndent()
        mockWebServer.enqueue(MockResponse().setBody(response))

        val results = runBlocking { repository.getPokemons() }
        val fakeResults = runBlocking { fakeRepository.getPokemons() }

        assertEquals(results[0].name, fakeResults[0].name)
        assertEquals(results[1].name, fakeResults[1].name)
    }

    @Test
    fun getPokemonByName_bulbasaurPokemon_bulbasaurPokemonDetailDto() {
        mockWebServer.enqueue(MockResponse().setBody(bulbasaurExpectedResponse))

        val result = runBlocking { repository.getPokemonByName("bulbasaur") }
        val fakeResult = runBlocking { fakeRepository.getPokemonByName("bulbasaur") }

        assertEquals(result.baseExperience, fakeResult.baseExperience)
        assertEquals(result.height, fakeResult.height)
        assertEquals(result.id, fakeResult.id)
        assertEquals(result.isDefault, fakeResult.isDefault)
        assertEquals(result.locationAreaEncounters, fakeResult.locationAreaEncounters)
        assertEquals(result.name, fakeResult.name)
        assertEquals(result.order, fakeResult.order)
        assertEquals(result.weight, fakeResult.weight)

        assertEquals(result.species.name, fakeResult.species.name)
        assertEquals(result.species.url, fakeResult.species.url)
    }
}