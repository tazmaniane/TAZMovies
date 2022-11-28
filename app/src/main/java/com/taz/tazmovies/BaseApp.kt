package com.taz.tazmovies

import android.app.Application
import com.taz.tazmovies.retrofit.Api
import com.taz.tazmovies.retrofit.RetrofitModule

class BaseApp : Application() {

  companion object {
    lateinit var instance: BaseApp private set
  }

  lateinit var retrofit: Api

  override fun onCreate() {
    super.onCreate()
    instance = this
    retrofit = RetrofitModule().provideRetrofit()
  }
}