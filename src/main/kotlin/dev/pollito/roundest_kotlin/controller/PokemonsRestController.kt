package dev.pollito.roundest_kotlin.controller

import dev.pollito.roundest_kotlin.controllers.PokemonsController
import dev.pollito.roundest_kotlin.models.Pokemon
import dev.pollito.roundest_kotlin.models.Pokemons
import dev.pollito.roundest_kotlin.service.PokemonService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class PokemonsRestController(private val pokemonService: PokemonService) : PokemonsController {
  override fun findAll(
      pageNumber: Int,
      pageSize: Int,
      pageSort: List<String>,
      random: Boolean,
      name: String?
  ): ResponseEntity<Pokemons> {
    return ResponseEntity.ok(pokemonService.findAll(name, pageNumber, pageSize, pageSort, random))
  }

  override fun findById(id: Long): ResponseEntity<Pokemon> {
    return ResponseEntity.ok(pokemonService.findById(id))
  }

  override fun incrementPokemonVotes(id: Long): ResponseEntity<Unit> {
    return ResponseEntity(pokemonService.incrementPokemonVotes(id), HttpStatus.NO_CONTENT)
  }
}
