package com.grzegorziwanek.moviewatcher.details.usecase

import com.grzegorziwanek.moviewatcher.details.model.MovieDetails
import com.grzegorziwanek.moviewatcher.details.repository.DetailsRepository
import com.grzegorziwanek.moviewatcher.storage.FavouritesStorage
import io.reactivex.Observable

class DetailsUseCases(
  private val repository: DetailsRepository,
  private val storage: FavouritesStorage
) {

  fun switchFavourite(id: Int) = storage.refreshFavourites(id)

  fun requestMovieDetails(id: Int): Observable<MovieDetails> {
    return repository.requestMovieDetails(id).map { details ->
      if (storage.getFavourites().contains(details.id)) {
        details.copy(isFavourite = true)
      } else {
        details.copy(isFavourite = false)
      }
    }
  }
}