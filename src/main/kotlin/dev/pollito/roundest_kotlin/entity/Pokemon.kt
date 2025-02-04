package dev.pollito.roundest_kotlin.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.io.Serializable

@Entity
@Table(name = "pokemons", schema = "public")
data class Pokemon(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,
    @Column(name = "name", nullable = false, length = 10, updatable = false, unique = true)
    val name: String,
    @Column(name = "sprite_url", nullable = false, updatable = false, unique = true)
    val spriteUrl: String,
    @Column(name = "votes", nullable = false) var votes: Int = 0
) : Serializable
