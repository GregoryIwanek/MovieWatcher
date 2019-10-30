package com.grzegorziwanek.moviewatcher.details.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.grzegorziwanek.moviewatcher.commons.RxImmediateSchedulerRule
import com.grzegorziwanek.moviewatcher.commons.movieDetailsMock
import com.grzegorziwanek.moviewatcher.details.usecase.DetailsUseCases
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
import org.mockito.Mockito

class DetailsViewModelTest {

  @Rule
  @JvmField
  val rule = InstantTaskExecutorRule()

  companion object {
    @JvmField
    @ClassRule
    val schedulers = RxImmediateSchedulerRule()
  }

  private val useCases = mock<DetailsUseCases>()

  private val observerState = mock<Observer<DetailsEvent>>()

  private val viewModel by lazy { DetailsViewModel(useCases) }

  @Before
  fun init() {
    reset(useCases, observerState)
  }

  @Test
  fun testMoviesViewModel_getDetails_EventLoadingSuccess() {
    val response = movieDetailsMock()
    whenever(useCases.requestMovieDetails(any()))
      .thenReturn(Observable.just(response))

    viewModel.events.observeForever(observerState)

    viewModel.getDetails(1)

    val argumentCaptor = ArgumentCaptor.forClass(DetailsEvent::class.java)
    val firstState = DetailsEvent.Loading
    val expectedState = DetailsEvent.LoadingSuccess(response)

    argumentCaptor.run {
      verify(observerState, Mockito.times(2)).onChanged(capture())
      val (initState, resultState) = allValues
      Assert.assertEquals(initState, firstState)
      Assert.assertEquals(resultState, expectedState)
    }
  }

  @Test
  fun testMoviesViewModel_getDetails_EventError() {
    val throwable = Throwable("Error response")
    whenever(useCases.requestMovieDetails(any()))
      .thenReturn(Observable.error(throwable))

    viewModel.events.observeForever(observerState)

    viewModel.getDetails(1)

    val argumentCaptor = ArgumentCaptor.forClass(DetailsEvent::class.java)
    val firstState = DetailsEvent.Loading
    val expectedState = DetailsEvent.Error(throwable.message!!)

    argumentCaptor.run {
      verify(observerState, Mockito.times(2)).onChanged(capture())
      val (initState, resultState) = allValues
      Assert.assertEquals(initState, firstState)
      Assert.assertEquals(resultState, expectedState)
    }
  }

  @Test
  fun testMoviesViewModel_favouriteSwitched_EventLoadingSuccess() {
    whenever(useCases.switchFavourite(any()))
      .thenReturn(Single.just(true))

    viewModel.events.observeForever(observerState)

    viewModel.switchFavourite(1)

    val argumentCaptor = ArgumentCaptor.forClass(DetailsEvent::class.java)
    val expectedState = DetailsEvent.FavouriteSwitched(true)

    argumentCaptor.run {
      verify(observerState, Mockito.times(1)).onChanged(capture())
      val (resultState) = allValues
      Assert.assertEquals(resultState, expectedState)
    }
  }

  @Test
  fun testMoviesViewModel_favouriteSwitched_EventError() {
    val throwable = Throwable("Error response")
    whenever(useCases.switchFavourite(any()))
      .thenReturn(Single.error(throwable))

    viewModel.events.observeForever(observerState)

    viewModel.switchFavourite(1)

    val argumentCaptor = ArgumentCaptor.forClass(DetailsEvent::class.java)
    val expectedState = DetailsEvent.Error(throwable.message!!)

    argumentCaptor.run {
      verify(observerState, Mockito.times(1)).onChanged(capture())
      val (resultState) = allValues
      Assert.assertEquals(resultState, expectedState)
    }
  }
}