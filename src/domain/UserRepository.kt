package dev.anapsil.domain

import dev.anapsil.domain.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*

class UserRepository {

    suspend fun listUsers() = dbQuery {
        Users.selectAll().map { it.toUser() }
    }

    suspend fun validate(email: String, password: String) = dbQuery {
        Users.select { Users.email.eq(email) and Users.password.eq(password) }
            .mapNotNull { it.toUser() }
            .singleOrNull()
    }

    suspend fun getUserById(id: Int) = dbQuery {
        Users.select { Users.id eq id }.map { it.toUser() }.singleOrNull()
    }

    suspend fun getUserByEmail(email: String) = dbQuery {
        Users.select { Users.email.eq(email) }.map { it.toUser() }.singleOrNull()
    }

    suspend fun createUser(user: User): User {

        val existUser = getUserByEmail(user.email)
        existUser?.let {
            if (it.id != 0) {
                return it
            }
        }

        var key = 0
        dbQuery {
            key = Users.insert {
                it[name] = user.name
                it[email] = user.email
                it[password] = user.password
            } get Users.id
        }

        return getUserById(key)!!
    }
}

fun ResultRow.toUser() = User(
    this[Users.id],
    this[Users.name],
    this[Users.email],
    this[Users.password]
)