package com.beworld.task1.presentation.pokemon_info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beworld.task1.common.Resource
import com.beworld.task1.domain.use_case.get_pokemon_detail.GetPokemonDetailUseCase
import com.beworld.task1.pokemosso.Pokemosso
import com.beworld.task1.presentation.pokemon_info.state_holder.PokemonDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val useCase: GetPokemonDetailUseCase
) : ViewModel() {
    private val _pokemonDetailStateHolder: MutableLiveData<PokemonDetailState>
            by lazy {
                MutableLiveData<PokemonDetailState>()
            }
    val pokemonsListStateHolder: LiveData<PokemonDetailState>
        get() = _pokemonDetailStateHolder


    fun fetchPokemon(pokemonName: String) {
        useCase(pokemonName).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _pokemonDetailStateHolder.value =
                        PokemonDetailState(pokemon = result.data)
                }

                is Resource.Error -> {
                    _pokemonDetailStateHolder.value =
                        PokemonDetailState(error = result.message ?: "An expected error")
                }

                is Resource.Loading -> {
                    _pokemonDetailStateHolder.value = PokemonDetailState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}