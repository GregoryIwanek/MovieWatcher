package com.grzegorziwanek.moviewatcher.movies.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.grzegorziwanek.moviewatcher.commons.favouritesIdsMock
import com.grzegorziwanek.moviewatcher.commons.moviesMock
import com.grzegorziwanek.moviewatcher.movies.model.MoviesResponse
import com.grzegorziwanek.moviewatcher.movies.repository.MoviesRepository
import com.grzegorziwanek.moviewatcher.storage.FavouritesStorage
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test

class MovieUseCasesTest {

  @Rule
  @JvmField
  val rule = InstantTaskExecutorRule()

  private val repository = mock<MoviesRepository>()

  private val storage = mock<FavouritesStorage>()

  private val useCases by lazy { MovieUseCases(repository, storage) }

  @Test
  fun testMovieUseCases_requestMovies_Completed() {
    whenever(repository.requestMovies())
      .thenReturn(Observable.just(MoviesResponse(emptyList())))

    useCases.requestMovies()
      .test()
      .assertComplete()
  }

  @Test
  fun testMovieUseCases_requestMovies_Error() {
    val throwable = Throwable("Error response")
    whenever(repository.requestMovies())
      .thenReturn(Observable.error(throwable))

    useCases.requestMovies()
      .test()
      .assertError(throwable)
  }

  @Test
  fun testMovieUseCases_requestMovies_SetupFavourites() {
    whenever(repository.requestMovies())
      .thenReturn(Observable.just(MoviesResponse(moviesMock())))
    whenever(storage.getFavourites())
      .thenReturn(favouritesIdsMock())

    useCases.requestMovies()
      .test()
      .assertComplete()
      .assertValue { it.results.first().isFavourite }
  }

  @Test
  fun testMovieUseCases_searchByQuery_Completed() {
    whenever(repository.searchByName(any()))
      .thenReturn(Observable.just(MoviesResponse(emptyList())))

    useCases.searchByQuery("")
      .test()
      .assertComplete()
  }

  @Test
  fun testMovieUseCases_searchByQuery_Error() {
    val throwable = Throwable("Error response")
    whenever(repository.searchByName(any()))
      .thenReturn(Observable.error(throwable))

    useCases.searchByQuery("")
      .test()
      .assertError(throwable)
  }

  @Test
  fun testMovieUseCases_searchByQuery_SetupFavourites() {
    whenever(repository.searchByName(any()))
      .thenReturn(Observable.just(MoviesResponse(moviesMock())))
    whenever(storage.getFavourites())
      .thenReturn(favouritesIdsMock())

    useCases.searchByQuery("")
      .test()
      .assertComplete()
      .assertValue { it.results.first().isFavourite }
  }

  @Test
  fun testMovieUseCases_checkFavourite_Completed() {
    whenever(storage.refreshFavourites(any()))
      .thenReturn(Single.just(true))

    useCases.checkFavourite(1)
      .test()
      .assertComplete()
  }

  @Test
  fun testMovieUseCases_checkFavourite_Error() {
    val throwable = Throwable("Error response")
    whenever(storage.refreshFavourites(any()))
      .thenReturn(Single.error(throwable))

    useCases.checkFavourite(1)
      .test()
      .assertError(throwable)
  }
}