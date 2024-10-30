package com.db.data

import com.google.gson.annotations.SerializedName

/**
 * GET Response format for returning fact information.
 *
 * @property fact The original fact text.
 * @property originalPermalink The original permalink for the fact.
 */
data class FactGetResponse(@SerializedName("fact") val fact: String, @SerializedName("original_permalink") val originalPermalink: String)