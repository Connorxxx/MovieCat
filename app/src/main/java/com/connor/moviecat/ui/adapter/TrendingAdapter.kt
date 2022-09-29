package com.connor.moviecat.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.connor.moviecat.R
import com.connor.moviecat.databinding.ItemTrendingBinding
import com.connor.moviecat.model.net.TrendingResult
import com.connor.moviecat.utlis.ImageUtils

class TrendingAdapter : PagingDataAdapter<TrendingResult, TrendingAdapter.ViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<TrendingResult>() {
            override fun areItemsTheSame(oldItem: TrendingResult, newItem: TrendingResult): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TrendingResult, newItem: TrendingResult): Boolean {
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
//        holder.getBinding().results = trendingResults[position]
//        val load = trendingResults[position]

        val repo = getItem(position)
        if (repo != null) {
            holder.getBinding().imgLoad.load(
                "${ImageUtils.IMAGE_API_URL}${repo.posterPath}"
            )
        }


    }

   // override fun getItemCount() = trendingResults.size

}