package com.beworld.task1.api

import com.beworld.task1.data.remote.PokemonApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.junit.Assert.*

class PokemonApiUnitTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: PokemonApi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        api = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getPokemons_response_ListOfPokemons() {
        val expectedResponse = """
            {"count": 1292, "next": "https://pokeapi.co/api/v2/pokemon/?offset=20&limit=20", "previous": null, "results": [{"name": "bulbasaur", "url": "https://pokeapi.co/api/v2/pokemon/1/" }, {"name": "ivysaur", "url": "https://pokeapi.co/api/v2/pokemon/2/" }, {"name": "venusaur", "url": "https://pokeapi.co/api/v2/pokemon/3/" }, {"name": "charmander", "url": "https://pokeapi.co/api/v2/pokemon/4/" }, {"name": "charmeleon", "url": "https://pokeapi.co/api/v2/pokemon/5/" }, {"name": "charizard", "url": "https://pokeapi.co/api/v2/pokemon/6/" }, {"name": "squirtle", "url": "https://pokeapi.co/api/v2/pokemon/7/" }, {"name": "wartortle", "url": "https://pokeapi.co/api/v2/pokemon/8/" }, {"name": "blastoise", "url": "https://pokeapi.co/api/v2/pokemon/9/" }, {"name": "caterpie", "url": "https://pokeapi.co/api/v2/pokemon/10/" }, {"name": "metapod", "url": "https://pokeapi.co/api/v2/pokemon/11/" }, {"name": "butterfree", "url": "https://pokeapi.co/api/v2/pokemon/12/" }, {"name": "weedle", "url": "https://pokeapi.co/api/v2/pokemon/13/" }, {"name": "kakuna", "url": "https://pokeapi.co/api/v2/pokemon/14/" }, {"name": "beedrill", "url": "https://pokeapi.co/api/v2/pokemon/15/" }, {"name": "pidgey", "url": "https://pokeapi.co/api/v2/pokemon/16/" }, {"name": "pidgeotto", "url": "https://pokeapi.co/api/v2/pokemon/17/" }, {"name": "pidgeot", "url": "https://pokeapi.co/api/v2/pokemon/18/" }, {"name": "rattata", "url": "https://pokeapi.co/api/v2/pokemon/19/" }, {"name": "raticate", "url": "https://pokeapi.co/api/v2/pokemon/20/" } ]}
        """.trimIndent()
        mockWebServer.enqueue(MockResponse().setBody(expectedResponse))

        val response = runBlocking { api.getPokemons() }

        assertEquals(20, response.results.size)

        assertEquals("bulbasaur", response.results[0].name)

        assertEquals("venusaur", response.results[2].name)
        assertEquals("https://pokeapi.co/api/v2/pokemon/3/", response.results[2].url)

        assertEquals("raticate", response.results[19].name)
    }

    @Test
    fun getPokemonByName_CorrectName_PokemonDetailDtoOfBulbasaur() {
        mockWebServer.enqueue(MockResponse().setBody(bulbasaurExpectedResponse))

        val response = runBlocking { api.getPokemonByName("bulbasaur") }

        assertEquals("bulbasaur", response.name)
        assertEquals(1, response.id)
        assertEquals(7, response.height)
    }

    @Test
    fun getPokemonByName_notCorrectName_retrofit2HttpExceptionHTTP404() {
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        val response = assertThrows(java.lang.Exception::class.java) {
            runBlocking { api.getPokemonByName("sdpffd") }
        }

        assertEquals("HTTP 404 ", response.message)
    }
}