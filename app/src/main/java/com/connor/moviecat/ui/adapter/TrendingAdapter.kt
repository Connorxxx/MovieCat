package com.connor.moviecat.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.connor.moviecat.App
import com.connor.moviecat.R
import com.connor.moviecat.databinding.ItemTrendingBinding
import com.connor.moviecat.model.net.MovieUiResult
import com.connor.moviecat.ui.DetailActivity
import com.connor.moviecat.utlis.ImageUtils
import com.connor.moviecat.utlis.loadWithQuality
import com.connor.moviecat.utlis.startActivity

class TrendingAdapter(private val ctx: Context) : PagingDataAdapter<MovieUiResult, TrendingAdapter.ViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<MovieUiResult>() {
            override fun areItemsTheSame(oldItem: MovieUiResult, newItem: MovieUiResult): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieUiResult, newItem: MovieUiResult): Boolean {
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
            holder.getBinding().imgLoad.setOnClickListener {
                startActivity<DetailActivity>(ctx) {
                    with(holder.getBinding().results!!) {
                        putExtra("movie_id", id.toString())
                        putExtra("media_type", mediaType)
                        putExtra("poster_path",posterPath)
                    }
                }
            }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getBinding().results = getItem(position)
        val repo = getItem(position)
        if (repo != null) {
            holder.getBinding().imgLoad.loadWithQuality(
                "${ImageUtils.IMAGE_API_URL}${repo.posterPath}",
                "${ImageUtils.IMAGE_W_500}${repo.posterPath}",
                R.drawable.placeholder,
                R.drawable.placeholder
            )
        }
    }
}