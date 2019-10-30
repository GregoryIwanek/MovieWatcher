package com.grzegorziwanek.moviewatcher.utils

import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> pushObservableToBackgroundThread(): ObservableTransformer<T, T> {
  return ObservableTransformer { observable ->
    observable
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
  }
}

fun <T> pushSingleToBackgroundThread(): SingleTransformer<T, T> {
  return SingleTransformer { observable ->
    observable
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
  }
}