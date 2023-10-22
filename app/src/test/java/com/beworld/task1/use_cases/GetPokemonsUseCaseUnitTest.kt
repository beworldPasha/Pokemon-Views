package com.beworld.task1.use_cases


import com.beworld.task1.common.Resource
import com.beworld.task1.domain.model.Pokemon
import com.beworld.task1.domain.use_case.get_pokemons.GetPokemonsUseCase
import com.beworld.task1.pokemon_repository.FakePokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class GetPokemonsUseCaseUnitTest {
    private lateinit var fakeRepository: FakePokemonRepository

    @Before
    fun setup() {
        fakeRepository = FakePokemonRepository()
    }

    @Test
    fun getPokemonsUseCase_firstOfTwoPokemon_SuccessResourceWithPokemons() {
        val results = GetPokemonsUseCase(fakeRepository).invoke()

        val targetResult = Resource.Success(
            listOf(
                Pokemon(1, "Bulbasaur"),
                Pokemon(2, "Ivysaur")
            )
        )
        val fakeFlow: Flow<Resource<List<Pokemon>>> = flow { emit(targetResult) }

        runBlocking {
            assertEquals(null, results.first().message)
            assertEquals(null, results.first().data?.size)
        }

    }
}