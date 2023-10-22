package com.beworld.task1.presentation.pokemon_info.state_holder

import com.beworld.task1.domain.model.Pokemon
import com.beworld.task1.domain.model.PokemonDetail

data class PokemonDetailState(
    val isLoading: Boolean = false,
    val pokemon: PokemonDetail? = null,
    val error: String = ""
)
