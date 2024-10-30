package com.db.data

import com.google.gson.annotations.SerializedName

/**
 * Represents the access statistics for a shortened URL.
 *
 * @property shortenedUrl The shortened URL.
 * @property accessCount The number of times the shortened URL has been accessed.
 */
data class StatsResponse(@SerializedName("shortened_url") val shortenedUrl: String,@SerializedName("access_count")  val accessCount: Int)