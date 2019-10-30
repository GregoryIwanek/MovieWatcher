package com.grzegorziwanek.moviewatcher.storage

import android.content.Context
import io.reactivex.Single

class FavouritesStorage(context: Context) {

  private val preference = context.getSharedPreferences(MOVIES_SHARED_PREF, Context.MODE_PRIVATE)

  fun refreshFavourites(id: Int): Single<Boolean> = Single.fromCallable {
    val isFavourite: Boolean
    val str = id.toString()
    val favourites = preference.getStringSet(FAVOURITE_MOVIES_KEY, mutableSetOf())
    isFavourite = if (favourites!!.contains(str)) {
      favourites.remove(str)
      false
    } else {
      favourites.add(str)
      true
    }
    preference.edit().remove(FAVOURITE_MOVIES_KEY).apply()
    preference.edit().putStringSet(FAVOURITE_MOVIES_KEY, favourites).apply()
    isFavourite
  }

  fun getFavourites(): List<Int> {
    val favourites = preference.getStringSet(FAVOURITE_MOVIES_KEY, mutableSetOf())
    val items = mutableListOf<Int>()
    favourites?.forEach { items.add(it.toInt()) }
    return items
  }

  companion object {

    private const val MOVIES_SHARED_PREF = "MOVIES_SHARED_PREF"

    private const val FAVOURITE_MOVIES_KEY = "FAVOURITE_MOVIES_KEY"
  }
}