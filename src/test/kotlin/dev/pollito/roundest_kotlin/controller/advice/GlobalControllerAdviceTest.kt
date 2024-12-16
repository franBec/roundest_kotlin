package dev.pollito.roundest_kotlin.controller.advice

import dev.pollito.roundest_kotlin.util.TimestampUtils
import io.mockk.mockk
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.springframework.data.mapping.PropertyReferenceException
import org.springframework.http.HttpStatus
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.resource.NoResourceFoundException

class GlobalControllerAdviceTest {

    private lateinit var advice: GlobalControllerAdvice
    private lateinit var mockTimestampUtils: TimestampUtils
    private lateinit var mockLogger: Logger

    @BeforeEach
    fun setup() {
        mockTimestampUtils = mockk(relaxed = true)
        mockLogger = mockk(relaxed = true)
        advice = GlobalControllerAdvice(mockTimestampUtils, mockLogger)
    }

    private fun assertProblemDetail(
        exception: Exception,
        expectedStatus: HttpStatus
    ) {
        val problemDetail = when (exception) {
            is ConstraintViolationException -> advice.handle(exception)
            is MethodArgumentTypeMismatchException -> advice.handle(exception)
            is NoResourceFoundException -> advice.handle(exception)
            is NoSuchElementException -> advice.handle(exception)
            is PropertyReferenceException -> advice.handle(exception)
            else -> advice.handle(exception)
        }

        assertEquals(expectedStatus.value(), problemDetail.status)
        assertEquals(exception.javaClass.simpleName, problemDetail.title)
        assertNotNull(problemDetail.detail)
        assertNotNull(problemDetail.properties!!["timestamp"])
        assertNotNull(problemDetail.properties!!["trace"])
    }

    @Test
    fun `handle ConstraintViolationException returns BAD_REQUEST ProblemDetail`() {
        assertProblemDetail(mockk<ConstraintViolationException>(relaxed = true), HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `handle generic Exception returns INTERNAL_SERVER_ERROR ProblemDetail`() {
        assertProblemDetail(mockk<Exception>(relaxed = true), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @Test
    fun `handle MethodArgumentTypeMismatchException returns BAD_REQUEST ProblemDetail`() {
        assertProblemDetail(mockk<MethodArgumentTypeMismatchException>(relaxed = true), HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `handle NoResourceFoundException returns NOT_FOUND ProblemDetail`() {
        assertProblemDetail(mockk<NoResourceFoundException>(relaxed = true), HttpStatus.NOT_FOUND)
    }

    @Test
    fun `handle NoSuchElementException returns NOT_FOUND ProblemDetail`() {
        assertProblemDetail(mockk<NoSuchElementException>(relaxed = true), HttpStatus.NOT_FOUND)
    }

    @Test
    fun `handle PropertyReferenceException returns BAD_REQUEST ProblemDetail`() {
        assertProblemDetail(mockk<PropertyReferenceException>(relaxed = true), HttpStatus.BAD_REQUEST)
    }
}
