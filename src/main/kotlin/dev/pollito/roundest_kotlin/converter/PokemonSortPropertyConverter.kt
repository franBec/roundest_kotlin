package dev.pollito.roundest_kotlin.converter

import dev.pollito.roundest_kotlin.model.PokemonSortProperty
import org.springframework.core.convert.converter.Converter

class PokemonSortPropertyConverter : Converter<String,PokemonSortProperty > {
    override fun convert(source: String): PokemonSortProperty {
        return PokemonSortProperty.fromValue(source)
    }
}