package com.taz.tazmovies.movies.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mitrasetia.anda1.Base.view.act.BaseActivity
import com.mitrasetia.anda1.Kontrak.GenreSelectDialog
import com.taz.tazmovies.R
import com.taz.tazmovies.adapter.MoviesAdapter
import com.taz.tazmovies.databinding.MoviesActivityBinding
import com.taz.tazmovies.movies.MovieDetailContract
import com.taz.tazmovies.movies.MovieDetailPresenter
import com.taz.tazmovies.movies.MoviesContract
import com.taz.tazmovies.movies.MoviesPresenter
import com.taz.tazmovies.responses.GenreResult
import com.taz.tazmovies.responses.MovieDetailResult
import com.taz.tazmovies.responses.MovieResult
import com.taz.tazmovies.utils.DialogProgress
import com.taz.tazmovies.utils.PrimaryToolbar

class MoviesActivity : BaseActivity(),
  MoviesContract.ViewContract,
  MovieDetailContract.ViewContract,
  MoviesAdapter.Companion.MoviesAdapterListener {

  companion object {
    val TAG = this::class.java.simpleName
    val GENRE = "genre"

    fun getIntent(context: Context, genreResult: GenreResult?): Intent {
      return Intent(context, MoviesActivity::class.java)
        .putExtra(GENRE, genreResult)
    }

    fun getGenreExtras(activity: Activity?): GenreResult? {
      return activity?.intent?.extras?.getSerializable(GENRE) as GenreResult?
    }
  }

  lateinit var _binding: MoviesActivityBinding

  var dialogProgress: DialogProgress? = null
  var moviesPresenter: MoviesPresenter? = null
  var movieDetailPresenter: MovieDetailPresenter? = null
  var moviesAdapter: MoviesAdapter? = null
  var mGenreSelectDialog: GenreSelectDialog? = null

  var mGenreResult: GenreResult? = null
  var mPrimaryToolbar: PrimaryToolbar? = null

  var visibleItemCount = 0
  var totalItemCount = 0
  var pastItemsVisible = 0
  var totalPage = 0
  var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    _binding = MoviesActivityBinding.inflate(layoutInflater)
    setContentView(_binding.root)
    setFullScreen()

    mGenreResult = getGenreExtras(this)
    Log.d(TAG, "genreResult: ${Gson().toJson(mGenreResult)}")
    initToolbar()
    initAdapter()
    getMovies(mGenreResult)
  }

  private fun initToolbar() {
    setSupportActionBar(_binding.appbarViewIcl.toolbarView)
    displayShowHomeEnabled(true)
    mPrimaryToolbar = PrimaryToolbar(this)
      .setToolbar(_binding.appbarViewIcl.toolbarView)
      .setTitle(getString(R.string.app_name))
      .setSubtitle("Genre: ${mGenreResult?.name ?: "Not Set"}")
      .setHideActionButton(false)
      .setActionText("Genre")
      .setActionIcon(ContextCompat.getDrawable(this, R.drawable.ic_search))
      .setHideBackButton(true)
      .setBackgroundColor(android.R.color.transparent)
      .setListener(object : PrimaryToolbar.PrimaryToolbarListener {
        override fun onClickAction() {
          showGenreSelectDialog(this@MoviesActivity)
        }
      })
      .init()
  }

  private fun initAdapter() {
    moviesAdapter = MoviesAdapter(this)
      .setListener(this)
    _binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
    _binding.recyclerView.adapter = moviesAdapter
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
                getMovies(mGenreResult)
              }
            }
          // }
        }
      }
    })
  }

  private fun getMovies(genreResult: GenreResult?) {
    if (moviesPresenter == null) {
      moviesPresenter = MoviesPresenter(this)
        .setViewContract(this)
    }
    // moviesPresenter?.getMovies(genreResult?.id ?: 0)
    if(totalPage == 0) {
      currentPage = 1
      moviesPresenter?.getMovies(with_genres = genreResult?.id ?: 0, page = currentPage)
    } else {
      if(currentPage < totalPage) {
        currentPage++
        moviesPresenter?.getMovies(with_genres = genreResult?.id ?: 0, page = currentPage)
      }
    }
    Log.d(TAG, "totalPage: $totalPage | currentPage: $currentPage")
  }

  private fun getMovieDetail(movie_id: Long) {
    if (movieDetailPresenter == null) {
      movieDetailPresenter = MovieDetailPresenter(this)
        .setViewContract(this)
    }
    movieDetailPresenter?.getMovieDetail(movie_id)
  }

  private fun showGenreSelectDialog(activity: Activity) {
    if(mGenreSelectDialog?.isVisible != true){
      mGenreSelectDialog = GenreSelectDialog(activity)
        .setListener(object : GenreSelectDialog.Companion.GenreSelectDialogListener {
          override fun onClick(genreResult: GenreResult) {
            mGenreResult = genreResult
            mPrimaryToolbar?.setSubtitle("Genre: ${mGenreResult?.name ?: "Not Set"}")
            currentPage = 0
            totalPage = 0
            getMovies(mGenreResult)
          }
        })
      mGenreSelectDialog?.show(supportFragmentManager, GenreSelectDialog.TAG)
    }
  }

  override fun onLoading() {
    if(currentPage <= 1){
      if (dialogProgress == null) dialogProgress = DialogProgress(this)
      dialogProgress?.show()
      _binding.progressbar.visibility = GONE
    } else {
      _binding.progressbar.visibility = VISIBLE
      dialogProgress?.dismiss()
    }
  }

  override fun onDismissLoading() {
    dialogProgress?.dismiss()
    _binding.progressbar.visibility = GONE
  }

  override fun onLoadingMovieDetail() {
    if (dialogProgress == null) dialogProgress = DialogProgress(this)
    dialogProgress?.show()
  }

  override fun onDismissLoadingMovieDetail() {
    dialogProgress?.dismiss()
  }

  override fun onSuccessGetMovieDetail(movieDetailResult: MovieDetailResult) {
    Log.d(TAG, "onSuccessGetMovieDetail")
    startActivity(MovieDetailActivity.getIntent(this, movieDetailResult = movieDetailResult))
  }

  override fun onFailedGetMovieDetail(message: String) {
    Log.e(TAG, "onFailedGetMovieDetail: $message")
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
  }

  override fun onSuccessGetMovies(items: List<MovieResult>, totalPage: Int, currentPage: Int, totalResult: Int) {
    Log.d(TAG, "onSuccessGetMovies")
    this.currentPage = currentPage
    this.totalPage = totalPage
    if(this.currentPage <= 1){
      moviesAdapter?.setItems(items)
    } else {
      moviesAdapter?.addItems(items)
    }
  }

  override fun onFailedGetMovies(message: String) {
    Log.e(TAG, "onFailedGetMovies: $message")
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
  }

  override fun onClickItem(movieResult: MovieResult) {
    getMovieDetail(movieResult.id)
  }
}