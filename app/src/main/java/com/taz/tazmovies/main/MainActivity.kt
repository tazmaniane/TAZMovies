package com.taz.tazmovies.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.mitrasetia.anda1.Base.view.act.BaseActivity
import com.taz.tazmovies.R
import com.taz.tazmovies.adapter.GenreAdapter
import com.taz.tazmovies.adapter.MoviesAdapter
import com.taz.tazmovies.databinding.GenresActivityBinding
import com.taz.tazmovies.databinding.MainActivityBinding
import com.taz.tazmovies.genres.GenresContract
import com.taz.tazmovies.genres.GenresPresenter
import com.taz.tazmovies.movies.MovieDetailContract
import com.taz.tazmovies.movies.MovieDetailPresenter
import com.taz.tazmovies.movies.MoviesContract
import com.taz.tazmovies.movies.MoviesPresenter
import com.taz.tazmovies.movies.view.MovieDetailActivity
import com.taz.tazmovies.movies.view.MoviesActivity
import com.taz.tazmovies.responses.GenreResult
import com.taz.tazmovies.responses.MovieDetailResult
import com.taz.tazmovies.responses.MovieResult
import com.taz.tazmovies.utils.DialogProgress
import com.taz.tazmovies.utils.PrimaryToolbar
import com.taz.tazmovies.utils.TabUtils

class MainActivity : BaseActivity(),
  GenresContract.ViewContract,
  MoviesContract.ViewContract,
  MovieDetailContract.ViewContract {

  companion object {
    val TAG = this::class.java.simpleName
  }

  lateinit var _binding: MainActivityBinding

  var mGenresPresenter: GenresPresenter? = null
  var mMoviesPresenter: MoviesPresenter? = null
  var mMovieDetailPresenter: MovieDetailPresenter? = null

  var mMoviesAdapter: MoviesAdapter? = null

  var visibleItemCount = 0
  var totalItemCount = 0
  var pastItemsVisible = 0
  var totalPage = 0
  var currentPage = 0

  var mGenreItems = mutableListOf<GenreResult>()
  var mGenreResult: GenreResult? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    _binding = MainActivityBinding.inflate(layoutInflater)
    setContentView(_binding.root)
    setFullScreen()
    initToolbar()
    initMoviesAdapter()
    getGenres()
  }

  private fun initToolbar() {
    setSupportActionBar(_binding.appbarViewIcl.toolbarView)
    displayShowHomeEnabled(true)
    PrimaryToolbar(this)
      .setToolbar(_binding.appbarViewIcl.toolbarView)
      .setTitle(getString(R.string.app_name))
      .setHideActionButton(true)
      .setHideBackButton(true)
      .setBackgroundColor(android.R.color.transparent)
      .init()
  }

  private fun getGenres(){
    if(mGenresPresenter == null){
      mGenresPresenter = GenresPresenter(this)
        .setViewContract(this)
    }
    mGenresPresenter?.getGenres()
  }

  private fun initMoviesAdapter() {
    mMoviesAdapter = MoviesAdapter(this)
      .setListener(object: MoviesAdapter.Companion.MoviesAdapterListener {
        override fun onClickItem(movieResult: MovieResult) {
          getMovieDetail(movieResult.id)
        }
      })
    _binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
    _binding.recyclerView.adapter = mMoviesAdapter
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

  @SuppressLint("SetTextI18n")
  private fun getMovies(genreResult: GenreResult?) {
    if (mMoviesPresenter == null) {
      mMoviesPresenter = MoviesPresenter(this)
        .setViewContract(this)
    }
    // moviesPresenter?.getMovies(genreResult?.id ?: 0)
    if(totalPage == 0) {
      currentPage = 1
      mMoviesPresenter?.getMovies(with_genres = genreResult?.id ?: 0, page = currentPage)
    } else {
      if(currentPage < totalPage) {
        currentPage++
        mMoviesPresenter?.getMovies(with_genres = genreResult?.id ?: 0, page = currentPage)
      }
    }
    _binding.tvGenreTitle.text = "Genre: ${genreResult?.name ?: "-"}"
    Log.d(MoviesActivity.TAG, "totalPage: $totalPage | currentPage: $currentPage")
  }

  private fun getMovieDetail(movie_id: Long) {
    if (mMovieDetailPresenter == null) {
      mMovieDetailPresenter = MovieDetailPresenter(this)
        .setViewContract(this)
    }
    mMovieDetailPresenter?.getMovieDetail(movie_id)
  }

  private fun initTabLayout(items: List<GenreResult>) {
    mGenreItems.clear()
    mGenreItems.addAll(items)
    var firstTab: TabLayout.Tab? = null
    for (i in mGenreItems.indices) {
      val tabView = TabUtils.getTabItemTransparent(this, mGenreItems[i].name)
      if(i == 0) {
        firstTab = _binding.tabLayout.newTab().setCustomView(tabView)
        _binding.tabLayout.addTab(firstTab, i, true)
      } else {
        _binding.tabLayout.addTab(_binding.tabLayout.newTab().setCustomView(tabView), i, false)
      }
    }
    _binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
      override fun onTabSelected(tab: TabLayout.Tab?) {
        mGenreResult = mGenreItems[tab?.position ?: 0]
        getMovies(mGenreResult)
      }

      override fun onTabUnselected(tab: TabLayout.Tab?) { }

      override fun onTabReselected(tab: TabLayout.Tab?) { }
    })
    _binding.tabLayout.selectTab(firstTab)
    _binding.tabLayout.isSelected = true
    mGenreResult = if(mGenreItems.isNotEmpty()) mGenreItems[0] else null
    getMovies(mGenreResult)
  }


  override fun onLoading() {
    showLoading()
  }

  override fun onDismissLoading() {
    dismissLoading()
  }

  override fun onLoadingGetMovies() {
    if(currentPage <= 1){
      showLoading()
      _binding.progressbar.visibility = View.GONE
    } else {
      _binding.progressbar.visibility = View.VISIBLE
      dismissLoading()
    }
  }

  override fun onDismissLoadingGetMovies() {
    dismissLoading()
    _binding.progressbar.visibility = View.GONE
  }

  override fun onSuccessGetMovies(
    items: List<MovieResult>,
    totalPage: Int,
    currentPage: Int,
    totalResult: Int
  ) {
    Log.d(MoviesActivity.TAG, "onSuccessGetMovies")
    this.currentPage = currentPage
    this.totalPage = totalPage
    if(this.currentPage <= 1){
      mMoviesAdapter?.setItems(items)
    } else {
      mMoviesAdapter?.addItems(items)
    }
  }

  override fun onFailedGetMovies(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
  }

  override fun onSuccessGetGenres(items: List<GenreResult>) {
    initTabLayout(items)
  }

  override fun onFailedGetGenres(message: String) {
    Log.e(TAG, "onFailedGetGenres: $message")
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
  }

  override fun onLoadingMovieDetail() {
    showLoading()
  }

  override fun onDismissLoadingMovieDetail() {
    dismissLoading()
  }

  override fun onSuccessGetMovieDetail(movieDetailResult: MovieDetailResult) {
    startActivity(MovieDetailActivity.getIntent(this, movieDetailResult = movieDetailResult))
  }

  override fun onFailedGetMovieDetail(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
  }
}