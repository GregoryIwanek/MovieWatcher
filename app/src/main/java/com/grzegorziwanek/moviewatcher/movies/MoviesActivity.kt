package com.grzegorziwanek.moviewatcher.movies

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.grzegorziwanek.moviewatcher.R
import com.grzegorziwanek.moviewatcher.common.BaseActivity
import com.grzegorziwanek.moviewatcher.details.DetailsActivity
import com.grzegorziwanek.moviewatcher.movies.adapter.MoviesAdapter
import com.grzegorziwanek.moviewatcher.movies.viewmodel.MoviesEvent
import com.grzegorziwanek.moviewatcher.movies.viewmodel.MoviesViewModel
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.activity_movies.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class MoviesActivity : BaseActivity() {

  private val viewModel: MoviesViewModel by inject()

  private val adapter = MoviesAdapter()

  override fun specifyLayoutResId(): Int = R.layout.activity_movies

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setupViews()

    viewModel.events.observe(this, Observer { event ->
      when (event) {
        is MoviesEvent.Loading -> showProgress()
        is MoviesEvent.LoadingSuccess -> handleLoadingSuccess(event)
        is MoviesEvent.FavouritesChanged -> handleFavouriteChanged()
        is MoviesEvent.LoadingEmpty -> handleEmpty()
        is MoviesEvent.Error -> showError(event)
      }
    })

    viewModel.requestNowPlaying()
  }

  private fun setupViews() {
    setupSearchInput()
    setupRecycler()
    setupBindings()
  }

  private fun setupSearchInput() {
    focusCatcher.requestFocus()
    val keywords = resources.getStringArray(R.array.movies_array)
    ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, keywords)
      .also { search.setAdapter(it) }
  }

  private fun setupRecycler() {
    movieList.layoutManager = LinearLayoutManager(this)
    movieList.adapter = adapter
  }

  private fun setupBindings() {
    swipeRefresh.setOnRefreshListener { loadFeed() }

    btnClear.clicks()
      .subscribe { search.text.clear() }
      .let { composite.add(it) }

    btnHelp.clicks()
      .subscribe { showMessage(getString(R.string.info_help)) }
      .let { composite.add(it) }

    search.textChanges()
      .debounce(1, TimeUnit.SECONDS)
      .subscribe { loadFeed() }
      .let { composite.add(it) }

    adapter.clickedMovie()
      .subscribe { showMovieDetails(it) }
      .let { composite.add(it) }

    adapter.checkedFavourite()
      .subscribe { viewModel.favouriteSwitched(it) }
      .let { composite.add(it) }
  }

  private fun showMovieDetails(id: Int) {
    val intent = Intent(this, DetailsActivity::class.java)
    intent.putExtra(MOVIE_ID, id)
    startActivityForResult(intent, MOVIE_CODE)
  }

  private fun handleLoadingSuccess(event: MoviesEvent.LoadingSuccess) {
    adapter.setData(event.response.results)
    hideProgress()
    hideEmpty()
  }

  private fun handleEmpty() {
    adapter.clear()
    hideProgress()
    showEmpty()
  }

  private fun handleFavouriteChanged() = showMessage(getString(R.string.favourite_changed))

  private fun showError(event: MoviesEvent.Error) {
    showMessage(event.message)
    hideProgress()
    showEmpty()
  }

  private fun showEmpty() {
    stateListEmpty.visibility = View.VISIBLE
  }

  private fun hideEmpty() {
    stateListEmpty.visibility = View.INVISIBLE
  }

  private fun showProgress() {
    progress.visibility = View.VISIBLE
  }

  private fun hideProgress() {
    progress.visibility = View.INVISIBLE
    swipeRefresh.isRefreshing = false
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == MOVIE_CODE && resultCode == CODE_RELOAD) {
      loadFeed()
    }
  }

  private fun loadFeed() {
    if (search.text.isEmpty()) {
      viewModel.requestNowPlaying()
    } else {
      viewModel.search(search.text.toString())
    }
  }

  private fun showMessage(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

  companion object {

    private const val MOVIE_ID = "movie_id"

    private const val MOVIE_CODE = 6555

    private const val CODE_RELOAD = 6777
  }
}
