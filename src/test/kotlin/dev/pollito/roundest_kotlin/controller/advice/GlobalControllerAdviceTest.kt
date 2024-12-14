package dev.pollito.roundest_kotlin.controller.advice

import io.mockk.*
import io.opentelemetry.api.trace.Span
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.mapping.PropertyReferenceException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.time.Instant

class GlobalControllerAdviceTest {

    private lateinit var globalControllerAdvice: GlobalControllerAdvice

    @BeforeEach
    fun setup() {
        mockkStatic(Span::class)
        mockkStatic(ProblemDetail::class)
        mockkStatic(java.time.Instant::class)
        mockkStatic(java.time.format.DateTimeFormatter::class)

        globalControllerAdvice = GlobalControllerAdvice()
    }

    @Test
    fun `should handle ConstraintViolationException and return BAD_REQUEST`() {
        val exception = mockk<ConstraintViolationException>(relaxed = true)
        every { exception.localizedMessage } returns "Validation failed"

        val problemDetail = mockProblemDetail(HttpStatus.BAD_REQUEST, exception)
        every { ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.localizedMessage) } returns problemDetail

        val result = globalControllerAdvice.handle(exception)

        assertEquals(problemDetail, result)
//        verify { GlobalControllerAdvice.log.error("ConstraintViolationException being handled", exception) }
    }

    @Test
    fun `should handle Exception and return INTERNAL_SERVER_ERROR`() {
        val exception = mockk<Exception>(relaxed = true)
        every { exception.localizedMessage } returns "Validation failed"

        val problemDetail = mockProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception)
        every { ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.localizedMessage) } returns problemDetail

        val result = globalControllerAdvice.handle(exception)

        assertEquals(problemDetail, result)
//        verify { GlobalControllerAdvice.log.error("ConstraintViolationException being handled", exception) }
    }

    @Test
    fun `should handle MethodArgumentTypeMismatchException and return BAD_REQUEST`() {
        val exception = mockk<MethodArgumentTypeMismatchException>(relaxed = true)
        every { exception.localizedMessage } returns "Validation failed"

        val problemDetail = mockProblemDetail(HttpStatus.BAD_REQUEST, exception)
        every { ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.localizedMessage) } returns problemDetail

        val result = globalControllerAdvice.handle(exception)

        assertEquals(problemDetail, result)
//        verify { GlobalControllerAdvice.log.error("ConstraintViolationException being handled", exception) }
    }

    @Test
    fun `should handle NoResourceFoundException and return NOT_FOUND`() {
        val exception = mockk<NoResourceFoundException>(relaxed = true)
        every { exception.localizedMessage } returns "Validation failed"

        val problemDetail = mockProblemDetail(HttpStatus.NOT_FOUND, exception)
        every { ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.localizedMessage) } returns problemDetail

        val result = globalControllerAdvice.handle(exception)

        assertEquals(problemDetail, result)
//        verify { GlobalControllerAdvice.log.error("ConstraintViolationException being handled", exception) }
    }

    @Test
    fun `should handle NoSuchElementException and return NOT_FOUND`() {
        val exception = mockk<NoSuchElementException>(relaxed = true)
        every { exception.localizedMessage } returns "Validation failed"

        val problemDetail = mockProblemDetail(HttpStatus.NOT_FOUND, exception)
        every { ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.localizedMessage) } returns problemDetail

        val result = globalControllerAdvice.handle(exception)

        assertEquals(problemDetail, result)
//        verify { GlobalControllerAdvice.log.error("ConstraintViolationException being handled", exception) }
    }

    @Test
    fun `should handle PropertyReferenceException and return BAD_REQUEST`() {
        val exception = mockk<PropertyReferenceException>(relaxed = true)
        every { exception.localizedMessage } returns "Validation failed"

        val problemDetail = mockProblemDetail(HttpStatus.BAD_REQUEST, exception)
        every { ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.localizedMessage) } returns problemDetail

        val result = globalControllerAdvice.handle(exception)

        assertEquals(problemDetail, result)
//        verify { GlobalControllerAdvice.log.error("ConstraintViolationException being handled", exception) }
    }

    private fun mockProblemDetail(status: HttpStatus, exception: Exception): ProblemDetail {
        val problemDetail = mockk<ProblemDetail>(relaxed = true)

        val fixedTimestamp = "2023-01-01T00:00:00Z"
        val traceId = "traceId"

        every { problemDetail.setProperty("timestamp", fixedTimestamp) } just Runs
        every { problemDetail.setProperty("trace", traceId) } just Runs
        every { Span.current().spanContext.traceId } returns traceId

        return problemDetail
    }
}
