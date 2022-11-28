package com.taz.tazmovies.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.taz.tazmovies.Constants.Companion.BASE_IMAGE_URL_API
import com.taz.tazmovies.R
import com.taz.tazmovies.databinding.MovieItemViewBinding
import com.taz.tazmovies.responses.MovieResult

class MoviesAdapter(
  private val context: Context
) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

  companion object {
    private val TAG = this::class.java.simpleName

    interface MoviesAdapterListener {
      fun onClickItem(movieResult: MovieResult)
    }
  }

  private lateinit var _binding: MovieItemViewBinding

  var items = mutableListOf<MovieResult>()
  var listener: MoviesAdapterListener? = null

  fun setListener(listener: MoviesAdapterListener): MoviesAdapter {
    this.listener = listener
    return this
  }

  fun setItems(items: List<MovieResult>): MoviesAdapter {
    this.items.clear()
    this.items.addAll(items)
    notifyDataSetChanged()
    return this
  }

  fun addItems(items: List<MovieResult>): MoviesAdapter {
    val index = this.items.size
    this.items.addAll(index, items)
    notifyItemRangeInserted(index, (this.items.size - 1))
    return this
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    _binding = MovieItemViewBinding.inflate(LayoutInflater.from(context), parent, false)
    return ViewHolder(_binding)
  }

  override fun getItemCount() = items.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindView(items[position], _binding)
  }

  inner class ViewHolder(binding: MovieItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindView(movieResult: MovieResult, binding: MovieItemViewBinding) {
      binding.tvMovieTitle.text = movieResult.title
      var url = BASE_IMAGE_URL_API
      url += if (!movieResult.backdrop_path.isNullOrEmpty()) {
        movieResult.backdrop_path
      } else movieResult.poster_path
      // if (movieResult.title.length > 30)
      //   itemView.tv_movie_title.isSelected = true
      binding.tvVoteAverage.text = movieResult.vote_average.toString()
      Picasso.get()
        .load(url)
        .error(R.drawable.ic_holder)
        .into(binding.imvMovie)

      binding.cardView.setOnClickListener {
        listener?.onClickItem(movieResult)
      }
    }
  }
}