package com.taz.tazmovies.reviews

import android.app.Activity
import android.util.Log
import com.google.gson.Gson
import com.taz.tazmovies.BaseApp
import com.taz.tazmovies.Constants.Companion.BASE_URL_API
import com.taz.tazmovies.responses.MovieQuery
import com.taz.tazmovies.responses.ReviewQuery
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class ReviewsPresenter(
  private val activity: Activity
) : ReviewsContract.PresenterContract {

  companion object {
    val TAG = this::class.java.simpleName
  }

  var viewContract: ReviewsContract.ViewContract? = null

  fun setViewContract(viewContract: ReviewsContract.ViewContract): ReviewsPresenter {
    this.viewContract = viewContract
    return this
  }

  override fun getReviews(movie_id: Long, page: Int) {
    viewContract?.onLoadingReview()
    BaseApp.instance.retrofit.getMoviewReviews(movie_id, page)
      .enqueue(object : Callback<ReviewQuery> {
        override fun onFailure(call: Call<ReviewQuery>, t: Throwable) {
          viewContract?.onDismissLoadingReview()
          Log.e(TAG, "getReviews response onFailure: ${t.message.toString()}")
          viewContract?.onFailedGetReviews(t.message.toString())
        }

        override fun onResponse(
          call: Call<ReviewQuery>,
          response: retrofit2.Response<ReviewQuery>
        ) {
          viewContract?.onDismissLoadingReview()
          val items = response.body()?.reviews ?: listOf()
          val totalPage = response.body()?.total_pages ?: 0
          val currentPage = response.body()?.page ?: 0
          val totalResult = response.body()?.total_results ?: 0
          Log.d(TAG, "getReviews response: \n" +
            "totalPage: $totalPage\n" +
            "currentPage: $currentPage\n" +
            "totalResult: $totalResult\n" +
            "items: ${Gson().toJson(items)}"
          )
          viewContract?.onSuccessGetReviews(
            items = items,
            totalPage = totalPage,
            currentPage = currentPage,
            totalResult = totalResult
          )
        }
      })
  }
}