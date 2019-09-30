package dev.anapsil.routing

import dev.anapsil.domain.User
import dev.anapsil.domain.UserRepository
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route

@KtorExperimentalLocationsAPI
@Location("/users")
class Users

@KtorExperimentalLocationsAPI
fun Route.users(repository: UserRepository) {
get<Users> {
        errorAware {
            call.respond(HttpStatusCode.OK, repository.listUsers())
        }
    }

    post<Users> {
        errorAware {
            val payload = call.receive<User>()
            val createdUser = repository.createUser(payload)

            call.respond(HttpStatusCode.Created, createdUser)
        }
    }
}