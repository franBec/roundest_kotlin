package dev.pollito.roundest_kotlin.controller

import dev.pollito.roundest_kotlin.service.PokemonService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import org.springframework.http.HttpStatus

@ExtendWith(MockitoExtension::class)
class PokemonsControllerTest {

    @InjectMocks
    private lateinit var pokemonsController: PokemonsController

    @Mock
    private lateinit var pokemonService: PokemonService

    @Test
    fun `when findAll then return OK`() {
        whenever(pokemonService.findAll(
            any<String>(),
            any<Int>(),
            any<Int>(),
            any<List<String>>(),
            any<Boolean>()
        )).thenReturn(mock())

        val response = pokemonsController.findAll(
            "Bulbasur",
            0,
            10,
            emptyList(),
            true
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun `when findById then return OK`(){
        whenever(pokemonService.findById(any<Long>())).thenReturn(mock())

        val response = pokemonsController.findById(1L)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun `when incrementPokemonVotes then return NO_CONTENT`() {
        doNothing().whenever(pokemonService).incrementPokemonVotes(any<Long>())

        val response = pokemonsController.incrementPokemonVotes(1L)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)

        verify(pokemonService).incrementPokemonVotes(any<Long>())
    }
}