package dev.anapsil.domain

import io.ktor.auth.Principal
import org.jetbrains.exposed.sql.Table

//User model
data class User(
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String
) : Principal

//User table
object Users : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val name = varchar("name", 100)
    val email = varchar("email", 100).uniqueIndex()
    val password = varchar("password", 30)
}

//Credential
data class Credential(
    val email: String,
    val password: String
)

//Task model
data class Task(
    val id: Int = 0,
    val description: String,
    val status: Int = -1,
    val user: User
)

//Tasks table
object Tasks : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val description = varchar("description", 100)
    val status = integer("status")
    val userId = (integer("user_id") references Users.id)
}

