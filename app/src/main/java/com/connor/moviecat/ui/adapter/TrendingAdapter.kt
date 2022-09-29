package com.connor.moviecat.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.connor.moviecat.R
import com.connor.moviecat.databinding.ItemTrendingBinding
import com.connor.moviecat.model.net.TrendingResult
import com.connor.moviecat.utlis.ImageUtils

class TrendingAdapter(private val ctx: Context, private val trendingResults: List<TrendingResult>) :
    RecyclerView.Adapter<TrendingAdapter.ViewHolder>() {

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
        holder.getBinding().results = trendingResults[position]
        val load = trendingResults[position]

        holder.getBinding().imgLoad.load(
            "${ImageUtils.IMAGE_API_URL}${load.posterPath}"
        )
    }

    override fun getItemCount() = trendingResults.size

}