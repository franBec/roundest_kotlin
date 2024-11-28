package dev.pollito.roundest_kotlin.aspect

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class LogAspect {

    companion object {
        private val log = LoggerFactory.getLogger(LogAspect::class.java)
    }

    @Pointcut("execution(public * dev.pollito.roundest_kotlin.controller..*.*(..))")
    fun controllerPublicMethodsPointcut() {
        // This function is intentionally left empty.
        // The purpose of this method is to act as a reusable pointcut definition.
        // It is referenced by other advice annotations (e.g., @Before, @AfterReturning)
        // to apply cross-cutting concerns at specific join points.
    }

    @Before("controllerPublicMethodsPointcut()")
    fun logBefore(joinPoint: JoinPoint) {
        log.info(
            "[{}] Args: {}",
            joinPoint.signature.toShortString(),
            joinPoint.args.joinToString(", ", "[", "]")
        )
    }

    @AfterReturning(pointcut = "controllerPublicMethodsPointcut()", returning = "result")
    fun logAfterReturning(joinPoint: JoinPoint, result: Any?) {
        log.info("[{}] Response: {}", joinPoint.signature.toShortString(), result)
    }
}