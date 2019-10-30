package com.grzegorziwanek.moviewatcher.details.model

import com.google.gson.annotations.SerializedName

data class MovieDetails(
  val id: Int,
  val title: String,
  @SerializedName("poster_path")
  val poster: String?,
  @SerializedName("backdrop_path")
  val backdrop: String?,
  val popularity: Float,
  val overview: String,
  @SerializedName("release_date")
  val releaseDate: String,
  val revenue: Int,
  val isFavourite: Boolean = false
)