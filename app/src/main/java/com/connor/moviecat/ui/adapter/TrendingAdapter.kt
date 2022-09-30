package com.connor.moviecat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.connor.moviecat.R
import com.connor.moviecat.databinding.ItemTrendingBinding
import com.connor.moviecat.model.net.MovieResult
import com.connor.moviecat.utlis.ImageUtils

class TrendingAdapter : PagingDataAdapter<MovieResult, TrendingAdapter.ViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<MovieResult>() {
            override fun areItemsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(private val binding: ItemTrendingBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun getBinding(): ItemTrendingBinding {
            return binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemTrendingBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_trending,
            parent,
            false
        )
        val holder = ViewHolder(binding)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = getItem(position)
        if (repo != null) {
            holder.getBinding().imgLoad.load(
                "${ImageUtils.IMAGE_API_URL}${repo.posterPath}"
            ) {
                placeholder(R.drawable.placeholder)
            }
        }
    }
}