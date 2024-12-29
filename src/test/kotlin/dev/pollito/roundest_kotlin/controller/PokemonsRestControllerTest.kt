package dev.pollito.roundest_kotlin.controller

import dev.pollito.roundest_kotlin.service.PokemonService
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class PokemonsRestControllerTest {

  private val pokemonService: PokemonService = mockk()
  private val pokemonsRestController = PokemonsRestController(pokemonService)

  @Test
  fun `when findAll then return OK`() {
    every {
      pokemonService.findAll(
          any<String>(), any<Int>(), any<Int>(), any<List<String>>(), any<Boolean>())
    } returns mockk()

    val response = pokemonsRestController.findAll(0, 10, emptyList(), true, "Bulbasaur")

    assertEquals(HttpStatus.OK, response.statusCode)
    assertNotNull(response.body)
  }

  @Test
  fun `when findById then return OK`() {
    every { pokemonService.findById(any<Long>()) } returns mockk()

    val response = pokemonsRestController.findById(1L)

    assertEquals(HttpStatus.OK, response.statusCode)
    assertNotNull(response.body)
  }

  @Test
  fun `when incrementPokemonVotes then return NO_CONTENT`() {
    justRun { pokemonService.incrementPokemonVotes(any<Long>()) }

    val response = pokemonsRestController.incrementPokemonVotes(1L)

    assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    assertEquals(Unit, response.body)
    verify { pokemonService.incrementPokemonVotes(any<Long>()) }
  }
}
