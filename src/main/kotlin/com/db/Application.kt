package com.db

import com.google.gson.Gson
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        Gson() // Use Gson for JSON serialization/deserialization
    }

    routing {
        // Define API routes
        factsRouting()
    }
}