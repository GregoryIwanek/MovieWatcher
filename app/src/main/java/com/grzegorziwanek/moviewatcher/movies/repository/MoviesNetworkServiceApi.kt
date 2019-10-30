package com.grzegorziwanek.moviewatcher.movies.repository

import com.grzegorziwanek.moviewatcher.movies.model.MoviesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesNetworkServiceApi {

  @GET("movie/now_playing")
  fun getMovies(): Observable<MoviesResponse>

  @GET("search/movie")
  fun searchByName(@Query("query") query: String): Observable<MoviesResponse>
}
