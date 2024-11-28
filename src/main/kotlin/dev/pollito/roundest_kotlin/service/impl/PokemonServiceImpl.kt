package dev.pollito.roundest_kotlin.service.impl

import dev.pollito.roundest_kotlin.mapper.PokemonModelMapper
import dev.pollito.roundest_kotlin.model.Pokemons
import dev.pollito.roundest_kotlin.repository.PokemonRepository
import dev.pollito.roundest_kotlin.service.PokemonService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class PokemonServiceImpl(
    private val pokemonRepository: PokemonRepository,
    private val pokemonModelMapper: PokemonModelMapper
) : PokemonService {

    companion object {
        private const val POKEMON_ID_MIN = 1
        private const val POKEMON_ID_MAX = 151
    }

    override fun findAll(pageRequest: PageRequest, random: Boolean): Pokemons {
        return if (random) {
            getRandomPokemons(pageRequest.pageSize)
        } else {
            pokemonModelMapper.map(pokemonRepository.findAll(pageRequest))
        }
    }

    private fun getRandomPokemons(size: Int): Pokemons {
        val pokemons = pokemonRepository.findByIds(generateRandomIds(size))
        return pokemonModelMapper.map(
            PageImpl(pokemons, PageRequest.of(0, size), pokemons.size.toLong())
        )
    }

    override fun incrementPokemonVotes(id: Long) {
        val pokemon = pokemonRepository.findById(id).orElseThrow()
        pokemon.votes += 1
        pokemonRepository.save(pokemon)
    }

    private fun generateRandomIds(count: Int): List<Long> {
        val ids = mutableSetOf<Long>()
        while (ids.size < count) {
            ids.add(POKEMON_ID_MIN + Random.nextLong((POKEMON_ID_MAX - POKEMON_ID_MIN).toLong()))
        }
        return ids.toList()
    }
}