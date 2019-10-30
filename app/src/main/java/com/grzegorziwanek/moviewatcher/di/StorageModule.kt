package com.grzegorziwanek.moviewatcher.di

import android.content.Context
import android.content.SharedPreferences
import com.grzegorziwanek.moviewatcher.storage.FavouritesStorage
import org.koin.dsl.module.module

val storageModule = module {

  single { provideSharedPreferences(get()) }

  single { FavouritesStorage(get()) }
}

fun provideSharedPreferences(context: Context): SharedPreferences =
  context.getSharedPreferences(MOVIES_SHARED_PREF, Context.MODE_PRIVATE)

private const val MOVIES_SHARED_PREF = "MOVIES_SHARED_PREF"
