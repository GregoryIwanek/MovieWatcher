package com.grzegorziwanek.moviewatcher

import android.app.Application
import com.grzegorziwanek.moviewatcher.di.KoinModules
import org.koin.android.ext.android.startKoin

class MovieWatcherApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    startKoin(this, KoinModules.applicationModules(this))
  }
}