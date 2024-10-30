package com.db

import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import com.db.data.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class RoutingTest {

    @Test
    fun `Verify facts routing`() {
        testApplication {
            application { module() } // Setup the application

            // POST /facts should return a new fact and shortened URL
            val response = client.post("/facts")
            assertEquals(HttpStatusCode.OK, response.status)

            val responseBody: String = response.bodyAsText()
            val factPostResponse: FactPostResponse = Gson().fromJson(responseBody, FactPostResponse::class.java)
            assertNotNull(factPostResponse.shortenedUrl)
            assertFalse(factPostResponse.originalFact.isBlank())

            // GET /facts/{shortenedUrl} should return the correct fact
            val getResponse = client.get("/facts/" + URLEncoder.encode(factPostResponse.shortenedUrl, StandardCharsets.UTF_8.toString()))
            assertEquals(HttpStatusCode.OK, getResponse.status)

            val getResponseBody: String = getResponse.bodyAsText()
            val factGetResponse: FactGetResponse = Gson().fromJson(getResponseBody, FactGetResponse::class.java)
            assertEquals(factPostResponse.originalFact, factGetResponse.fact)
            assertFalse(factGetResponse.originalPermalink.isBlank())

            // GET /facts should return all facts
            val getAllResponse = client.get( "/facts")
            assertEquals(HttpStatusCode.OK, response.status)

            val getAllResponseBody: String = getAllResponse.bodyAsText()
            val factList = object : TypeToken<List<Fact>>() {}.type
            val facts: List<Fact> = Gson().fromJson(getAllResponseBody, factList)

            assertEquals(factPostResponse.originalFact, facts.first().originalFact)
            assertEquals(factPostResponse.shortenedUrl, facts.first().shortenedUrl)
            assertEquals(factGetResponse.originalPermalink, facts.first().originalPermalink)
        }
    }

    @Test
    fun `GET !facts!{shortenedUrl}!redirect should redirect correctly`() { // GET /facts/{shortenedUrl}/redirect
        // Add a fact to the cache
        val shortenedUrl = "http://short.url/200"
        val fact = Fact("Redirect test fact.", shortenedUrl, "http://www.test.com")
        FactCache.addFact(fact)

        // Test the redirect
        testApplication {
            application { module() } // Start the application

            try {
                val redirectResponse = client.get(
                    "/facts/" + URLEncoder.encode(
                        shortenedUrl,
                        StandardCharsets.UTF_8.toString()
                    ) + "/redirect"
                )
            } catch (e: IllegalArgumentException) {
                // In testing environment, routing respondRedirect will throw IllegalArgumentException
                assertTrue(e.message?.contains("http://www.test.com") == true)
            }
            assertEquals(1, FactCache.getStatistics().first { it.shortenedUrl == shortenedUrl }.accessCount)
        }
    }

    @Test
    fun `GET !admin!statistics should return access statistics`() { // GET /admin/statistics
        val shortenedUrl = "http://short.url/100"
        val fact = Fact("Stat test fact.", shortenedUrl, "https://uselessfacts.jsph.pl/api/b2/facts/20703986d294054ff282e1ee212e3242")
        FactCache.addFact(fact)
        FactCache.incrementAccessCount(shortenedUrl)

        testApplication {
            application { module() } // Setup the application

            val response = client.get("/admin/statistics")
            assertEquals(HttpStatusCode.OK, response.status)

            val responseBody: String = response.bodyAsText()
            val statsResponseList = object : TypeToken<List<StatsResponse>>() {}.type
            val stats: List<StatsResponse> = Gson().fromJson(responseBody, statsResponseList)
            assertEquals(1, stats.first { it.shortenedUrl == shortenedUrl }.accessCount)
        }
    }

}
