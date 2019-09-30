package dev.anapsil.di

import dev.anapsil.auth.JwtService
import dev.anapsil.domain.TaskRepository
import dev.anapsil.domain.UserRepository
import org.koin.dsl.module

val modules = module {
    single { UserRepository() }
    single { TaskRepository() }
    single { JwtService() }
}