package com.connor.moviecat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.connor.moviecat.R
import com.connor.moviecat.databinding.ItemTrendingBinding
import com.connor.moviecat.model.net.MovieUiResult
import com.connor.moviecat.utlis.ImageUtils
import com.connor.moviecat.utlis.loadWithQuality

class TrendingAdapter(private val onClick: (MovieUiResult) -> Unit) :
    PagingDataAdapter<MovieUiResult, TrendingAdapter.ViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<MovieUiResult>() {
            override fun areItemsTheSame(oldItem: MovieUiResult, newItem: MovieUiResult): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MovieUiResult,
                newItem: MovieUiResult
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(
        private val binding: ItemTrendingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imgLoad.setOnClickListener {
                binding.results?.let {
                    onClick(it)
                }
            }
        }

        fun bind(result: MovieUiResult) {
            binding.results = result
            binding.imgLoad.loadWithQuality(
                "${ImageUtils.IMAGE_API_URL}${result.posterPath}",
                "${ImageUtils.IMAGE_W_500}${result.posterPath}",
                R.drawable.placeholder,
                R.drawable.placeholder
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemTrendingBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_trending,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = getItem(position)
        repo?.let { holder.bind(it) }
    }
}