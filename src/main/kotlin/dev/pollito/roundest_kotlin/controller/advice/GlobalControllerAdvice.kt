package dev.pollito.roundest_kotlin.controller.advice

import dev.pollito.roundest_kotlin.aspect.LogAspect
import io.opentelemetry.api.trace.Span
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.data.mapping.PropertyReferenceException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.time.Instant
import java.time.format.DateTimeFormatter

@RestControllerAdvice
class GlobalControllerAdvice {
    companion object {
        private val log = LoggerFactory.getLogger(LogAspect::class.java)

        private fun buildProblemDetail(e: Exception, status: HttpStatus): ProblemDetail {
            val exceptionSimpleName = e.javaClass.simpleName
            log.error("{} being handled", exceptionSimpleName, e)
            val problemDetail = ProblemDetail.forStatusAndDetail(status, e.localizedMessage)
            problemDetail.title = exceptionSimpleName
            problemDetail.setProperty("timestamp", DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
            problemDetail.setProperty("trace", Span.current().spanContext.traceId)
            return problemDetail
        }
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handle(e: ConstraintViolationException): ProblemDetail {
        return buildProblemDetail(e, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handle(e: Exception): ProblemDetail {
        return buildProblemDetail(e, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handle(e: MethodArgumentTypeMismatchException): ProblemDetail {
        return buildProblemDetail(e, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handle(e: NoResourceFoundException): ProblemDetail {
        return buildProblemDetail(e, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handle(e: NoSuchElementException): ProblemDetail {
        return buildProblemDetail(e, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(PropertyReferenceException::class)
    fun handle(e: PropertyReferenceException): ProblemDetail {
        return buildProblemDetail(e, HttpStatus.BAD_REQUEST)
    }
}