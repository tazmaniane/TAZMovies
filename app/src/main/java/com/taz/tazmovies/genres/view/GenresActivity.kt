package com.taz.tazmovies.genres.view

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mitrasetia.anda1.Base.view.act.BaseActivity
import com.taz.tazmovies.R
import com.taz.tazmovies.adapter.GenreAdapter
import com.taz.tazmovies.databinding.GenresActivityBinding
import com.taz.tazmovies.genres.GenresContract
import com.taz.tazmovies.genres.GenresPresenter
import com.taz.tazmovies.movies.view.MoviesActivity
import com.taz.tazmovies.responses.GenreResult
import com.taz.tazmovies.utils.DialogProgress
import com.taz.tazmovies.utils.PrimaryToolbar

class GenresActivity : BaseActivity(),
  GenresContract.ViewContract,
  GenreAdapter.Companion.GenreAdapterListener{

  companion object {
    val TAG = this::class.java.simpleName
  }

  lateinit var _binding: GenresActivityBinding

  var dialogProgress: DialogProgress? = null
  lateinit var genresPresenter: GenresPresenter
  lateinit var genreAdapter: GenreAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    _binding = GenresActivityBinding.inflate(layoutInflater)
    setContentView(_binding.root)
    setFullScreen()
    initToolbar()
    initAdapter()
    initPresenter()
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

  private fun initAdapter(){
    genreAdapter = GenreAdapter(this)
      .setListener(this)
    _binding.recyclerView.layoutManager = LinearLayoutManager(this)
    _binding.recyclerView.adapter = genreAdapter
  }

  private fun initPresenter(){
    genresPresenter = GenresPresenter(this)
      .setViewContract(this)
    genresPresenter.getGenres()
  }

  override fun onLoading() {
    if(dialogProgress == null) dialogProgress = DialogProgress(this)
    dialogProgress?.show()
  }

  override fun onDismissLoading() {
    dialogProgress?.dismiss()
  }

  override fun onSuccessGetGenres(items: List<GenreResult>) {
    genreAdapter.setItems(items)
  }

  override fun onFailedGetGenres(message: String) {
    Log.e(TAG, "onFailedGetGenres: $message")
  }

  override fun onClickItem(genreResult: GenreResult) {
    Log.d(TAG, "onClickItem: ${Gson().toJson(genreResult)}")
    startActivity(MoviesActivity.getIntent(this, genreResult = genreResult))
  }
}