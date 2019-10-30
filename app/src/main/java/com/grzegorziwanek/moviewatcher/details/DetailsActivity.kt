package com.grzegorziwanek.moviewatcher.details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.grzegorziwanek.moviewatcher.common.BaseActivity
import com.grzegorziwanek.moviewatcher.R
import com.grzegorziwanek.moviewatcher.details.model.MovieDetails
import com.grzegorziwanek.moviewatcher.details.viewmodel.DetailsEvent
import com.grzegorziwanek.moviewatcher.details.viewmodel.DetailsViewModel
import com.grzegorziwanek.moviewatcher.utils.ImageLoader
import com.grzegorziwanek.moviewatcher.utils.getStringFromRes
import com.jakewharton.rxbinding2.view.clicks
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.android.ext.android.inject

class DetailsActivity : BaseActivity() {

  private val viewModel: DetailsViewModel by inject()

  override fun specifyLayoutResId(): Int = R.layout.activity_details

  private var id: Int? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setup()

    viewModel.events.observe(this, Observer { event ->
      when (event) {
        is DetailsEvent.Loading -> showProgress()
        is DetailsEvent.LoadingSuccess -> handleLoadingSuccess(event)
        is DetailsEvent.FavouriteSwitched -> handleFavouriteSwitched(event)
        is DetailsEvent.Error -> showError(event)
      }
    })

    id?.let { viewModel.getDetails(it) } ?: showMessage(getString(R.string.something_went_wrong))
  }

  private fun setup() {
    setupId()
    setupBindings()
  }

  private fun setupId() {
    id = intent.extras?.getInt(MOVIE_ID) ?: 0
  }

  private fun setupBindings() {
    toolbar.setNavigationOnClickListener { onBackPressed() }

    starFavourite.clicks()
      .subscribe { onFavouriteClick() }
      .let { composite.add(it) }
  }

  private fun onFavouriteClick() = id?.let { viewModel.switchFavourite(it) }

  private fun handleLoadingSuccess(event: DetailsEvent.LoadingSuccess) {
    setupFavourite(event.details.isFavourite)
    loadImages(event.details)
    setupTexts(event.details)
    hideProgress()
  }

  private fun loadImages(details: MovieDetails) {
    val posterUrl = String.format(getString(R.string.movie_img_w500_url), details.poster)
    val backdropUrl = String.format(getString(R.string.movie_img_w500_url), details.backdrop)
    ImageLoader(this).loadImageFromUrl(posterUrl, R.color.overlay_dark_10, frontImage)
    ImageLoader(this).loadImageFromUrl(backdropUrl, R.color.overlay_dark_10, backImage)
  }

  private fun setupTexts(details: MovieDetails) {
    releaseDate.text = String.format(getString(R.string.movie_release_date), details.releaseDate)
    rating.text = String.format(getString(R.string.movie_rating), details.popularity.toString())
    revenue.text = String.format(getString(R.string.movie_revenue), details.revenue.toString())
    movieTitle.text = details.title
    overview.text = details.overview
  }

  private fun setupFavourite(isFavourite: Boolean) {
    if (isFavourite) {
      starFavourite.tag = getStringFromRes(this, R.string.favourite)
      starFavourite.setBackgroundResource(R.drawable.ic_star)
    } else {
      starFavourite.tag = getStringFromRes(this, R.string.non_favourite)
      starFavourite.setBackgroundResource(R.drawable.ic_star_border)
    }
  }

  private fun handleFavouriteSwitched(event: DetailsEvent.FavouriteSwitched) {
    showMessage(getString(R.string.favourite_changed))
    setupFavourite(event.isFavourite)
    setResult(CODE_RELOAD)
  }

  private fun showError(event: DetailsEvent.Error) {
    showMessage(event.message)
    hideProgress()
  }

  private fun hideProgress() {
    progress.visibility = View.INVISIBLE
  }

  private fun showProgress() {
    progress.visibility = View.VISIBLE
  }

  private fun showMessage(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

  companion object {

    private const val MOVIE_ID = "movie_id"

    private const val CODE_RELOAD = 6777
  }
}
