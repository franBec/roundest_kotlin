package dev.pollito.roundest_kotlin.service

import dev.pollito.roundest_kotlin.model.Pokemon
import dev.pollito.roundest_kotlin.model.Pokemons

interface PokemonService {
    fun findAll(
        name: String?,
        pageNumber: Int,
        pageSize: Int,
        pageSort: List<String>,
        random: Boolean
    ): Pokemons
    fun findById(id: Long): Pokemon
    fun incrementPokemonVotes(id: Long)
}