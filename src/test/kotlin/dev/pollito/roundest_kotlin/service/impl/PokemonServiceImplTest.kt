package dev.pollito.roundest_kotlin.service.impl

import dev.pollito.roundest_kotlin.entity.Pokemon
import dev.pollito.roundest_kotlin.mapper.PokemonModelMapper
import dev.pollito.roundest_kotlin.repository.PokemonRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mapstruct.factory.Mappers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

@ExtendWith(MockitoExtension::class)
class PokemonServiceImplTest {

    @InjectMocks
    private lateinit var pokemonService: PokemonServiceImpl

    @Mock
    private lateinit var pokemonRepository: PokemonRepository

    @SuppressWarnings("unused")
    @Spy
    private var pokemonModelMapper: PokemonModelMapper = Mappers.getMapper(PokemonModelMapper::class.java)

    @Test
    fun `when findAll not random then return Pokemons`() {
        whenever(pokemonRepository.findAll(any<PageRequest>()))
            .thenReturn(PageImpl(emptyList(), PageRequest.of(0, 10), 0))

        assertNotNull(pokemonService.findAll(mock(), false))
    }

    @Test
    fun `when findAll random then return Pokemons`() {
        whenever(pokemonRepository.findByIds(any<List<Long>>())).thenReturn(emptyList())

        val pageRequest: PageRequest = mock()
        whenever(pageRequest.pageSize).thenReturn(2)

        assertNotNull(pokemonService.findAll(pageRequest, true))
    }

    @Test
    fun `when incrementPokemonVotes then return Void`() {
        val pokemon = Pokemon(name = "Bulbasaur", spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/705.png")
        val pokemonInitialVotes = pokemon.votes

        whenever(pokemonRepository.findById(any<Long>())).thenReturn(Optional.of(pokemon))
        whenever(pokemonRepository.save(any<Pokemon>())).thenReturn(pokemon)

        assertNull(pokemonService.incrementPokemonVotes(1L))
        assertEquals(pokemonInitialVotes + 1, pokemon.votes)
    }
}