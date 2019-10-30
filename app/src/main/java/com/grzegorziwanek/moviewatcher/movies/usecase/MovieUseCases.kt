package com.grzegorziwanek.moviewatcher.movies.usecase

import com.grzegorziwanek.moviewatcher.movies.model.Movie
import com.grzegorziwanek.moviewatcher.movies.model.MoviesResponse
import com.grzegorziwanek.moviewatcher.movies.repository.MoviesRepository
import com.grzegorziwanek.moviewatcher.storage.FavouritesStorage
import io.reactivex.Observable

class MovieUseCases(
  private val repository: MoviesRepository,
  private val storage: FavouritesStorage
) {

  fun checkFavourite(id: Int) = storage.refreshFavourites(id)

  fun requestMovies(): Observable<MoviesResponse> = repository.requestMovies().map { setupFavourites(it) }

  fun searchByQuery(query: String): Observable<MoviesResponse> = repository.searchByName(query).map { setupFavourites(it) }

  private fun setupFavourites(response: MoviesResponse): MoviesResponse {
    val items = mutableListOf<Movie>()
    val favourites = storage.getFavourites()
    response.results.forEach {
      if (favourites.contains(it.id)) {
        items.add(it.copy(isFavourite = true))
      } else {
        items.add(it.copy(isFavourite = false))
      }
    }
    return response.copy(results = items)
  }
}