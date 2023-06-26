package com.example.demojwt.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    @Autowired
    lateinit var jwtAutFilter: JwtAuthenticationFilter

    @Autowired
    lateinit var authenticationProvider: AuthenticationProvider

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
        http.authorizeHttpRequests().requestMatchers("/api/**").permitAll()
        http.authorizeHttpRequests().requestMatchers("/**").authenticated()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.authenticationProvider(authenticationProvider)
        http.addFilterBefore(jwtAutFilter, UsernamePasswordAuthenticationFilter::class.java)


        return http.build()
    }

}