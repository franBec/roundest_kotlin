package dev.pollito.roundest_kotlin.service

import dev.pollito.roundest_kotlin.model.Pokemons
import org.springframework.data.domain.PageRequest

interface PokemonService {
    fun findAll(pageRequest: PageRequest, random: Boolean): Pokemons

    fun incrementPokemonVotes(id: Long): Void?
}