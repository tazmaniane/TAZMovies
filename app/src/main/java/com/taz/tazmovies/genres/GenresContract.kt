package com.taz.tazmovies.genres

import com.taz.tazmovies.responses.GenreResult

interface GenresContract {
  interface ViewContract {
    fun onLoading()
    fun onDismissLoading()
    fun onSuccessGetGenres(items: List<GenreResult>)
    fun onFailedGetGenres(message: String)
  }

  interface PresenterContract {
    fun getGenres()
  }
}