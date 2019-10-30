package com.grzegorziwanek.moviewatcher.details.repository

class DetailsRepository(private val networkService: DetailsNetworkServiceApi) {

  fun requestMovieDetails(id: Int) = networkService.getMovieDetails(id)
}
