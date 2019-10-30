package com.grzegorziwanek.moviewatcher.details.repository

import com.grzegorziwanek.moviewatcher.details.model.MovieDetails
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailsNetworkServiceApi {

  @GET("movie/{movie_id}")
  fun getMovieDetails(@Path("movie_id") id: Int): Observable<MovieDetails>
}
