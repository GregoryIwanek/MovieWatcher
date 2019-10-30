package com.grzegorziwanek.moviewatcher.details.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.grzegorziwanek.moviewatcher.commons.favouritesIdsMock
import com.grzegorziwanek.moviewatcher.commons.movieDetailsMock
import com.grzegorziwanek.moviewatcher.details.repository.DetailsRepository
import com.grzegorziwanek.moviewatcher.storage.FavouritesStorage
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test

class DetailsUseCasesTest {

  @Rule
  @JvmField
  val rule = InstantTaskExecutorRule()

  private val repository = mock<DetailsRepository>()

  private val storage = mock<FavouritesStorage>()

  private val useCases by lazy { DetailsUseCases(repository, storage) }

  @Test
  fun testDetailsUseCases_requestMovieDetails_Completed() {
    whenever(repository.requestMovieDetails(any()))
      .thenReturn(Observable.just(movieDetailsMock()))

    useCases.requestMovieDetails(1)
      .test()
      .assertComplete()
  }

  @Test
  fun testDetailsUseCases_requestMovieDetails_Error() {
    val throwable = Throwable("Error response")
    whenever(repository.requestMovieDetails(any()))
      .thenReturn(Observable.error(throwable))

    useCases.requestMovieDetails(1)
      .test()
      .assertError(throwable)
  }

  @Test
  fun testDetailsUseCases_requestMovieDetails_SetupFavourites() {
    whenever(repository.requestMovieDetails(any()))
      .thenReturn(Observable.just(movieDetailsMock()))
    whenever(storage.getFavourites())
      .thenReturn(favouritesIdsMock())

    useCases.requestMovieDetails(1)
      .test()
      .assertComplete()
      .assertValue { it.isFavourite }
  }

  @Test
  fun testMovieUseCases_checkFavourite_Completed() {
    whenever(storage.refreshFavourites(any()))
      .thenReturn(Single.just(true))

    useCases.switchFavourite(1)
      .test()
      .assertComplete()
  }

  @Test
  fun testMovieUseCases_checkFavourite_Error() {
    val throwable = Throwable("Error response")
    whenever(storage.refreshFavourites(any()))
      .thenReturn(Single.error(throwable))

    useCases.switchFavourite(1)
      .test()
      .assertError(throwable)
  }
}