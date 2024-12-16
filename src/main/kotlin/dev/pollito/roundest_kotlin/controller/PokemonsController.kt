package dev.pollito.roundest_kotlin.controller

import dev.pollito.roundest_kotlin.api.PokemonsApi
import dev.pollito.roundest_kotlin.model.Pokemon
import dev.pollito.roundest_kotlin.model.Pokemons
import dev.pollito.roundest_kotlin.service.PokemonService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class PokemonsController(
    private val pokemonService: PokemonService
): PokemonsApi{
    override fun findAll(
        name: String?,
        pageNumber: Int,
        pageSize: Int,
        pageSort: List<String>,
        random: Boolean
    ): ResponseEntity<Pokemons> {
        return ResponseEntity.ok(
            pokemonService.findAll(
                name,
                pageNumber,
                pageSize,
                pageSort,
                random
            )
        )
    }

    override fun findById(id: Long): ResponseEntity<Pokemon> {
        return ResponseEntity.ok(pokemonService.findById(id))
    }

    override fun incrementPokemonVotes(id: Long): ResponseEntity<Void> {
        pokemonService.incrementPokemonVotes(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}