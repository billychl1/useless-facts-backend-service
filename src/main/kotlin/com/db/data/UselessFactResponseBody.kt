package com.db.data

/**
 * Data class representing the response from the Useless Facts API.
 *
 * @property text The fact retrieved from the API. This may be null if the API response does not contain a valid fact.
 * @property permalink A URL that links to the original fact. This may be null if the API response does not provide a permalink.
 */
data class UselessFactResponseBody(
    val text: String?,
    val permalink: String?
)