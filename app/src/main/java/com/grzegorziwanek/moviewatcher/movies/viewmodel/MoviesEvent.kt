package com.grzegorziwanek.moviewatcher.movies.viewmodel

import com.grzegorziwanek.moviewatcher.movies.model.MoviesResponse

sealed class MoviesEvent {

  data class LoadingSuccess(val response: MoviesResponse) : MoviesEvent()

  data class InputModified(val shouldShowClear: Boolean) : MoviesEvent()

  data class Error(val message: String) : MoviesEvent()

  object FavouritesChanged : MoviesEvent()

  object LoadingEmpty : MoviesEvent()

  object Loading : MoviesEvent()
}
