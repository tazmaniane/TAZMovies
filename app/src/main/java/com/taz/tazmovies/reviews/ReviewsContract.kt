package com.taz.tazmovies.reviews

import com.taz.tazmovies.responses.ReviewResult

interface ReviewsContract {
  interface ViewContract {
    fun onLoadingReview()
    fun onDismissLoadingReview()
    fun onSuccessGetReviews(items: List<ReviewResult>, totalPage: Int, currentPage: Int, totalResult: Int)
    fun onFailedGetReviews(message: String)
  }

  interface PresenterContract {
    fun getReviews(movie_id: Long, page: Int)
  }
}