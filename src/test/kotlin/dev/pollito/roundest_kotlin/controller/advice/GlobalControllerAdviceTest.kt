package dev.pollito.roundest_kotlin.controller.advice

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.resource.NoResourceFoundException

@ExtendWith(MockitoExtension::class)
class GlobalControllerAdviceTest {
    @InjectMocks
    private lateinit var globalControllerAdvice: GlobalControllerAdvice

    companion object {
        private fun problemDetailAssertions(
            response: ProblemDetail, e: Exception, httpStatus: HttpStatus
        ) {
            assertEquals(httpStatus.value(), response.status)
            assertEquals(e.javaClass.simpleName, response.title)
            assertNotNull(response.properties)
            assertNotNull(response.properties!!["timestamp"])
            assertNotNull(response.properties!!["trace"])
        }
    }

    @Test
    fun `when NoResourceFoundException then return ProblemDetail`() {
        val exception: NoResourceFoundException = mock()
        problemDetailAssertions(globalControllerAdvice.handle(exception), exception, HttpStatus.NOT_FOUND)
    }

    @Test
    fun `when NoSuchElementException then return ProblemDetail`() {
        val exception: NoSuchElementException = mock()
        problemDetailAssertions(globalControllerAdvice.handle(exception), exception, HttpStatus.NOT_FOUND)
    }

    @Test
    fun `when MethodArgumentNotValidException then return ProblemDetail`() {
        val exception: MethodArgumentNotValidException = mock()
        problemDetailAssertions(globalControllerAdvice.handle(exception), exception, HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `when MethodArgumentTypeMismatchException then return ProblemDetail`() {
        val exception: MethodArgumentTypeMismatchException = mock()
        problemDetailAssertions(globalControllerAdvice.handle(exception), exception, HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `when generic Exception then return ProblemDetail`() {
        val exception: Exception = mock()
        problemDetailAssertions(globalControllerAdvice.handle(exception), exception, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}