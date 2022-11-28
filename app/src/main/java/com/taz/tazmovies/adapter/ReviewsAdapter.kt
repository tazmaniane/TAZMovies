package com.taz.tazmovies.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.taz.tazmovies.Constants.Companion.BASE_IMAGE_URL_API
import com.taz.tazmovies.R
import com.taz.tazmovies.databinding.ReviewItemViewBinding
import com.taz.tazmovies.responses.ReviewResult

class ReviewsAdapter(
  private val context: Context
) : RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {

  companion object {
    private val TAG = this::class.java.simpleName

    interface ReviewsAdapterListener {
      fun onClickItem(reviewResult: ReviewResult)
    }
  }

  private lateinit var _binding: ReviewItemViewBinding

  var items = mutableListOf<ReviewResult>()
  var listener: ReviewsAdapterListener? = null

  fun setListener(listener: ReviewsAdapterListener): ReviewsAdapter {
    this.listener = listener
    return this
  }

  fun setItems(items: List<ReviewResult>): ReviewsAdapter {
    this.items.clear()
    this.items.addAll(items)
    notifyDataSetChanged()
    return this
  }

  fun addItems(items: List<ReviewResult>): ReviewsAdapter {
    val index = this.items.size
    this.items.addAll(index, items)
    notifyItemRangeInserted(index, (this.items.size - 1))
    return this
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    _binding = ReviewItemViewBinding.inflate(LayoutInflater.from(context), parent, false)
    return ViewHolder(_binding)
  }

  override fun getItemCount() = items.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindView(items[position], _binding)
  }

  inner class ViewHolder(binding: ReviewItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bindView(reviewResult: ReviewResult, binding: ReviewItemViewBinding) {
      binding.tvName.text = "${reviewResult.reviewDetail.name} (${reviewResult.reviewDetail.username})"
      binding.tvContent.text = reviewResult.content
      binding.tvCreateDate.text = if(reviewResult.updated_at.isNotEmpty()){
        "Diubah pada ${reviewResult.updated_at}"
      } else {
        reviewResult.created_at
      }
      binding.tvRating.text = (reviewResult.reviewDetail.rating ?: 0).toString()

      // var url = BASE_IMAGE_URL_API
      // url += if (!movieResult.backdrop_path.isNullOrEmpty()) {
      //   movieResult.backdrop_path
      // } else movieResult.poster_path
      // // if (movieResult.title.length > 30)
      // //   itemView.tv_movie_title.isSelected = true
      // binding.tvVoteAverage.text = movieResult.vote_average.toString()
      // Picasso.get()
      //   .load(url)
      //   .error(R.drawable.ic_holder)
      //   .into(binding.imvMovie)

      binding.cardView.setOnClickListener {
        listener?.onClickItem(reviewResult)
      }
    }
  }
}