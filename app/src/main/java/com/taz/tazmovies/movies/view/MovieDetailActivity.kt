package com.taz.tazmovies.movies.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity.CENTER
import android.view.View.GONE
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.mitrasetia.anda1.Base.view.act.BaseActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.taz.tazmovies.R
import com.taz.tazmovies.adapter.ReviewsAdapter
import com.taz.tazmovies.databinding.MovieDetailActivityBinding
import com.taz.tazmovies.movies.MovieVideoContract
import com.taz.tazmovies.movies.MovieVideoPresenter
import com.taz.tazmovies.responses.Actor
import com.taz.tazmovies.responses.GenreResult
import com.taz.tazmovies.responses.MovieDetailResult
import com.taz.tazmovies.responses.MovieVideoResult
import com.taz.tazmovies.responses.ReviewResult
import com.taz.tazmovies.reviews.ReviewsContract
import com.taz.tazmovies.reviews.ReviewsPresenter
import com.taz.tazmovies.reviews.view.ReviewsActivity
import com.taz.tazmovies.utils.NumberUtils
import com.taz.tazmovies.utils.TextUtils

class MovieDetailActivity : BaseActivity(),
MovieVideoContract.ViewContract,
ReviewsContract.ViewContract {

  companion object {
    val TAG = this::class.java.simpleName
    val MOVIE_DETAIL = "movieDetail"

    fun getIntent(context: Context, movieDetailResult: MovieDetailResult): Intent {
      return Intent(context, MovieDetailActivity::class.java)
        .putExtra(MOVIE_DETAIL, movieDetailResult)
    }

    fun getMovieDetailExtras(activity: Activity?): MovieDetailResult? {
      return activity?.intent?.extras?.getSerializable(MOVIE_DETAIL) as MovieDetailResult?
    }
  }

  lateinit var _binding: MovieDetailActivityBinding

  var mReviewsPresenter: ReviewsPresenter? = null
  var movieVideoPresenter: MovieVideoPresenter? = null

  var mReviewsAdapter: ReviewsAdapter? = null
  var movieDetailResult: MovieDetailResult? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    _binding = MovieDetailActivityBinding.inflate(layoutInflater)
    setContentView(_binding.root)
    setFullScreen()

    initReviewAdapter()

    movieDetailResult = getMovieDetailExtras(this)
    Log.d(MoviesActivity.TAG, "movieDetailResult: ${Gson().toJson(movieDetailResult)}")
    movieDetailResult?.let { movieDetail ->
      _binding.tvMovieTitle.text = movieDetail.title
      _binding.tvMovieDescription.text = if(movieDetail.overview.isNullOrEmpty()){
        "Tidak ada deskripsi"
      } else {
        movieDetail.overview
      }
      _binding.tvVoteAverage.text = movieDetail.voteAverage.toString()
      val voteCount = movieDetail.popularity.toString().replace(".", "").toDouble()
      _binding.tvVoteCount.text = NumberUtils.getCurrencyFrom(voteCount)
      addGenreChips(movieDetail.genres ?: listOf())
      addActors(movieDetail.credits?.cast ?: listOf())

      _binding.btnNextReviews.visibility = GONE
      _binding.progressbarReview.visibility = GONE

      getReviews(movieDetailResult)
      getMovieVideo(movieDetailResult)
    }
  }

  private fun initVideoPlayer(videoId: String){
    // https://api.themoviedb.org/3/movie/297762/videos?api_key=###
    lifecycle.addObserver(_binding.youtubePlayerView)
    _binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
      override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
        youTubePlayer.cueVideo(videoId, 0f)
        // youTubePlayer.loadVideo(videoId, 0f)
        // youTubePlayer.pause()
      }
    })
    // _binding.youtubePlayerView.V
  }

  private fun initReviewAdapter(){
    mReviewsAdapter = ReviewsAdapter(this)
      .setListener(object: ReviewsAdapter.Companion.ReviewsAdapterListener {
        override fun onClickItem(reviewResult: ReviewResult) {

        }
      })
    _binding.recyclerViewReviews.layoutManager = LinearLayoutManager(this)
    _binding.recyclerViewReviews.adapter = mReviewsAdapter
    _binding.btnNextReviews.setOnClickListener {
      startActivity(ReviewsActivity.getIntent(this, movieDetailResult))
    }
    /*
    // this is for endless scroll
    _binding.recyclerViewReviews.addOnScrollListener(object: RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if(dy > 0){
          // check scroll down
          visibleItemCount = _binding.recyclerViewReviews.layoutManager?.childCount ?: 0
          totalItemCount = _binding.recyclerViewReviews.layoutManager?.itemCount ?: 0
          pastItemsVisible = (_binding.recyclerViewReviews.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
          // if(_binding.progressbar.visibility == VISIBLE) {
          if((visibleItemCount + pastItemsVisible) >= totalItemCount) {
            if(currentPageReview < totalPageReview){
              getReviews(movieDetailResult)
            }
          }
          // }
        }
      }
    })
     */
  }

  private fun getMovieVideo(movieDetailResult: MovieDetailResult?){
    if(movieVideoPresenter == null){
      movieVideoPresenter = MovieVideoPresenter(this)
        .setViewContract(this)
    }
    movieDetailResult?.let { movieDetail ->
      movieVideoPresenter?.getMovieVideo(movieDetail.id ?: 0)
    }
  }

  private fun getReviews(movieDetailResult: MovieDetailResult?){
    if (mReviewsPresenter == null) {
      mReviewsPresenter = ReviewsPresenter(this)
        .setViewContract(this)
    }
    mReviewsPresenter?.getReviews(movie_id = movieDetailResult?.id ?: 0, page = 1)
  }

  private fun addActors(actors: List<Actor>) {

  }

  private fun addGenreChips(genres: List<GenreResult>) {
    for(genre in genres){
      val chip = Chip(this)
      chip.id = ViewCompat.generateViewId()
      chip.setPadding(10, 5, 10, 5)
      chip.text = genre.name
      chip.textAlignment = TEXT_ALIGNMENT_CENTER
      chip.isCloseIconVisible = false
      chip.isCheckedIconVisible = false
      chip.isCheckable = false
      chip.setChipBackgroundColorResource(R.color.colorSemiTransparentBlack)
      chip.setTextColor(ContextCompat.getColor(this, android.R.color.white))
      chip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12f)
      chip.typeface = TextUtils.getTypeFaceFontPrimary(this)
      // chip.setTextAppearance(R.style.ChipTextAppearance)
      _binding.chipGroup.addView(chip)
    }
  }

  override fun onLoading() {
    showLoading()
  }

  override fun onDismissLoading() {
    dismissLoading()
  }

  override fun onLoadingReview() {
    _binding.progressbarReview.visibility = VISIBLE
    _binding.tvNoReview.visibility = GONE
    _binding.btnNextReviews.visibility = GONE
  }

  override fun onDismissLoadingReview() {
    _binding.progressbarReview.visibility = GONE
    _binding.tvNoReview.visibility = GONE
    _binding.btnNextReviews.visibility = VISIBLE
  }

  override fun onSuccessGetReviews(
    items: List<ReviewResult>,
    totalPage: Int,
    currentPage: Int,
    totalResult: Int
  ) {
    Log.d(MoviesActivity.TAG, "onSuccessGetMovies")
    mReviewsAdapter?.setItems(items)
    _binding.tvReviewTitle.text = "Review (${mReviewsAdapter?.itemCount})"
    if(items.isNotEmpty()){
      _binding.tvNoReview.visibility = GONE
      // _binding.btnNextReviews.visibility = VISIBLE
    } else {
      _binding.tvNoReview.visibility = VISIBLE
      // _binding.btnNextReviews.visibility = GONE
    }
  }

  override fun onFailedGetReviews(message: String) {
    Log.e(MoviesActivity.TAG, "onFailedGetReviews: $message")
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
  }

  override fun onSuccessGetMovieVideo(items: List<MovieVideoResult>) {
    if(items.isNotEmpty()){
      initVideoPlayer(items[0].key)
    }
  }

  override fun onFailedGetMovieVideo(message: String) {
    Log.e(MoviesActivity.TAG, "onFailedGetMovieDetail: $message")
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding.youtubePlayerView.release()
  }
}