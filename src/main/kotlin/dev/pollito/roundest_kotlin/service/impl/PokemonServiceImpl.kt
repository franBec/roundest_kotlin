package dev.pollito.roundest_kotlin.service.impl

import dev.pollito.roundest_kotlin.mapper.PokemonModelMapper
import dev.pollito.roundest_kotlin.models.Pokemon
import dev.pollito.roundest_kotlin.models.Pokemons
import dev.pollito.roundest_kotlin.repository.PokemonRepository
import dev.pollito.roundest_kotlin.service.PokemonService
import dev.pollito.roundest_kotlin.util.PageableUtils
import dev.pollito.roundest_kotlin.util.RandomUtils.generateRandomIds
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PokemonServiceImpl(
    private val pokemonRepository: PokemonRepository,
    private val pokemonModelMapper: PokemonModelMapper
) : PokemonService {

  override fun findAll(
      name: String?,
      pageNumber: Int,
      pageSize: Int,
      pageSort: List<String>,
      random: Boolean
  ): Pokemons {
    if (random) {
      return getRandomPokemons(pageSize)
    }

    val pageable: Pageable = PageableUtils.createPageable(pageNumber, pageSize, pageSort)

    return if (name.isNullOrBlank()) {
      pokemonModelMapper.map(pokemonRepository.findAll(pageable))
    } else {
      pokemonModelMapper.map(pokemonRepository.findByNameContainingIgnoreCase(name, pageable))
    }
  }

  override fun findById(id: Long): Pokemon {
    return pokemonModelMapper.map(pokemonRepository.findById(id).orElseThrow())
  }

  override fun incrementPokemonVotes(id: Long) {
    val pokemon = pokemonRepository.findById(id).orElseThrow()
    pokemon.votes += 1
    pokemonRepository.save(pokemon)
  }

  private fun getRandomPokemons(size: Int): Pokemons {
    val pokemons = pokemonRepository.findByIds(generateRandomIds(size))
    return pokemonModelMapper.map(
        PageImpl(pokemons, PageRequest.of(0, size), pokemons.size.toLong()))
  }
}
