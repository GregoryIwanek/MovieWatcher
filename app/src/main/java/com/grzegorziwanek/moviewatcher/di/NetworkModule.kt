package com.grzegorziwanek.moviewatcher.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

  single { provideAuthenticationInterceptor() }

  single { provideRetrofit(get(), get()) }

  single { provideOkHttpClient(get()) }

  single { provideGson() }
}

private fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
  return Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()
}

private fun provideOkHttpClient(authenticationInterceptor: Interceptor): OkHttpClient {
  return OkHttpClient.Builder()
    .addInterceptor(authenticationInterceptor)
    .connectTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .build()
}

private fun provideAuthenticationInterceptor(): Interceptor {
  return Interceptor { chain ->
    var request = chain.request()
    val url = request.url().newBuilder()
      .addQueryParameter(API_KEY, API_KEY_VALUE)
      .build()
    request = request.newBuilder().url(url).build()
    chain.proceed(request)
  }
}

private fun provideGson() = GsonBuilder().create()

private const val BASE_URL = "https://api.themoviedb.org/3/"

private const val API_KEY = "api_key"

private const val API_KEY_VALUE = "daba85c9ea873342363bc13cdca9971b"