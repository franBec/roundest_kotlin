package dev.pollito.roundest_kotlin.service

import dev.pollito.roundest_kotlin.entity.Pokemon
import dev.pollito.roundest_kotlin.mapper.PokemonModelMapper
import dev.pollito.roundest_kotlin.repository.PokemonRepository
import dev.pollito.roundest_kotlin.service.impl.PokemonServiceImpl
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

class PokemonServiceTest {

    private val pokemonRepository: PokemonRepository = mockk()
    private val pokemonModelMapper: PokemonModelMapper = spyk(Mappers.getMapper(PokemonModelMapper::class.java))
    private val pokemonService = PokemonServiceImpl(pokemonRepository, pokemonModelMapper)

    @Test
    fun `when findAll random then return Pokemons`() {
        every {
            pokemonRepository.findByIds(any<List<Long>>())
        } returns emptyList()

        assertNotNull(
            pokemonService.findAll(
                null,
                0,
                10,
                emptyList(),
                true
            )
        )
    }

    @Test
    fun `when findAll then return Pokemons`() {
        every {
            pokemonRepository.findAll(any<PageRequest>())
        } returns PageImpl(emptyList(), PageRequest.of(0, 10), 0)

        assertNotNull(
            pokemonService.findAll(
                null,
                0,
                10,
                emptyList(),
                false
            )
        )
    }

    @Test
    fun `when findAll with name then return Pokemons`() {
        every {
            pokemonRepository.findByNameContainingIgnoreCase(any<String>(), any<PageRequest>())
        } returns PageImpl(emptyList(), PageRequest.of(0, 10), 0)

        assertNotNull(
            pokemonService.findAll(
                "abra",
                0,
                10,
                emptyList(),
                false
            )
        )
    }

    @Test
    fun `when findById then return Pokemon`() {
        val pokemon = Pokemon(name = "Bulbasaur", spriteUrl = "url")
        every { pokemonRepository.findById(any<Long>()) } returns Optional.of(pokemon)

        assertNotNull(pokemonService.findById(1L))
    }

    @Test
    fun `when incrementPokemonVotes then return Void`() {
        val pokemon = Pokemon(name = "Bulbasaur", spriteUrl = "url")
        val pokemonInitialVotes = pokemon.votes

        every { pokemonRepository.findById(any<Long>()) } returns Optional.of(pokemon)
        every { pokemonRepository.save(any<Pokemon>()) } returns pokemon

        pokemonService.incrementPokemonVotes(1L)
        assertEquals(pokemonInitialVotes + 1, pokemon.votes)

        verify { pokemonRepository.findById(any<Long>()) }
        verify { pokemonRepository.save(any<Pokemon>()) }
    }
}
