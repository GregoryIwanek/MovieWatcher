package com.grzegorziwanek.moviewatcher.details.viewmodel

import com.grzegorziwanek.moviewatcher.details.model.MovieDetails

sealed class DetailsEvent {

  data class FavouriteSwitched(val isFavourite: Boolean) : DetailsEvent()

  data class LoadingSuccess(val details: MovieDetails) : DetailsEvent()

  data class Error(val message: String) : DetailsEvent()

  object Loading : DetailsEvent()
}
