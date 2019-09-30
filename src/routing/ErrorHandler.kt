package dev.anapsil.routing

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import org.jetbrains.exposed.exceptions.ExposedSQLException

suspend fun <R> PipelineContext<*, ApplicationCall>.errorAware(block: suspend () -> R): R? {
    return try {
        block()
    } catch (e: Exception) {
        val statusCode = when (e) {
            is ExposedSQLException -> HttpStatusCode.InternalServerError
            else -> HttpStatusCode.BadRequest
        }

        call.respond(statusCode, e.localizedMessage)
        null
    }
}