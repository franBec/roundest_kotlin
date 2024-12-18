package dev.pollito.roundest_kotlin.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "cors")
class CorsConfigurationProperties {
  var allowedOrigins: List<String>? = null
  var allowedMethods: List<String>? = null
  var allowedHeaders: String? = null
  var allowCredentials: Boolean? = null
}
