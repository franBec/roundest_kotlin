package dev.pollito.roundest_kotlin.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "spring.application")
class SpringApplicationConfigurationProperties {
  var name: String? = null
}
