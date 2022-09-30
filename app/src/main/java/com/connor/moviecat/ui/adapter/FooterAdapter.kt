package com.connor.moviecat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.connor.moviecat.R
import com.connor.moviecat.databinding.FooterItemBinding

class FooterAdapter(val retry: () -> Unit) : LoadStateAdapter<FooterAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: FooterItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun getBinding(): FooterItemBinding {
            return binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding: FooterItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.footer_item,
            parent,
            false
        )
        val holder = ViewHolder(binding)
        holder.getBinding().retryButton.setOnClickListener {
            retry()
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.getBinding().progressBar.isVisible = loadState is LoadState.Loading
        holder.getBinding().retryButton.isVisible = loadState is LoadState.Error
    }
}