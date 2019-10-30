package com.grzegorziwanek.moviewatcher.movies.model

import com.google.gson.annotations.SerializedName

data class Movie(
  val id: Int,
  val title: String,
  @SerializedName("poster_path")
  val poster: String?,
  val overview: String,
  val isFavourite: Boolean = false
)