package com.example.demojwt.auth

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class BorrarController(val service: AuthenticationService) {
    @GetMapping("")
    fun hola(): String {
        return "hola"
    }
}