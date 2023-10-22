package com.beworld.task1.presentation.pokemon_list.state_holder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beworld.task1.common.Constants
import com.beworld.task1.common.Resource
import com.beworld.task1.domain.use_case.get_pokemons.GetPokemonsUseCase
import com.beworld.task1.domain.use_case.save_pokemon.SavePokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PokemonsViewModel @Inject constructor(
    private val getPokemonsUseCase: GetPokemonsUseCase,
    private val savePokemonUseCase: SavePokemonUseCase
) : ViewModel() {
    private val _pokemonsListStateHolder: MutableLiveData<PokemonsListState>
            by lazy {
                MutableLiveData<PokemonsListState>()
            }
    val pokemonsListStateHolder: LiveData<PokemonsListState>
        get() = _pokemonsListStateHolder

    private val _downloadStateHolder: MutableLiveData<DownloadingState>
            by lazy {
                MutableLiveData<DownloadingState>()
            }
    val downloadStateHolder: LiveData<DownloadingState>
        get() = _downloadStateHolder

    init {
        getPokemons()
    }

    private fun getPokemons() {
        getPokemonsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _pokemonsListStateHolder.value =
                        PokemonsListState(pokemons = result.data ?: emptyList(),)
                }

                is Resource.Error -> {
                    _pokemonsListStateHolder.value =
                        PokemonsListState(error = result.message ?: "An expected error")
                }

                is Resource.Loading -> {
                    _pokemonsListStateHolder.value = PokemonsListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun savePokemon(pokemonName: String) {
        savePokemonUseCase(pokemonName).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _downloadStateHolder.value = DownloadingState(
                        isDownloading = false,
                        downloadingResult = true
                    )

                    _downloadStateHolder.value = DownloadingState(
                        isDownloading = false,
                        downloadingResult = null
                    )
                }

                is Resource.Error -> {
                    _downloadStateHolder.value = DownloadingState(
                        isDownloading = false,
                        downloadingResult = false
                    )
                }

                is Resource.Loading -> {
                    _downloadStateHolder.value = DownloadingState(
                        isDownloading = true,
                        downloadingResult = null
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}