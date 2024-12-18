package dev.pollito.roundest_kotlin.filter

import dev.pollito.roundest_kotlin.configuration.properties.SpringApplicationConfigurationProperties
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(1)
class ApplicationNameHeaderFilter(
    private val springApplicationConfigurationProperties: SpringApplicationConfigurationProperties
) : Filter {

  override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
    if (response is HttpServletResponse) {
      response.addHeader("X-Application-Name", springApplicationConfigurationProperties.name)
    }
    chain.doFilter(request, response)
  }
}
