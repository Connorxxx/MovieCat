package com.connor.moviecat.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.connor.moviecat.R
import com.connor.moviecat.contract.onScroll
import com.connor.moviecat.databinding.FragmentTVShowBinding
import com.connor.moviecat.model.net.ApiPath
import com.connor.moviecat.tag.Tag
import com.connor.moviecat.ui.adapter.FooterAdapter
import com.connor.moviecat.ui.adapter.MovieAdapter
import com.connor.moviecat.viewmodel.MainViewModel
import com.drake.channel.receiveTag
import com.drake.engine.base.EngineFragment
import com.drake.logcat.LogCat
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TVShowFragment : EngineFragment<FragmentTVShowBinding>(R.layout.fragment_t_v_show) {

    lateinit var tvAdapter: MovieAdapter

    private val viewModel: MainViewModel by sharedViewModel()

    override fun initView() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.layoutManager = layoutManager
        tvAdapter = MovieAdapter(requireActivity(), "tv")
        binding.rv.adapter = tvAdapter.withLoadStateFooter(
            FooterAdapter{ tvAdapter.retry() }
        )
    }

    override fun initData() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.getPagingData(ApiPath.TV).collect {
                    tvAdapter.submitData(it)
                }
            }
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect {
                    it.onScroll { binding.rv.scrollToPosition(0) }
                }
            }
        }
    }


}