package com.grzegorziwanek.moviewatcher.movies.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.grzegorziwanek.moviewatcher.commons.RxImmediateSchedulerRule
import com.grzegorziwanek.moviewatcher.commons.moviesMock
import com.grzegorziwanek.moviewatcher.movies.model.MoviesResponse
import com.grzegorziwanek.moviewatcher.movies.usecase.MovieUseCases
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.times

class MoviesViewModelTest {

  @Rule
  @JvmField
  val rule = InstantTaskExecutorRule()

  companion object {
    @JvmField
    @ClassRule
    val schedulers = RxImmediateSchedulerRule()
  }

  private val useCases = mock<MovieUseCases>()

  private val observerState = mock<Observer<MoviesEvent>>()

  private val viewModel by lazy { MoviesViewModel(useCases) }

  @Before
  fun init() {
    reset(useCases, observerState)
  }

  @Test
  fun testMoviesViewModel_requestNowPlaying_EventLoadingSuccess() {
    val response = MoviesResponse(moviesMock())
    whenever(useCases.requestMovies())
      .thenReturn(Observable.just(response))

    viewModel.events.observeForever(observerState)

    viewModel.requestNowPlaying()

    val argumentCaptor = ArgumentCaptor.forClass(MoviesEvent::class.java)
    val firstState = MoviesEvent.Loading
    val expectedState = MoviesEvent.LoadingSuccess(response)

    argumentCaptor.run {
      verify(observerState, times(2)).onChanged(capture())
      val (initState, resultState) = allValues
      Assert.assertEquals(initState, firstState)
      Assert.assertEquals(resultState, expectedState)
    }
  }

  @Test
  fun testMoviesViewModel_requestNowPlaying_EventError() {
    val throwable = Throwable("Error response")
    whenever(useCases.requestMovies())
      .thenReturn(Observable.error(throwable))

    viewModel.events.observeForever(observerState)

    viewModel.requestNowPlaying()

    val argumentCaptor = ArgumentCaptor.forClass(MoviesEvent::class.java)
    val firstState = MoviesEvent.Loading
    val expectedState = MoviesEvent.Error(throwable.message!!)

    argumentCaptor.run {
      verify(observerState, times(2)).onChanged(capture())
      val (initState, resultState) = allValues
      Assert.assertEquals(initState, firstState)
      Assert.assertEquals(resultState, expectedState)
    }
  }

  @Test
  fun testMoviesViewModel_search_EventLoadingSuccess() {
    val response = MoviesResponse(moviesMock())
    whenever(useCases.searchByQuery(any()))
      .thenReturn(Observable.just(response))

    viewModel.events.observeForever(observerState)

    viewModel.search("")

    val argumentCaptor = ArgumentCaptor.forClass(MoviesEvent::class.java)
    val firstState = MoviesEvent.Loading
    val expectedState = MoviesEvent.LoadingSuccess(response)

    argumentCaptor.run {
      verify(observerState, times(2)).onChanged(capture())
      val (initState, resultState) = allValues
      Assert.assertEquals(initState, firstState)
      Assert.assertEquals(resultState, expectedState)
    }
  }

  @Test
  fun testMoviesViewModel_search_EventError() {
    val throwable = Throwable("Error response")
    whenever(useCases.searchByQuery(any()))
      .thenReturn(Observable.error(throwable))

    viewModel.events.observeForever(observerState)

    viewModel.search("")

    val argumentCaptor = ArgumentCaptor.forClass(MoviesEvent::class.java)
    val firstState = MoviesEvent.Loading
    val expectedState = MoviesEvent.Error(throwable.message!!)

    argumentCaptor.run {
      verify(observerState, times(2)).onChanged(capture())
      val (initState, resultState) = allValues
      Assert.assertEquals(initState, firstState)
      Assert.assertEquals(resultState, expectedState)
    }
  }

  @Test
  fun testMoviesViewModel_favouriteSwitched_EventLoadingSuccess() {
    whenever(useCases.checkFavourite(any()))
      .thenReturn(Single.just(true))

    viewModel.events.observeForever(observerState)

    viewModel.favouriteSwitched(1)

    val argumentCaptor = ArgumentCaptor.forClass(MoviesEvent::class.java)
    val expectedState = MoviesEvent.FavouritesChanged

    argumentCaptor.run {
      verify(observerState, times(1)).onChanged(capture())
      val (resultState) = allValues
      Assert.assertEquals(resultState, expectedState)
    }
  }

  @Test
  fun testMoviesViewModel_favouriteSwitched_EventError() {
    val throwable = Throwable("Error response")
    whenever(useCases.checkFavourite(any()))
      .thenReturn(Single.error(throwable))

    viewModel.events.observeForever(observerState)

    viewModel.favouriteSwitched(1)

    val argumentCaptor = ArgumentCaptor.forClass(MoviesEvent::class.java)
    val expectedState = MoviesEvent.Error(throwable.message!!)

    argumentCaptor.run {
      verify(observerState, times(1)).onChanged(capture())
      val (resultState) = allValues
      Assert.assertEquals(resultState, expectedState)
    }
  }
}