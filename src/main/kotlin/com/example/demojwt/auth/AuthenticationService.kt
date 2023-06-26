package com.example.demojwt.auth


import com.example.demojwt.config.JwtService
import com.example.demojwt.user.User
import com.example.demojwt.user.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    val repository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val jwtService: JwtService,
    val authenticationManager: AuthenticationManager
) {
    fun register(request: RegisterRequest): AuthenticationResponse? {
        val user = User()
        user.email = request.email
        user.firstName = request.firstname
        user.pass = passwordEncoder.encode(request.password)
        repository.save(user)

        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(token = jwtToken)
    }

    fun authenticate(request: AuthenticationRequest): AuthenticationResponse? {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.email, request.password))
        val user = repository.findUserByEmail(request.email) ?: throw UsernameNotFoundException("Usuario no encontrado")
        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(token = jwtToken)
    }
}