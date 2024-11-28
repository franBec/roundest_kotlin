package dev.pollito.roundest_kotlin.repository

import dev.pollito.roundest_kotlin.entity.Pokemon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PokemonRepository : JpaRepository<Pokemon, Long> {
    @Query("SELECT p FROM Pokemon p WHERE p.id IN :ids")
    fun findByIds(@Param("ids") ids: List<Long?>?): List<Pokemon>
}