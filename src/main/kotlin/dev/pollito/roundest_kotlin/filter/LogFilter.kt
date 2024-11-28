package dev.pollito.roundest_kotlin.filter

import dev.pollito.roundest_kotlin.aspect.LogAspect
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import java.io.IOException

class LogFilter : Filter {

    companion object {
        private val log = LoggerFactory.getLogger(LogAspect::class.java)
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(
        servletRequest: ServletRequest,
        servletResponse: ServletResponse,
        filterChain: FilterChain
    ) {
        logRequestDetails(servletRequest as HttpServletRequest)
        filterChain.doFilter(servletRequest, servletResponse)
        logResponseDetails(servletResponse as HttpServletResponse)
    }

    private fun logRequestDetails(request: HttpServletRequest) {
        log.info(
            ">>>> Method: {}; URI: {}; QueryString: {}; Headers: {}",
            request.method,
            request.requestURI,
            request.queryString,
            headersToString(request)
        )
    }

    fun headersToString(request: HttpServletRequest): String {
        val headerNames = request.headerNames
        val stringBuilder = StringBuilder("{")

        while (headerNames.hasMoreElements()) {
            val headerName = headerNames.nextElement()
            val headerValue = request.getHeader(headerName)

            stringBuilder.append(headerName).append(": ").append(headerValue)

            if (headerNames.hasMoreElements()) {
                stringBuilder.append(", ")
            }
        }

        stringBuilder.append("}")
        return stringBuilder.toString()
    }

    private fun logResponseDetails(response: HttpServletResponse) {
        log.info("<<<< Response Status: {}", response.status)
    }
}
