package dev.anapsil.domain

import dev.anapsil.domain.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class TaskRepository {

    suspend fun listAll() = dbQuery {
        (Tasks innerJoin Users).selectAll().mapNotNull { it.toTask() }
    }

    suspend fun getTaskById(id:Int) = dbQuery {
        (Tasks innerJoin Users).select { Tasks.id eq id }.map { it.toTask() }.single()
    }

    suspend fun createTask(task: Task) : Task  {
            val key =  dbQuery {
            Tasks.insert {
                it[description] = task.description
                it[status] = task.status
                it[userId] = task.user.id
            } get Tasks.id
        }

        return getTaskById(key)
    }
}

fun ResultRow.toTask() = Task(
    this[Tasks.id],
    this[Tasks.description],
    this[Tasks.status],
    User(this[Tasks.userId],this[Users.name],this[Users.email],this[Users.password])
)