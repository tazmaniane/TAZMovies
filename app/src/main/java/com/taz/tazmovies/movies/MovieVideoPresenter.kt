package com.taz.tazmovies.movies

import android.app.Activity
import android.util.Log
import com.google.gson.Gson
import com.taz.tazmovies.BaseApp
import com.taz.tazmovies.Constants.Companion.BASE_URL_API
import com.taz.tazmovies.responses.MovieQuery
import com.taz.tazmovies.responses.MovieVideoQuery
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class MovieVideoPresenter(
  private val activity: Activity
) : MovieVideoContract.PresenterContract {

  companion object {
    val TAG = this::class.java.simpleName
  }

  var viewContract: MovieVideoContract.ViewContract? = null

  fun setViewContract(viewContract: MovieVideoContract.ViewContract) : MovieVideoPresenter{
    this.viewContract = viewContract
    return this
  }

  override fun getMovieVideo(movie_id: Long) {
    // activity
    viewContract?.onLoading()
    BaseApp.instance.retrofit.getMovieVideo(movie_id)
      .enqueue(object : Callback<MovieVideoQuery> {
      override fun onFailure(call: Call<MovieVideoQuery>, t: Throwable) {
        viewContract?.onDismissLoading()
        Log.d(TAG, "onFailure: ${t.message.toString()}")
        viewContract?.onFailedGetMovieVideo(t.message.toString())
      }

      override fun onResponse(
        call: Call<MovieVideoQuery>,
        response: retrofit2.Response<MovieVideoQuery>
      ) {
        viewContract?.onDismissLoading()
        Log.d(TAG, "response: ${Gson().toJson(response.body())}")
        viewContract?.onSuccessGetMovieVideo(response.body()?.movieVideos ?: listOf())
      }
    })
  }
}