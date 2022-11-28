package com.taz.tazmovies.movies

import android.app.Activity
import android.util.Log
import com.google.gson.Gson
import com.taz.tazmovies.BaseApp
import com.taz.tazmovies.Constants.Companion.BASE_URL_API
import com.taz.tazmovies.responses.MovieDetailResult
import com.taz.tazmovies.responses.MovieQuery
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class MovieDetailPresenter(
  private val activity: Activity
) : MovieDetailContract.PresenterContract {

  companion object {
    val TAG = this::class.java.simpleName
  }

  var viewContract: MovieDetailContract.ViewContract? = null

  fun setViewContract(viewContract: MovieDetailContract.ViewContract) : MovieDetailPresenter{
    this.viewContract = viewContract
    return this
  }

  override fun getMovieDetail(movie_id: Long) {
    // activity
    viewContract?.onLoadingMovieDetail()
    BaseApp.instance.retrofit.getMovieDetail(movie_id)
      .enqueue(object : Callback<MovieDetailResult> {
      override fun onFailure(call: Call<MovieDetailResult>, t: Throwable) {
        viewContract?.onDismissLoadingMovieDetail()
        Log.d(TAG, "getMovieDetail response onFailure: ${t.message.toString()}")
        viewContract?.onFailedGetMovieDetail(t.message.toString())
      }

      override fun onResponse(
        call: Call<MovieDetailResult>,
        response: retrofit2.Response<MovieDetailResult>
      ) {
        viewContract?.onDismissLoadingMovieDetail()
        Log.d(TAG, "getMovieDetail response: ${Gson().toJson(response.body())}")
        viewContract?.onSuccessGetMovieDetail(response.body() as MovieDetailResult)
      }
    })
  }
}