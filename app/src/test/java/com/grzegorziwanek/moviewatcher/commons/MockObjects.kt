package com.grzegorziwanek.moviewatcher.commons

import com.grzegorziwanek.moviewatcher.details.model.MovieDetails
import com.grzegorziwanek.moviewatcher.movies.model.Movie

fun movieDetailsMock() = MovieDetails(
  1, "Titanic", "some_url",
  "some_url", 100f, "Description",
  "10-10-2019", 200, false
)

fun movieMock() = Movie(
  1, "Titanic", "some_url",
  "Description", false
)

fun moviesMock() = listOf(movieMock(), movieMock(), movieMock())

fun favouritesIdsMock() = listOf(1)