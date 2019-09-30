package dev.anapsil

import dev.anapsil.auth.JwtService
import dev.anapsil.di.modules
import dev.anapsil.domain.DatabaseFactory
import dev.anapsil.domain.TaskRepository
import dev.anapsil.domain.UserRepository
import dev.anapsil.routing.login
import dev.anapsil.routing.tasks
import dev.anapsil.routing.users
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalLocationsAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    //Locations
    install(Locations)
    //Authentication
    install(Authentication)

    //ContentNegotiation
    install(ContentNegotiation) {
        gson {  }
    }


    install(Koin) {
        modules(listOf(modules))
    }

    //Inject dependencies
    val userRepository: UserRepository by inject()
    val taskRepository: TaskRepository by inject()
    val jwtService: JwtService by inject()

    //Init Database
    DatabaseFactory.init()

    //Setup authentication
    authentication {
            jwt("jwt") {
                verifier(jwtService.verifier)
                validate {
                    val payload = it.payload
                    val claim = payload.getClaim("email")
                    val claimString = claim.asString()

                    userRepository.getUserByEmail(claimString)
                }
            }
        }

    routing {
        login(userRepository, jwtService)
        authenticate("jwt") {
            users(userRepository)
            tasks(taskRepository)
        }
    }
}


