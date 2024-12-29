package dev.pollito.roundest_kotlin.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class PokemonInterceptor : HandlerInterceptor {
  @Throws(IOException::class)
  override fun preHandle(
      request: HttpServletRequest,
      response: HttpServletResponse,
      handler: Any
  ): Boolean {
    if ("GET".equals(request.method, ignoreCase = true) && "/pokemons" == request.requestURI) {
      val queryString = request.queryString

      if (queryString == null || !queryString.contains("pageSort")) {
        val newQueryString =
            if ((queryString == null || queryString.isEmpty())) "pageSort=id:asc"
            else "$queryString&pageSort=id:asc"

        response.sendRedirect(request.requestURL.toString() + "?" + newQueryString)
        return false
      }
    }
    return true
  }
}
