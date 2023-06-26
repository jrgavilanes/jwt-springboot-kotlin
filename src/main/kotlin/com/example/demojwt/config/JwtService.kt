package com.example.demojwt.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import java.util.function.Function

@Service
class JwtService {

    fun extractUsername(token: String?): String? {
        return extractClaim(token) { obj: Claims? -> obj!!.subject }
    }

    fun <T> extractClaim(token: String?, claimsResolver: Function<Claims?, T>): T? {
        val claims = extractAllClaims(token!!)
        return claimsResolver.apply(claims)
    }

    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
        val now = Date(System.currentTimeMillis())
        val expiration = Date(System.currentTimeMillis() + 3600000)
        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun generateToken(userDetails: UserDetails): String {
        return generateToken(HashMap(), userDetails)
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username.equals(userDetails.username) && !isTokenExpired(token)

    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token)?.before(Date()) ?: true
    }

    private fun extractExpiration(token: String): Date? {
//        return extractClaim(token, Claims::getExpiration);
        return extractClaim(token) {
            it?.expiration
        }
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).body
    }

    private fun getSignInKey(): Key {
        val key = Decoders.BASE64.decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(key)
    }
}