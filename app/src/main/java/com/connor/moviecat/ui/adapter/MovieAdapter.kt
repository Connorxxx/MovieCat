package com.connor.moviecat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.connor.moviecat.R
import com.connor.moviecat.databinding.ItemMovieBinding
import com.connor.moviecat.model.net.MovieUiResult
import com.connor.moviecat.utlis.ImageUtils

class MovieAdapter(private val onClick: (MovieUiResult) -> Unit) :
    PagingDataAdapter<MovieUiResult, MovieAdapter.ViewHolder>(COMPARATOR) {

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

    inner class ViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imgMovie.setOnClickListener {
                binding.m?.let {
                    onClick(it)
                }
            }
        }

        fun bind(result: MovieUiResult) {
            binding.m = result
            binding.imgMovie.load(
                "${ImageUtils.IMAGE_W_500}${result.posterPath}"
            ) {
                placeholder(R.drawable.placeholder)
            }
        }

        fun getBinding(): ItemMovieBinding {
            return binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemMovieBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_movie,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      //  holder.getBinding().m = getItem(position)
        val repo = getItem(position)
        repo?.let { holder.bind(it) }
    }
}