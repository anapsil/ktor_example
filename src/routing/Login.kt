package dev.anapsil.routing

import dev.anapsil.auth.JwtService
import dev.anapsil.domain.Credential
import dev.anapsil.domain.UserRepository
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route

@KtorExperimentalLocationsAPI
@Location("/login")
class Login

@KtorExperimentalLocationsAPI
fun Route.login(repository: UserRepository, jwtService: JwtService) {
    post<Login> {
            val payload = call.receive<Credential>()
            val user = repository.validate(payload.email, payload.password)

            if (user != null) {
                val token = jwtService.generateToken(user)
                call.respond(HttpStatusCode.OK, token)
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            }
        }
}