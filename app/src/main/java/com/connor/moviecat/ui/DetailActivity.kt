package com.connor.moviecat.ui

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.connor.moviecat.BaseActivity
import com.connor.moviecat.R
import com.connor.moviecat.databinding.ActivityDetailBinding
import com.connor.moviecat.model.room.MovieEntity
import com.connor.moviecat.utlis.ImageUtils
import com.connor.moviecat.utlis.Tools.openLink
import com.connor.moviecat.utlis.loadWithQuality
import com.connor.moviecat.viewmodel.DetailViewModel
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class DetailActivity : BaseActivity(R.layout.activity_detail) {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }

    private val viewModel by inject<DetailViewModel>()

    private val default: CoroutineDispatcher by lazy { Dispatchers.Default }
    private val main: CoroutineDispatcher by lazy { Dispatchers.Main }
    private val io: CoroutineDispatcher by lazy { Dispatchers.IO }

    private val type by lazy { intent.getStringExtra("media_type") ?: "movie" }
    private val id by lazy { intent.getStringExtra("movie_id") ?: "" }
    private val posterPath by lazy { intent.getStringExtra("poster_path") ?: "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = getColor(R.color.transparent)
        setActionBarAndHome(binding.toolbar)
        initScope()
        initPoster()
        initClick()
    }

    private fun initClick() {
        binding.detailPart.homepage.setOnClickListener {
            openLink(binding.detailPart.model!!.homepage, this, binding.imgPoster)
        }
        binding.detailPart.addDatabase.setOnClickListener {
            if (!viewModel.check.value) {
                with(binding.detailPart.model!!) {
                    val moves = MovieEntity(
                        id,
                        posterPath,
                        title,
                        releaseOrFirstAirDate,
                        voteAverage,
                        type
                    )
                    viewModel.insertMovie(moves)
                    viewModel.setCheck(true)
                }
            } else {
                viewModel.deleteMovie(id.toInt())
                viewModel.setCheck(false)
            }
        }
    }

    private fun initPoster() {
        binding.imgPoster.loadWithQuality(
            "${ImageUtils.IMAGE_API_URL}${posterPath}",
            "${ImageUtils.IMAGE_W_500}${posterPath}",
            R.drawable.placeholder,
            R.drawable.placeholder
        )
    }

    private fun initScope() {
        lifecycleScope.launch(main) {
            launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    viewModel.check.collect {
                        if (it) binding.detailPart.addDatabase.load(R.drawable.outline_bookmark_24)
                        else binding.detailPart.addDatabase.load(R.drawable.ic_baseline_bookmark_border_24)
                    }
                }
            }
            launch {
                viewModel.insert.collect {}
            }
            launch {
                viewModel.delete.collect {}
            }
            launch(default) {
                viewModel.getMovies.collect {
                    it.forEach { movie ->
                        ensureActive()
                        if (movie.id == id.toInt()) {
                            viewModel.setCheck(true)
                            this.cancel()
                        }
                    }
                }
            }
            launch {
                viewModel.detail(type, id).collect {
                    binding.detailPart.model = it
                }
            }
        }
    }
}