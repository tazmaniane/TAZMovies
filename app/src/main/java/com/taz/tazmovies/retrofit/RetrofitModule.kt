package com.taz.tazmovies.retrofit

import com.taz.tazmovies.Constants
import com.taz.tazmovies.Constants.Companion.API_KEY
import com.taz.tazmovies.Constants.Companion.BASE_URL_API
import com.taz.tazmovies.Constants.Companion.LANGUAGE
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitModule {

    private val connectionTimeout: Int = 10000

    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(connectionTimeout.toLong(), TimeUnit.MILLISECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url()
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .addQueryParameter("language", LANGUAGE)
                    .build()
                val requestBuilder = original.newBuilder()
                    .url(url)
                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    fun provideRetrofit(): Api {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build().create(Api::class.java)
    }
}