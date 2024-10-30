package com.db.data

/**
 * Represents a fact with its original text, a shortened URL, an original permalink.
 *
 * @property original_fact The original text of the fact.
 * @property shortened_url The shortened URL associated with the fact.
 * @property originalPermalink The original permalink for the fact.
 */
data class Fact(val originalFact: String, val shortenedUrl: String, val originalPermalink: String)