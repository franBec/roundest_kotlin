package dev.pollito.roundest_kotlin.service

import dev.pollito.roundest_kotlin.models.Pokemon
import dev.pollito.roundest_kotlin.models.Pokemons

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
