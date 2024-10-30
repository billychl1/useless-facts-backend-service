package com.db.data

import com.google.gson.annotations.SerializedName

/**
 * POST Response format for returning fact information.
 *
 * @property original_fact The original text of the fact.
 * @property shortened_url The shortened URL associated with the fact.
 */
data class FactPostResponse(@SerializedName("original_fact") val originalFact: String, @SerializedName("shortened_url") val shortenedUrl: String)