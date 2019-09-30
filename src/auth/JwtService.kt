package dev.anapsil.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.anapsil.domain.User
import java.util.*

class JwtService {
    private val issuer = "ktorexample"
    private val secret = System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier = JWT.require(algorithm).withIssuer(issuer).build()

    fun generateToken(user: User) = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("email", user.email)
        .withExpiresAt(expiresAt())
        .sign(algorithm)

    private fun expiresAt() = Date(System.currentTimeMillis() + 3_600_000 * 24) //24 hours

}