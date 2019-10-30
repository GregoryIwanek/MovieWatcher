package com.grzegorziwanek.moviewatcher.details.viewmodel

import com.grzegorziwanek.moviewatcher.common.BaseViewModel
import com.grzegorziwanek.moviewatcher.details.usecase.DetailsUseCases
import com.grzegorziwanek.moviewatcher.utils.pushObservableToBackgroundThread
import com.grzegorziwanek.moviewatcher.utils.pushSingleToBackgroundThread

class DetailsViewModel(private val useCase: DetailsUseCases) : BaseViewModel<DetailsEvent>() {

  fun getDetails(id: Int) {
    emitLoading()
    useCase.requestMovieDetails(id)
      .compose(pushObservableToBackgroundThread())
      .subscribe({ events.value = DetailsEvent.LoadingSuccess(it) }, { emitError(it) })
      .let { compositeDisposable.add(it) }
  }

  fun switchFavourite(id: Int) {
    useCase.switchFavourite(id)
      .compose(pushSingleToBackgroundThread())
      .subscribe({ events.value = DetailsEvent.FavouriteSwitched(it) }, { emitError(it) })
      .let { compositeDisposable.add(it) }
  }

  private fun emitError(error: Throwable) {
    events.value = DetailsEvent.Error(error.message ?: DEFAULT_ERROR_MESSAGE)
  }

  private fun emitLoading() = events.postValue(DetailsEvent.Loading)
}
