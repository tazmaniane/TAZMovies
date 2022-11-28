package com.taz.tazmovies.movies

import com.taz.tazmovies.responses.MovieResult

interface MoviesContract {
  interface ViewContract {
    fun onLoading()
    fun onDismissLoading()
    fun onSuccessGetMovies(items: List<MovieResult>, totalPage: Int, currentPage: Int, totalResult: Int)
    fun onFailedGetMovies(message: String)
  }

  interface PresenterContract {
    // fun getMovies(with_genres: Long)
    fun getMovies(with_genres: Long, page: Int)
  }
}