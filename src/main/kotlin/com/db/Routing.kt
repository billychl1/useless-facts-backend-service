package com.db

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.db.data.*
import com.google.gson.Gson

/**
 * Sets up the routing for facts-related endpoints.
 */
fun Routing.factsRouting() {
    route("/facts") {
        // Endpoint to fetch a random fact and return a shortened URL
        post {
            val fact = fetchRandomFact()
            FactCache.addFact(fact)
            println(fact)
            call.respondText(Gson().toJson(FactPostResponse(fact.originalFact, fact.shortenedUrl)), status = HttpStatusCode.OK)
        }

        // Endpoint to retrieve a cached fact by its shortened URL
        get("/{shortenedUrl}") {
            val shortenedUrl = call.parameters["shortenedUrl"]!!
            FactCache.incrementAccessCount(shortenedUrl)
            val fact = FactCache.getFact(shortenedUrl)

            if (fact != null) {
                call.respondText(Gson().toJson(FactGetResponse(fact.originalFact, fact.originalPermalink)), status = HttpStatusCode.OK)
            } else {
                call.respondText("Fact not found", status = HttpStatusCode.NotFound)
            }
        }

        // Endpoint to return all cached facts
        get {
            val allFacts = FactCache.getAllFacts()
            call.respondText(Gson().toJson(allFacts), status = HttpStatusCode.OK)
        }

        // Endpoint to redirect to the original fact and increment access count
        get("/{shortenedUrl}/redirect") {
            val shortenedUrl = call.parameters["shortenedUrl"]!!
            FactCache.incrementAccessCount(shortenedUrl)
            val fact = FactCache.getFact(shortenedUrl)

            if (fact != null) {
                call.respondRedirect(fact.originalPermalink)
            } else {
                call.respondText("Fact not found", status = HttpStatusCode.NotFound)
            }
        }
    }

    // Admin endpoint to retrieve access statistics
    route("/admin/statistics") {
        get {
            val stats = FactCache.getStatistics()
            call.respondText(Gson().toJson(stats), status = HttpStatusCode.OK)
        }
    }
}
