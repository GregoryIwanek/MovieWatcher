package com.grzegorziwanek.moviewatcher.di

import com.grzegorziwanek.moviewatcher.details.repository.DetailsNetworkServiceApi
import com.grzegorziwanek.moviewatcher.movies.repository.MoviesNetworkServiceApi
import org.koin.dsl.module.module
import retrofit2.Retrofit

val apiModule = module {

  factory { provideMoviesNetworkServiceApi(get()) }

  factory { provideMovieDetailNetworkServiceApi(get()) }
}

fun provideMoviesNetworkServiceApi(retrofit: Retrofit): MoviesNetworkServiceApi = retrofit.create(MoviesNetworkServiceApi::class.java)

fun provideMovieDetailNetworkServiceApi(retrofit: Retrofit): DetailsNetworkServiceApi = retrofit.create(DetailsNetworkServiceApi::class.java)
