package com.grzegorziwanek.moviewatcher.movies.repository

class MoviesRepository(private val networkService: MoviesNetworkServiceApi) {

  fun requestMovies() = networkService.getMovies()

  fun searchByName(query: String) = networkService.searchByName(query)
}