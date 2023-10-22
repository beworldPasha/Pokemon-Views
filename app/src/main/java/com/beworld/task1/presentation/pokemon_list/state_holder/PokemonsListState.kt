package com.beworld.task1.presentation.pokemon_list.state_holder

import com.beworld.task1.domain.model.Pokemon

data class PokemonsListState(
    val isLoading: Boolean = false,
    val pokemons: List<Pokemon> = emptyList(),
    val error: String = ""
)