package com.grzegorziwanek.moviewatcher.movies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.grzegorziwanek.moviewatcher.R
import com.grzegorziwanek.moviewatcher.movies.model.Movie
import com.grzegorziwanek.moviewatcher.utils.DiffUtilAdapter
import com.grzegorziwanek.moviewatcher.utils.ImageLoader
import com.grzegorziwanek.moviewatcher.utils.getStringFromRes
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_movie.view.*

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

  private val clickedMovie = PublishSubject.create<Int>()

  private val clickedFavourite = PublishSubject.create<Int>()

  private val items = mutableListOf<Movie>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
    return MovieViewHolder(view, clickedMovie, clickedFavourite)
  }

  override fun getItemCount(): Int = items.size

  override fun onBindViewHolder(holder: MovieViewHolder, position: Int) = holder.bind(items[position])

  fun clickedMovie(): Observable<Int> = clickedMovie.doOnNext { println(it) }

  fun checkedFavourite(): Observable<Int> = clickedFavourite.doOnNext { idClicked -> updateFavourite(idClicked) }

  private fun updateFavourite(id: Int) =
    items.forEachIndexed { index, movie ->
      if (movie.id == id) {
        items[index] = movie.copy(isFavourite = !movie.isFavourite)
      }
    }

  fun setData(items: List<Movie>) {
    val diffCallback = DiffUtilAdapter(this.items, items)
    val diffResult = DiffUtil.calculateDiff(diffCallback)
    diffResult.dispatchUpdatesTo(this)
    this.items.clear()
    this.items.addAll(items)
  }

  fun clear() {
    items.clear()
    notifyDataSetChanged()
  }

  inner class MovieViewHolder(
    itemView: View,
    private val clickSubject: Observer<Int>,
    private val favouriteSubject: Observer<Int>
  ) : RecyclerView.ViewHolder(itemView) {

    fun bind(movie: Movie) {
      setupViews(movie)
      setupListeners(movie)
    }

    private fun setupViews(movie: Movie) {
      val url = String.format(itemView.context.getString(R.string.movie_img_w500_url), movie.poster)
      ImageLoader(itemView.context).loadImageFromUrl(url, R.color.overlay_dark_10, itemView.image)
      itemView.movieTitle.text = movie.title
      itemView.overview.text = movie.overview
      if (movie.isFavourite) {
        itemView.starFavourite.tag = getStringFromRes(itemView.context, R.string.favourite)
        itemView.starFavourite.setBackgroundResource(R.drawable.ic_star)
      } else {
        itemView.starFavourite.tag = getStringFromRes(itemView.context, R.string.non_favourite)
        itemView.starFavourite.setBackgroundResource(R.drawable.ic_star_border)
      }
    }

    private fun setupListeners(movie: Movie) {
      itemView.setOnClickListener { clickSubject.onNext(movie.id) }
      itemView.starFavourite.setOnClickListener {
        favouriteSubject.onNext(movie.id)
        itemView.starFavourite.apply {
          tag = if (tag == getStringFromRes(itemView.context, R.string.favourite)) {
            setBackgroundResource(R.drawable.ic_star_border)
            getStringFromRes(itemView.context, R.string.non_favourite)
          } else {
            setBackgroundResource(R.drawable.ic_star)
            getStringFromRes(itemView.context, R.string.favourite)
          }
        }
      }
    }
  }
}
