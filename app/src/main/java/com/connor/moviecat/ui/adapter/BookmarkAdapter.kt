package com.connor.moviecat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.connor.moviecat.R
import com.connor.moviecat.databinding.ItemBookmarkBinding
import com.connor.moviecat.model.net.MovieUiResult
import com.connor.moviecat.model.room.MovieEntity
import com.connor.moviecat.utlis.ImageUtils

class BookmarkAdapter(private val onClick: (MovieEntity) -> Unit)
    : ListAdapter<MovieEntity, BookmarkAdapter.ViewHolder>(FlowerDiffCallback) {

    object FlowerDiffCallback : DiffUtil.ItemCallback<MovieEntity>() {
        override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem == newItem
        }
    }
    inner class ViewHolder(private val binding: ItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imgMovie.setOnClickListener {
                binding.m?.let {
                    onClick(it)
                }
            }
        }

            fun bind(movies: MovieEntity) {
                binding.m = movies
                binding.imgMovie.load(
                    "${ImageUtils.IMAGE_W_500}${movies.poster}"
                ) {
                    placeholder(R.drawable.placeholder)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBookmarkBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_bookmark,
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