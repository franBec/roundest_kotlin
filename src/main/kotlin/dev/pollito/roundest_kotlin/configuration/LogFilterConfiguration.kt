package dev.pollito.roundest_kotlin.configuration

import dev.pollito.roundest_kotlin.filter.LogFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LogFilterConfiguration {
    @Bean
    fun loggingFilter(): FilterRegistrationBean<LogFilter> {
        val registrationBean: FilterRegistrationBean<LogFilter> = FilterRegistrationBean<LogFilter>()

        registrationBean.filter = LogFilter()
        registrationBean.addUrlPatterns("/*")

        return registrationBean
    }
}