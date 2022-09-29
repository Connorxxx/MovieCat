package com.connor.moviecat.ui

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.connor.moviecat.R
import com.connor.moviecat.databinding.FragmentMovieBinding
import com.connor.moviecat.ui.adapter.MovieAdapter
import com.connor.moviecat.viewmodel.MainViewModel
import com.drake.channel.receiveTag
import com.drake.engine.base.EngineFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieFragment : EngineFragment<FragmentMovieBinding>(R.layout.fragment_movie) {

    private lateinit var movieAdapter: MovieAdapter

    private val viewModel: MainViewModel by viewModel()

    override fun initView() {
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rv.layoutManager = layoutManager
        movieAdapter = MovieAdapter()
        binding.rv.adapter = movieAdapter
        lifecycleScope.launch {
            viewModel.getMoviePagingData().collect {
                movieAdapter.submitData(it)
            }
        }
        receiveTag("smoothScrollToPosition") {
            binding.rv.scrollToPosition(0)
        }
    }

    override fun initData() {

    }
}