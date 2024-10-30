package com.db

import com.db.data.*
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Initialize the HttpClient once
val client = HttpClient {
    install(ContentNegotiation) {
        Gson() // Use Gson for JSON serialization/deserialization
    }
}

/**
 * Fetches a random fact from the Useless Facts API.
 *
 * @return A Fact object
 * @throws Exception if the API request fails or returns an error.
 */
suspend fun fetchRandomFact(): Fact {
    return withContext(Dispatchers.IO) {
        try {
            // Make a request to the Useless Facts API
            val response: HttpResponse = client.get("https://uselessfacts.jsph.pl/random.json?language=en")

            // Check if the response is successful (2xx)
            if (!response.status.isSuccess()) {
                throw Exception("Failed to fetch fact: ${response.status.value} - ${response.status.description}")
            }
            // Parse the response body using Gson
            val responseBody: UselessFactResponseBody = Gson().fromJson(response.bodyAsText(), UselessFactResponseBody::class.java)

            // Extract the original fact and permalink
            val originalFact = responseBody.text ?:  throw Exception("Unexpected response format: missing 'text' field")
            val permalink = responseBody.permalink ?:  throw Exception("Unexpected response format: missing 'permalink' field")

            // Simplified URL shortening logic
            val shortenedUrl = "http://short.url/${System.currentTimeMillis()}"

            Fact(originalFact, shortenedUrl, permalink)
        } catch (e: Exception) {
            // Log the exception
            println("Error fetching fact: ${e.message}")
            throw Exception("Error fetching fact from API: ${e.message}")
        } finally {
            client.close() // Ensure client is closed after use
        }
    }
}
