package dev.pollito.roundest_kotlin.mapper

import dev.pollito.roundest_kotlin.entity.Pokemon
import dev.pollito.roundest_kotlin.model.Pokemons
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import org.springframework.data.domain.Page

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface PokemonModelMapper {
    fun map(pokemon: Pokemon): dev.pollito.roundest_kotlin.model.Pokemon
    fun map(pokemonPage: Page<Pokemon>?): Pokemons
}