package com.grzegorziwanek.moviewatcher.movies.viewmodel

import com.grzegorziwanek.moviewatcher.common.BaseViewModel
import com.grzegorziwanek.moviewatcher.movies.model.MoviesResponse
import com.grzegorziwanek.moviewatcher.movies.usecase.MovieUseCases
import com.grzegorziwanek.moviewatcher.utils.pushObservableToBackgroundThread
import com.grzegorziwanek.moviewatcher.utils.pushSingleToBackgroundThread

class MoviesViewModel(
  private val usecase: MovieUseCases
) : BaseViewModel<MoviesEvent>() {

  fun requestNowPlaying() {
    emitLoading()
    usecase.requestMovies()
      .compose(pushObservableToBackgroundThread())
      .subscribe({ emitLoadingEvent(it) }, { emitError(it) })
      .let { compositeDisposable.add(it) }
  }

  fun search(query: String) {
    emitLoading()
    usecase.searchByQuery(query)
      .compose(pushObservableToBackgroundThread())
      .subscribe({ emitLoadingEvent(it) }, { emitError(it) })
      .let { compositeDisposable.add(it) }
  }

  fun favouriteSwitched(id: Int) {
    usecase.checkFavourite(id)
      .compose(pushSingleToBackgroundThread())
      .subscribe({ events.value = MoviesEvent.FavouritesChanged }, { emitError(it) })
      .let { compositeDisposable.add(it) }
  }

  private fun emitLoadingEvent(response: MoviesResponse) {
    events.value = if (response.results.isNotEmpty()) {
      MoviesEvent.LoadingSuccess(response)
    } else {
      MoviesEvent.LoadingEmpty
    }
  }

  private fun emitError(error: Throwable) {
    events.value = MoviesEvent.Error(error.message ?: DEFAULT_ERROR_MESSAGE)
  }

  private fun emitLoading() = events.postValue(MoviesEvent.Loading)
}