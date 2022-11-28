package com.taz.tazmovies.genres

import android.app.Activity
import android.content.Context
import android.media.tv.TvContract.Programs.Genres
import android.util.Log
import com.google.gson.Gson
import com.taz.tazmovies.BaseApp
import com.taz.tazmovies.Constants.Companion.PREFERENCE
import com.taz.tazmovies.responses.GenreQuery
import com.taz.tazmovies.responses.GenreResult
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback

class GenresPresenter(
  private val activity: Activity
) : GenresContract.PresenterContract {

  companion object {
    val TAG = this::class.java.simpleName

    fun saveGenres(context: Context, genres: List<GenreResult>) {
      val prefs = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
      prefs.edit().putString(TAG, Gson().toJson(genres)).apply()
    }

    fun getGenres(context: Context): List<GenreResult> {
      val prefs = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
      val jsonArray = JSONArray(prefs.getString(TAG, "[]"))
      val items = mutableListOf<GenreResult>()
      for (i in 0 until jsonArray.length()) {
        val jsonObject = jsonArray.getJSONObject(i)
        items.add(GenreResult(jsonObject))
      }
      return items
    }
  }

  var viewContract: GenresContract.ViewContract? = null

  fun setViewContract(viewContract: GenresContract.ViewContract) : GenresPresenter {
    this.viewContract = viewContract
    return this
  }

  override fun getGenres() {
    Log.i(TAG, "getGenres...")
    viewContract?.onLoading()
    var items = Companion.getGenres(activity)
    if(items.isNotEmpty()){
      viewContract?.onDismissLoading()
      Log.d(TAG, "response isNotEmpty: ${Gson().toJson(items)}")
      viewContract?.onSuccessGetGenres(items)
    } else {
      BaseApp.instance.retrofit.getGenreList()
        .enqueue(object : Callback<GenreQuery> {
          override fun onFailure(call: Call<GenreQuery>, t: Throwable) {
            viewContract?.onDismissLoading()
            Log.d(TAG, "onFailure: ${t.message.toString()}")
            viewContract?.onFailedGetGenres(t.message.toString())
          }

          override fun onResponse(
            call: Call<GenreQuery>,
            response: retrofit2.Response<GenreQuery>
          ) {
            viewContract?.onDismissLoading()
            Log.d(TAG, "response: ${Gson().toJson(response.body())}")
            items = response.body()?.genres ?: listOf()
            viewContract?.onSuccessGetGenres(items)
            saveGenres(activity, items)
          }
        })
    }
  }
}