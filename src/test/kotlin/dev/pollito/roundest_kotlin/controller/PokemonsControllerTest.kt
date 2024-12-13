package dev.pollito.roundest_kotlin.controller

import dev.pollito.roundest_kotlin.service.PokemonService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class PokemonsControllerTest {

    private var pokemonService: PokemonService = mockk()
    private var pokemonsController = PokemonsController(pokemonService)

    @Test
    fun `when findAll then return OK`() {
        every {
            pokemonService.findAll(
                any<String>(),
                any<Int>(),
                any<Int>(),
                any<List<String>>(),
                any<Boolean>()
            )
        } returns mockk()

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
        every {
            pokemonService.findById(any<Long>())
        } returns mockk()

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