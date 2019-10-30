package com.grzegorziwanek.moviewatcher.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel<EVENT> : ViewModel() {

  protected val compositeDisposable = CompositeDisposable()

  val events: MutableLiveData<EVENT> = MutableLiveData()

  override fun onCleared() {
    compositeDisposable.clear()
    super.onCleared()
  }

  companion object {

    @JvmStatic
    protected val DEFAULT_ERROR_MESSAGE = "Something went wrong."
  }
}