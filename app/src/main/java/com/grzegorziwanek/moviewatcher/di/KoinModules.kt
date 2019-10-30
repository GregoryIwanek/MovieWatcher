package com.grzegorziwanek.moviewatcher.di

import android.app.Application
import com.grzegorziwanek.moviewatcher.details.repository.DetailsRepository
import com.grzegorziwanek.moviewatcher.details.usecase.DetailsUseCases
import com.grzegorziwanek.moviewatcher.details.viewmodel.DetailsViewModel
import com.grzegorziwanek.moviewatcher.movies.repository.MoviesRepository
import com.grzegorziwanek.moviewatcher.movies.usecase.MovieUseCases
import com.grzegorziwanek.moviewatcher.movies.viewmodel.MoviesViewModel
import com.grzegorziwanek.moviewatcher.storage.FavouritesStorage
import org.koin.dsl.module.module

object KoinModules {

  fun applicationModules(application: Application) = listOf(
    coreModule(application),
    viewModelModule,
    useCaseModule,
    networkModule,
    apiModule,
    utilModule
  )
}

private fun coreModule(application: Application) = module { single { application } }

private val viewModelModule = module {

  single { MoviesViewModel(get()) }

  factory { DetailsViewModel(get()) }
}

private val useCaseModule = module {

  single { MovieUseCases(get(), get()) }

  single { MoviesRepository(get()) }

  single { DetailsUseCases(get(), get()) }

  single { DetailsRepository(get()) }
}

private val utilModule = module {

  single { FavouritesStorage(get()) }
}
