package dev.pollito.roundest_kotlin.configuration

import dev.pollito.roundest_kotlin.interceptor.PokemonInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration(private val pokemonInterceptor: PokemonInterceptor) : WebMvcConfigurer {
  override fun addInterceptors(registry: InterceptorRegistry) {
    registry.addInterceptor(pokemonInterceptor)
  }
}
