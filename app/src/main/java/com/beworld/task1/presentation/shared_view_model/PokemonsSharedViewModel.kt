package com.beworld.task1.presentation.shared_view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PokemonsSharedViewModel: ViewModel() {
    private val pokemonName = MutableLiveData<String>()

    fun setName(otherName: String) {
        pokemonName.value = otherName
    }

    fun getName() = pokemonName.value
}