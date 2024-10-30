package com.db

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import com.db.data.*

class FactCacheTest {

    @BeforeEach
    fun setup() {
        // Clear the cache before each test to ensure isolation
        FactCache.clear()
    }

    @Test
    fun `should add a fact to the cache`() {
        val fact = Fact("This is a test fact.", "http://short.url/123", "https://uselessfacts.jsph.pl/api/v2/facts/20703986d294054ff282e1ee212e3242")
        FactCache.addFact(fact)

        assertNotNull(FactCache.getFact("http://short.url/123"))
    }

    @Test
    fun `should return null for a non-existing fact`() {
        assertNull(FactCache.getFact("http://short.url/non-existing"))
    }

    @Test
    fun `should increment access count correctly`() {
        val fact = Fact("Another test fact.", "http://short.url/456", "https://uselessfacts.jsph.pl/api/v3/facts/20703986d294054ff282e1ee212e3242")
        FactCache.addFact(fact)

        FactCache.incrementAccessCount("http://short.url/456")
        assertEquals(1, FactCache.getStatistics().first { it.shortenedUrl == "http://short.url/456" }.accessCount)

        FactCache.incrementAccessCount("http://short.url/456")
        assertEquals(2, FactCache.getStatistics().first { it.shortenedUrl == "http://short.url/456" }.accessCount)
    }

    @Test
    fun `should return all cached facts`() {
        val fact1 = Fact("Fact 1", "http://short.url/1", "https://uselessfacts.jsph.pl/api/t2/facts/20703986d294054ff282e1ee212e3242")
        val fact2 = Fact("Fact 2", "http://short.url/2", "https://uselessfacts.jsph.pl/api/t3/facts/20703986d294054ff282e1ee212e3242")
        FactCache.addFact(fact1)
        FactCache.addFact(fact2)

        val allFacts = FactCache.getAllFacts()
        assertEquals(2, allFacts.size)
        assertTrue(allFacts.any { it.shortenedUrl == "http://short.url/1" })
        assertTrue(allFacts.any { it.shortenedUrl == "http://short.url/2" })
        assertTrue(allFacts.any { it.originalPermalink == "https://uselessfacts.jsph.pl/api/t2/facts/20703986d294054ff282e1ee212e3242" })
        assertTrue(allFacts.any { it.originalPermalink == "https://uselessfacts.jsph.pl/api/t3/facts/20703986d294054ff282e1ee212e3242" })
    }

    @Test
    fun `should not crash when incrementing access count for non-existing fact`() {
        assertDoesNotThrow {
            FactCache.incrementAccessCount("http://short.url/non-existing")
        }
    }
}
