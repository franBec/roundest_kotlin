package dev.pollito.roundest_kotlin.controller

import dev.pollito.roundest_kotlin.model.PokemonSortProperty
import dev.pollito.roundest_kotlin.model.SortDirection
import dev.pollito.roundest_kotlin.service.PokemonService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus

@ExtendWith(MockitoExtension::class)
class PokemonsControllerTest {

    @InjectMocks
    private lateinit var pokemonsController: PokemonsController

    @Mock
    private lateinit var pokemonService: PokemonService

    @Test
    fun `when findAll then return OK`() {
        whenever(pokemonService.findAll(any<PageRequest>(), any<Boolean>())).thenReturn(mock())

        val response = pokemonsController.findAll(0, 10, PokemonSortProperty.ID, SortDirection.ASC, true)

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