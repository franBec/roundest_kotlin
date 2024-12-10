package dev.pollito.roundest_kotlin.service

import dev.pollito.roundest_kotlin.model.Pokemon
import dev.pollito.roundest_kotlin.model.Pokemons
import org.springframework.data.domain.PageRequest

interface PokemonService {
    fun findAll(name: String?, pageRequest: PageRequest, random: Boolean): Pokemons
    fun findById(id: Long): Pokemon
    fun incrementPokemonVotes(id: Long): Void?
}