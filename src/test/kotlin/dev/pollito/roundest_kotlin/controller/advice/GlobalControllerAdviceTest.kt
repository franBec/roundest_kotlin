package dev.pollito.roundest_kotlin.controller.advice

import com.ninjasquad.springmockk.MockkBean
import io.mockk.mockk
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.data.mapping.PropertyReferenceException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.resource.NoResourceFoundException

class GlobalControllerAdviceTest {
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
    fun `when ConstraintViolationException then return ProblemDetail`() {
        val exception: ConstraintViolationException = mockk()
        problemDetailAssertions(globalControllerAdvice.handle(exception), exception, HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `when generic Exception then return ProblemDetail`() {
        val exception: Exception = mockk()
        problemDetailAssertions(globalControllerAdvice.handle(exception), exception, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @Test
    fun `when MethodArgumentTypeMismatchException then return ProblemDetail`() {
        val exception: MethodArgumentTypeMismatchException = mockk()
        problemDetailAssertions(globalControllerAdvice.handle(exception), exception, HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `when NoResourceFoundException then return ProblemDetail`() {
        val exception: NoResourceFoundException = mockk()
        problemDetailAssertions(globalControllerAdvice.handle(exception), exception, HttpStatus.NOT_FOUND)
    }

    @Test
    fun `when NoSuchElementException then return ProblemDetail`() {
        val exception: NoSuchElementException = mockk()
        problemDetailAssertions(globalControllerAdvice.handle(exception), exception, HttpStatus.NOT_FOUND)
    }

    @Test
    fun `when PropertyReferenceException then return ProblemDetail`() {
        val exception: PropertyReferenceException = mockk()
        problemDetailAssertions(globalControllerAdvice.handle(exception), exception, HttpStatus.BAD_REQUEST)
    }
}