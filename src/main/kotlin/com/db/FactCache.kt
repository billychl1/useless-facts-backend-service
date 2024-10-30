package com.db

import java.util.concurrent.ConcurrentHashMap
import com.db.data.*

/**
 * Caches facts and their access statistics in memory.
 */
object FactCache {
    private val facts = ConcurrentHashMap<String, Pair<Fact, Int>>() // Stores facts and their access count

    /**
     * Adds a fact to the cache.
     *
     * @param fact The fact to be added.
     */
    fun addFact(fact: Fact) {
        facts[fact.shortenedUrl] = Pair(fact, 0)
    }

    /**
     * Retrieves a fact from the cache by its shortened URL.
     *
     * @param shortenedUrl The shortened URL for which to retrieve the fact.
     * @return The corresponding fact or null if not found.
     */
    fun getFact(shortenedUrl: String): Fact? {
        return facts[shortenedUrl]?.first
    }

    /**
     * Increments the access count for a given shortened URL.
     *
     * @param shortenedUrl The shortened URL for which to increment the access count.
     */
    fun incrementAccessCount(shortenedUrl: String) {
        facts[shortenedUrl]?.let {
            facts[shortenedUrl] = it.copy(second = it.second + 1)
        }
    }

    /**
     * Retrieves all facts currently stored in the cache.
     *
     * @return A list of all cached facts.
     */
    fun getAllFacts(): List<Fact> {
        return facts.values.map { it.first }
    }

    /**
     * Retrieves access statistics for all shortened URLs.
     *
     * @return A list of statistics for each shortened URL.
     */
    fun getStatistics(): List<StatsResponse> {
        return facts.map { StatsResponse(it.key, it.value.second) }
    }

    /**
     * Clears all cached facts and their statistics.
     */
    fun clear() {
        facts.clear()
    }

}
