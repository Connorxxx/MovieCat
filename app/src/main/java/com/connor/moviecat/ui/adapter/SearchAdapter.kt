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
import com.connor.moviecat.databinding.ItemSearchBinding
import com.connor.moviecat.model.net.MovieUiResult
import com.connor.moviecat.ui.DetailActivity
import com.connor.moviecat.utlis.ImageUtils
import com.connor.moviecat.utlis.startActivity

class SearchAdapter(private val ctx: Context) : PagingDataAdapter<MovieUiResult, SearchAdapter.ViewHolder>(COMPARATOR) {

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

    inner class ViewHolder(private val binding: ItemSearchBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun getBinding(): ItemSearchBinding {
            return binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSearchBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_search,
            parent,
            false
        )
        val holder = ViewHolder(binding)
        holder.getBinding().imgMovie.setOnClickListener {
            startActivity<DetailActivity>(ctx) {
                with(holder.getBinding().m!!) {
                    putExtra("movie_id", id.toString())
                    putExtra("media_type", mediaType)
                    putExtra("poster_path",posterPath)
                }
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getBinding().m = getItem(position)
        val repo = getItem(position)
        if (repo != null) {
            holder.getBinding().m = repo
            holder.getBinding().imgMovie.load(
                "${ImageUtils.IMAGE_W_500}${repo.posterPath}"
            ) {
                placeholder(R.drawable.placeholder)
            }
        }
    }
}