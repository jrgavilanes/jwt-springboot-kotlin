package com.example.demojwt.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Int> {
    fun findUserByEmail(email: String): User?
}