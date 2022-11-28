package com.taz.tazmovies.retrofit

import com.taz.tazmovies.responses.GenreQuery
import com.taz.tazmovies.responses.MovieDetailResult
import com.taz.tazmovies.responses.MovieQuery
import com.taz.tazmovies.responses.MovieVideoQuery
import com.taz.tazmovies.responses.MovieVideoResult
import com.taz.tazmovies.responses.ReviewQuery
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("genre/movie/list") fun getGenreList(): Call<GenreQuery>

    @GET("discover/movie?")
    fun getMovieList(@Query("with_genres") with_genres: Long):  Call<MovieQuery>

    @GET("discover/movie?")
    fun getMovieList(
        @Query("with_genres") with_genres: Long,
        @Query("page") page: Int,
    ):  Call<MovieQuery>

    @GET("movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id") movie_id: Long): Call<MovieDetailResult>

    @GET("movie/{movie_id}/videos")
    fun getMovieVideo(@Path("movie_id") movie_id: Long): Call<MovieVideoQuery>

    @GET("movie/{movie_id}/reviews")
    fun getMoviewReviews(
        @Path("movie_id") movie_id: Long,
        @Query("page") page: Int
    ): Call<ReviewQuery>
}