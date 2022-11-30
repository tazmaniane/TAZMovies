package com.taz.tazmovies.reviews.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mitrasetia.anda1.Base.view.act.BaseActivity
import com.taz.tazmovies.R
import com.taz.tazmovies.adapter.ReviewsAdapter
import com.taz.tazmovies.databinding.ReviewsActivityBinding
import com.taz.tazmovies.responses.MovieDetailResult
import com.taz.tazmovies.responses.ReviewResult
import com.taz.tazmovies.reviews.ReviewsContract
import com.taz.tazmovies.reviews.ReviewsPresenter
import com.taz.tazmovies.utils.PrimaryToolbar

class ReviewsActivity : BaseActivity(),
  ReviewsContract.ViewContract {

  companion object {
    val TAG = this::class.java.simpleName
    val MOVIE_DETAIL = "movieDetail"

    fun getIntent(context: Context, movieDetailResult: MovieDetailResult?): Intent {
      return Intent(context, ReviewsActivity::class.java)
        .putExtra(MOVIE_DETAIL, movieDetailResult)
    }

    fun getMovieDetailExtras(activity: Activity?): MovieDetailResult? {
      return activity?.intent?.extras?.getSerializable(MOVIE_DETAIL) as MovieDetailResult?
    }
  }

  lateinit var _binding: ReviewsActivityBinding

  var mReviewsPresenter: ReviewsPresenter? = null
  var mReviewsAdapter: ReviewsAdapter? = null
  var mMovieDetailResult: MovieDetailResult? = null

  var visibleItemCount = 0
  var totalItemCount = 0
  var pastItemsVisible = 0
  var totalPage = 0
  var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    _binding = ReviewsActivityBinding.inflate(layoutInflater)
    setContentView(_binding.root)
    setFullScreen()

      mMovieDetailResult = getMovieDetailExtras(this)
    Log.d(TAG, "mMovieDetailResult: ${Gson().toJson(mMovieDetailResult)}")
    initToolbar()
    initAdapter()
    getReviews(mMovieDetailResult)
  }

  private fun initToolbar() {
    setSupportActionBar(_binding.appbarViewIcl.toolbarView)
    displayShowHomeEnabled(true)
    PrimaryToolbar(this)
      .setToolbar(_binding.appbarViewIcl.toolbarView)
      .setTitle("${getString(R.string.app_name)} Reviews")
      .setSubtitle("${mMovieDetailResult?.title ?: "-"}")
      .setHideActionButton(true)
      .init()
  }

  private fun initAdapter() {
    mReviewsAdapter = ReviewsAdapter(this)
    _binding.recyclerView.layoutManager = GridLayoutManager(this, 1)
    _binding.recyclerView.adapter = mReviewsAdapter
    _binding.recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if(dy > 0){
          // check scroll down
          visibleItemCount = _binding.recyclerView.layoutManager?.childCount ?: 0
          totalItemCount = _binding.recyclerView.layoutManager?.itemCount ?: 0
          pastItemsVisible = (_binding.recyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
          // if(_binding.progressbar.visibility == VISIBLE) {
            if((visibleItemCount + pastItemsVisible) >= totalItemCount) {
              if(currentPage < totalPage){
                getReviews(mMovieDetailResult)
              }
            }
          // }
        }
      }
    })
  }

  private fun getReviews(movieDetailResult: MovieDetailResult?) {
    if (mReviewsPresenter == null) {
      mReviewsPresenter = ReviewsPresenter(this)
        .setViewContract(this)
    }
    // moviesPresenter?.getMovies(genreResult?.id ?: 0)
    if(totalPage == 0) {
      currentPage = 1
      mReviewsPresenter?.getReviews(movie_id = movieDetailResult?.id ?: 0, page = currentPage)
    } else {
      if(currentPage < totalPage) {
        currentPage++
        mReviewsPresenter?.getReviews(movie_id = movieDetailResult?.id ?: 0, page = currentPage)
      }
    }
    Log.d(TAG, "totalPage: $totalPage | currentPage: $currentPage")
  }

  override fun onLoadingReview() {
    if(currentPage <= 1){
      showLoading()
      _binding.progressbar.visibility = GONE
    } else {
      _binding.progressbar.visibility = VISIBLE
      dialogProgress?.dismiss()
    }
  }

  override fun onDismissLoadingReview() {
    dismissLoading()
    _binding.progressbar.visibility = GONE
  }

  override fun onSuccessGetReviews(items: List<ReviewResult>, totalPage: Int, currentPage: Int, totalResult: Int) {
    Log.d(TAG, "onSuccessGetMovies")
    this.currentPage = currentPage
    this.totalPage = totalPage
    if(this.currentPage <= 1){
      mReviewsAdapter?.setItems(items)
    } else {
      mReviewsAdapter?.addItems(items)
    }
  }

  override fun onFailedGetReviews(message: String) {
    Log.e(TAG, "onFailedGetReviews: $message")
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
  }
}