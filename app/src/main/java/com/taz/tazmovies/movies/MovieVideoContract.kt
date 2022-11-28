package com.taz.tazmovies.movies

import com.taz.tazmovies.responses.MovieVideoResult

interface MovieVideoContract {
  interface ViewContract {
    fun onLoading()
    fun onDismissLoading()
    fun onSuccessGetMovieVideo(items: List<MovieVideoResult>)
    fun onFailedGetMovieVideo(message: String)
  }

  interface PresenterContract {
    fun getMovieVideo(movie_id: Long)
  }
}