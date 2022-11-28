package com.taz.tazmovies.movies

import android.app.Activity
import android.util.Log
import com.google.gson.Gson
import com.taz.tazmovies.BaseApp
import com.taz.tazmovies.Constants.Companion.BASE_URL_API
import com.taz.tazmovies.responses.MovieQuery
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class MoviesPresenter(
  private val activity: Activity
) : MoviesContract.PresenterContract {

  companion object {
    val TAG = this::class.java.simpleName
  }

  var viewContract: MoviesContract.ViewContract? = null

  fun setViewContract(viewContract: MoviesContract.ViewContract): MoviesPresenter {
    this.viewContract = viewContract
    return this
  }

  override fun getMovies(with_genres: Long, page: Int) {
    viewContract?.onLoading()
    BaseApp.instance.retrofit.getMovieList(with_genres, page)
      .enqueue(object : Callback<MovieQuery> {
        override fun onFailure(call: Call<MovieQuery>, t: Throwable) {
          viewContract?.onDismissLoading()
          Log.e(TAG, "response onFailure: ${t.message.toString()}")
          viewContract?.onFailedGetMovies(t.message.toString())
        }

        override fun onResponse(
          call: Call<MovieQuery>,
          response: retrofit2.Response<MovieQuery>
        ) {
          viewContract?.onDismissLoading()
          val items = response.body()?.movies ?: listOf()
          val totalPage = response.body()?.total_pages ?: 0
          val currentPage = response.body()?.page ?: 0
          val totalResult = response.body()?.total_results ?: 0
          Log.d(TAG, "response: \n" +
            "totalPage: $totalPage\n" +
            "currentPage: $currentPage\n" +
            "totalResult: $totalResult\n" +
            "items: ${Gson().toJson(items)}"
          )
          viewContract?.onSuccessGetMovies(
            items = items,
            totalPage = totalPage,
            currentPage = currentPage,
            totalResult = totalResult
          )
        }
      })
  }

  // override fun getMovies(with_genres: Long) {
  //   viewContract?.onLoading()
  //   BaseApp.instance.retrofit.getMovieList(with_genres)
  //     .enqueue(object : Callback<MovieQuery> {
  //     override fun onFailure(call: Call<MovieQuery>, t: Throwable) {
  //       viewContract?.onDismissLoading()
  //       Log.d(TAG, "onFailure: ${t.message.toString()}")
  //       viewContract?.onFailedGetMovies(t.message.toString())
  //     }
  //
  //     override fun onResponse(
  //       call: Call<MovieQuery>,
  //       response: retrofit2.Response<MovieQuery>
  //     ) {
  //       viewContract?.onDismissLoading()
  //       Log.d(TAG, "response: ${Gson().toJson(response.body())}")
  //       viewContract?.onSuccessGetMovies(response.body()?.movies ?: listOf())
  //     }
  //   })
  // }
}