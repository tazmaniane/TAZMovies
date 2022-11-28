package com.taz.tazmovies.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taz.tazmovies.databinding.GenreItemViewBinding
import com.taz.tazmovies.responses.GenreResult

class GenreAdapter(
  private val context: Context
) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

  companion object {
    private val TAG = this::class.java.simpleName

    interface GenreAdapterListener {
      fun onClickItem(genreResult: GenreResult)
    }
  }

  private lateinit var _binding: GenreItemViewBinding

  var items = mutableListOf<GenreResult>()
  var filteredItems = mutableListOf<GenreResult>()
  var listener: GenreAdapterListener? = null

  fun setListener(listener: GenreAdapterListener): GenreAdapter {
    this.listener = listener
    return this
  }

  fun setItems(items: List<GenreResult>): GenreAdapter {
    this.items.clear()
    this.items.addAll(items)
    this.filteredItems.clear()
    this.filteredItems.addAll(items)
    this.notifyDataSetChanged()
    return this
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    _binding = GenreItemViewBinding.inflate(LayoutInflater.from(context), parent, false)
    return ViewHolder(_binding)
  }

  override fun getItemCount() = filteredItems.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindView(filteredItems[position], _binding)
  }

  inner class ViewHolder(binding: GenreItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindView(genreResult: GenreResult, binding: GenreItemViewBinding) {
      binding.tvTitle.text = genreResult.name
      binding.cardView.setOnClickListener {
        listener?.onClickItem(genreResult)
      }
    }
  }

  @SuppressLint("NotifyDataSetChanged")
  fun excuteFilter(searchText: String) {
    this.filteredItems.clear()
    if (searchText.isNotEmpty()) {
      this.filteredItems.addAll(
        items.filter { item ->
          item.name.toString().lowercase().contains(searchText.toString().lowercase())
        }
      )
    } else {
      this.filteredItems.addAll(items)
    }
    this.notifyDataSetChanged()
  }
}