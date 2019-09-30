package dev.anapsil.routing

import dev.anapsil.domain.Task
import dev.anapsil.domain.TaskRepository
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
@Location("/tasks")
class Tasks

@KtorExperimentalLocationsAPI
fun Route.tasks(taskRepository: TaskRepository) {
    get<Tasks> {
        errorAware {
            call.respond(HttpStatusCode.OK, taskRepository.listAll())
        }
    }

    post<Tasks> {
        errorAware {
            val payload = call.receive<Task>()
            val createdTask = taskRepository.createTask(payload)

            call.respond(HttpStatusCode.Created, createdTask)
        }
    }
}