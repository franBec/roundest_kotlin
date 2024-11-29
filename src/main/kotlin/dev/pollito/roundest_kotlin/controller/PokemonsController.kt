package dev.pollito.roundest_kotlin.controller

import dev.pollito.roundest_kotlin.api.PokemonsApi
import dev.pollito.roundest_kotlin.model.PokemonSortProperty
import dev.pollito.roundest_kotlin.model.Pokemons
import dev.pollito.roundest_kotlin.model.SortDirection
import dev.pollito.roundest_kotlin.service.PokemonService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class PokemonsController(
    private val pokemonService: PokemonService
): PokemonsApi{
    override fun findAll(
        pageNumber: Int,
        pageSize: Int,
        sortProperty: PokemonSortProperty,
        sortDirection: SortDirection,
        random: Boolean
    ): ResponseEntity<Pokemons> {
        return ResponseEntity.ok(
            pokemonService.findAll(
                PageRequest.of(
                    pageNumber,
                    pageSize,
                    Sort.Direction.fromString(sortDirection.value),
                    sortProperty.value
                ),
                random
            )
        );
    }

    override fun incrementPokemonVotes(id: Long): ResponseEntity<Void> {
        return ResponseEntity<Void>(pokemonService.incrementPokemonVotes(id),HttpStatus.NO_CONTENT)
    }
}