package com.taz.tazmovies.movies

import com.taz.tazmovies.responses.MovieDetailResult

interface MovieDetailContract {
  interface ViewContract {
    fun onLoadingMovieDetail()
    fun onDismissLoadingMovieDetail()
    fun onSuccessGetMovieDetail(movieDetailResult: MovieDetailResult)
    fun onFailedGetMovieDetail(message: String)
  }

  interface PresenterContract {
    fun getMovieDetail(movie_id: Long)
  }
}