package dev.pollito.roundest_kotlin.controller.advice

import io.opentelemetry.api.trace.Span
import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
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
class GlobalControllerAdvice(
    private val timestampProvider: TimestampProvider = DefaultTimestampProvider(),
    private val log: Logger = LoggerFactory.getLogger(GlobalControllerAdvice::class.java)
) {
    companion object {

        private fun buildProblemDetail(
            e: Exception,
            status: HttpStatus,
            timestampProvider: TimestampProvider,
            log: Logger
        ): ProblemDetail {
            val exceptionSimpleName = e.javaClass.simpleName
            log.error("{} being handled", exceptionSimpleName, e)
            val problemDetail = ProblemDetail.forStatusAndDetail(status, e.localizedMessage)
            problemDetail.title = exceptionSimpleName
            problemDetail.setProperty("timestamp", timestampProvider.now())
            problemDetail.setProperty("trace", Span.current().spanContext.traceId)
            return problemDetail
        }
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handle(e: ConstraintViolationException): ProblemDetail {
        return buildProblemDetail(e, HttpStatus.BAD_REQUEST, timestampProvider, log)
    }

    @ExceptionHandler(Exception::class)
    fun handle(e: Exception): ProblemDetail {
        return buildProblemDetail(e, HttpStatus.INTERNAL_SERVER_ERROR, timestampProvider, log)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handle(e: MethodArgumentTypeMismatchException): ProblemDetail {
        return buildProblemDetail(e, HttpStatus.BAD_REQUEST, timestampProvider, log)
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handle(e: NoResourceFoundException): ProblemDetail {
        return buildProblemDetail(e, HttpStatus.NOT_FOUND, timestampProvider, log)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handle(e: NoSuchElementException): ProblemDetail {
        return buildProblemDetail(e, HttpStatus.NOT_FOUND, timestampProvider, log)
    }

    @ExceptionHandler(PropertyReferenceException::class)
    fun handle(e: PropertyReferenceException): ProblemDetail {
        return buildProblemDetail(e, HttpStatus.BAD_REQUEST, timestampProvider, log)
    }
}

interface TimestampProvider {
    fun now(): String
}

class DefaultTimestampProvider : TimestampProvider {
    override fun now(): String = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
}
