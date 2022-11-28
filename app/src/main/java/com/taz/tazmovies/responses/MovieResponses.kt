package com.taz.tazmovies.responses

import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.io.Serializable

data class GenreQuery(@SerializedName("genres") var genres: List<GenreResult>)

data class MovieQuery(
  @SerializedName("page") var page: Int,
  @SerializedName("total_results") var total_results: Int,
  @SerializedName("total_pages") var total_pages: Int,
  @SerializedName("results") var movies: List<MovieResult>
)

data class MovieVideoQuery(@SerializedName("results") var movieVideos: List<MovieVideoResult>)

data class ReviewQuery(
  @SerializedName("results") var reviews: List<ReviewResult>,
  @SerializedName("page") var page: Int,
  @SerializedName("total_results") var total_results: Int,
  @SerializedName("total_pages") var total_pages: Int
)

data class MovieResult(
  @SerializedName("id") val id: Long,
  @SerializedName("original_title") val original_title: String,
  @SerializedName("poster_path") val poster_path: String?,
  @SerializedName("backdrop_path") val backdrop_path: String?,
  @SerializedName("popularity") val popularity: Double,
  @SerializedName("title") val title: String,
  @SerializedName("vote_average") val vote_average: Double
) : Serializable

data class GenreResult(
  @SerializedName("id") val id: Long,
  @SerializedName("name") val name: String
) : Serializable {
  constructor(jsonObject: JSONObject?) : this(
    id = jsonObject?.optLong("id", 0) ?: 0,
    name = jsonObject?.optString("name", "") ?: "",
  )
}

data class MovieVideoResult(
  @SerializedName("id") val id: String,
  @SerializedName("name") var name: String,
  @SerializedName("key") var key: String,
  @SerializedName("site") var site: String,
  @SerializedName("size") var size: Long,
  @SerializedName("published_at") var published_at: String,
) : Serializable

data class Actor(
  @SerializedName("id") val id: Long,
  @SerializedName("name") val name: String,
  @SerializedName("profile_path") val profile_path: String?
) : Serializable

data class Credits(
  @SerializedName("cast") var cast: List<Actor>? = listOf()
) : Serializable

data class MovieDetailResult(
  @SerializedName("id")
  val id: Long? = 0,
  @SerializedName("title")
  val title: String? = "",
  @SerializedName("poster_path")
  val posterPath: String? = "",
  @SerializedName("backdrop_path")
  val backdropPath: String? = "",
  @SerializedName("overview")
  val overview: String? = "",
  @SerializedName("genres")
  val genres: List<GenreResult>? = listOf(),
  @SerializedName("credits")
  val credits: Credits? = null,
  @SerializedName("popularity")
  val popularity: Double? = 0.0,
  @SerializedName("release_date")
  val releaseDate: String? = "",
  @SerializedName("vote_average")
  val voteAverage: Double? = 0.0,
  @SerializedName("vote_count")
  val voteCount: Int? = 0,
  @SerializedName("runtime")
  val runtime: Int? = 0,
  @SerializedName("tagline")
  val tagline: String? = ""
) : Serializable

data class ReviewDetail(
  @SerializedName("name") val name: String,
  @SerializedName("username") val username: String,
  @SerializedName("avatar_path") val avatar_path: String?,
  @SerializedName("rating") val rating: Int?
) : Serializable

data class ReviewResult(
  @SerializedName("id") val id: String,
  @SerializedName("author") val author: String,
  @SerializedName("author_detail") val reviewDetail: ReviewDetail,
  @SerializedName("content") val content: String,
  @SerializedName("created_at") val created_at: String,
  @SerializedName("updated_at") val updated_at: String
) : Serializable