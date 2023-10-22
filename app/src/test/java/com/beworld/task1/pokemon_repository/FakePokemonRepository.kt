package com.beworld.task1.pokemon_repository

import com.beworld.task1.data.remote.dto.Animated
import com.beworld.task1.data.remote.dto.BlackWhite
import com.beworld.task1.data.remote.dto.Crystal
import com.beworld.task1.data.remote.dto.DiamondPearl
import com.beworld.task1.data.remote.dto.DreamWorld
import com.beworld.task1.data.remote.dto.Emerald
import com.beworld.task1.data.remote.dto.FireredLeafgreen
import com.beworld.task1.data.remote.dto.GenerationI
import com.beworld.task1.data.remote.dto.GenerationIi
import com.beworld.task1.data.remote.dto.GenerationIii
import com.beworld.task1.data.remote.dto.GenerationIv
import com.beworld.task1.data.remote.dto.GenerationV
import com.beworld.task1.data.remote.dto.GenerationVi
import com.beworld.task1.data.remote.dto.GenerationVii
import com.beworld.task1.data.remote.dto.GenerationViii
import com.beworld.task1.data.remote.dto.Gold
import com.beworld.task1.data.remote.dto.HeartgoldSoulsilver
import com.beworld.task1.data.remote.dto.Home
import com.beworld.task1.data.remote.dto.Icons
import com.beworld.task1.data.remote.dto.OfficialArtwork
import com.beworld.task1.data.remote.dto.OmegarubyAlphasapphire
import com.beworld.task1.data.remote.dto.Other
import com.beworld.task1.data.remote.dto.Platinum
import com.beworld.task1.data.remote.dto.PokemonDetailDto
import com.beworld.task1.data.remote.dto.PokemonDto
import com.beworld.task1.data.remote.dto.RedBlue
import com.beworld.task1.data.remote.dto.RubySapphire
import com.beworld.task1.data.remote.dto.Silver
import com.beworld.task1.data.remote.dto.Species
import com.beworld.task1.data.remote.dto.Sprites
import com.beworld.task1.data.remote.dto.UltraSunUltraMoon
import com.beworld.task1.data.remote.dto.Versions
import com.beworld.task1.data.remote.dto.XY
import com.beworld.task1.data.remote.dto.Yellow
import com.beworld.task1.domain.repository.PokemonRepository

class FakePokemonRepository : PokemonRepository {

    // Only first of two Pokemon
    override suspend fun getPokemons(): List<PokemonDto> {
        return listOf(
            PokemonDto(
                "bulbasaur",
                "https://pokeapi.co/api/v2/pokemon/1/"
            ),
            PokemonDto(
                "ivysaur",
                "https://pokeapi.co/api/v2/pokemon/2/"
            )
        )
    }

    // Bulbasaur Pokemon
    override suspend fun getPokemonByName(pokemonName: String): PokemonDetailDto {
        return PokemonDetailDto(
            emptyList(),
            64,
            emptyList(),
            emptyList(),
            7,
            emptyList(),
            1,
            true,
            "https://pokeapi.co/api/v2/pokemon/1/encounters",
            emptyList(),
            "bulbasaur",
            1,
            emptyList(),
            Species("bulbasaur", "https://pokeapi.co/api/v2/pokemon-species/1/"),
            Sprites(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                Other(
                    DreamWorld("", ""),
                    Home("", "", "", ""),
                    OfficialArtwork("")
                ),
                Versions(
                    GenerationI(
                        RedBlue("", "", "", ""),
                        Yellow("", "", "", "")
                    ),
                    GenerationIi(
                        Crystal("", "", "", ""),
                        Gold("", "", "", ""),
                        Silver("", "", "", "")
                    ),
                    GenerationIii(
                        Emerald("", ""),
                        FireredLeafgreen("", "", "", ""),
                        RubySapphire("", "", "", "")
                    ),
                    GenerationIv(
                        DiamondPearl(
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            ""
                        ),
                        HeartgoldSoulsilver(
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            ""
                        ),
                        Platinum(
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            ""
                        )
                    ),
                    GenerationV(
                        BlackWhite(
                            Animated(
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                ""
                            ),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            ""
                        )
                    ),
                    GenerationVi(
                        OmegarubyAlphasapphire("", "", "", ""),
                        XY("", "", "", "")
                    ),
                    GenerationVii(
                        Icons("", ""),
                        UltraSunUltraMoon("", "", "", "")
                    ),
                    GenerationViii(Icons("", ""))
                )
            ),
            emptyList(),
            emptyList(),
            69
            )
    }
}