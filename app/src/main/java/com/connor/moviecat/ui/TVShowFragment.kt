package com.connor.moviecat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.connor.moviecat.R
import com.connor.moviecat.databinding.FragmentTVShowBinding
import com.connor.moviecat.ui.adapter.TVAdapter
import com.connor.moviecat.viewmodel.MainViewModel
import com.drake.channel.receiveTag
import com.drake.engine.base.EngineFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TVShowFragment : EngineFragment<FragmentTVShowBinding>(R.layout.fragment_t_v_show) {

    lateinit var tvAdapter: TVAdapter

    private val viewModel: MainViewModel by viewModel()

    override fun initView() {
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rv.layoutManager = layoutManager
        tvAdapter = TVAdapter()
        binding.rv.adapter = tvAdapter
        lifecycleScope.launch {
            viewModel.getTVPagingData().collect {
                tvAdapter.submitData(it)
            }
        }
        receiveTag("smoothScrollToPosition") {
            binding.rv.scrollToPosition(0)
        }
    }

    override fun initData() {

    }


}