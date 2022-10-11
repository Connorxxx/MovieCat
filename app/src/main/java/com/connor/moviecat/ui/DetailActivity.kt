package com.connor.moviecat.ui

import android.animation.Animator
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.connor.moviecat.R
import com.connor.moviecat.common.BaseActivity
import com.connor.moviecat.databinding.ActivityDetailBinding
import com.connor.moviecat.model.net.ApiPath
import com.connor.moviecat.model.room.MovieEntity
import com.connor.moviecat.type.onDelete
import com.connor.moviecat.type.onInsert
import com.connor.moviecat.utlis.ImageUtils
import com.connor.moviecat.utlis.Tools.openLink
import com.connor.moviecat.utlis.loadWithQuality
import com.connor.moviecat.viewmodel.DetailViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        initListener()
        initScope()
        initPoster()
        initClick()
    }

    private fun initClick() {
        with(binding.detailPart) {
            homepage.setOnClickListener {
                openLink(model!!.homepage, this@DetailActivity, binding.imgPoster)
            }
            addDatabase.setOnClickListener {
                if (!viewModel.check.value) {
                    model!!.apply {
                        val moves = MovieEntity(
                            id, posterPath, title, releaseOrFirstAirDate, voteAverage, type
                        )
                        binding.lottieBookmark.isVisible = true
                        binding.lottieBookmark.playAnimation()
                        viewModel.insert(moves)
                        viewModel.setCheck(true)
                    }
                } else {
                    viewModel.delete(id.toInt())
                    viewModel.setCheck(false)
                }
            }
            imgRarbg.setOnClickListener {
                model?.let {
                    val path = it.imdbId.ifBlank { it.title }
                    openLink(
                        "${ApiPath.RARBG}$path",
                        this@DetailActivity,
                        binding.imgPoster
                    )
                }
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

    private fun initListener() {
        binding.lottieBookmark.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                binding.lottieBookmark.isVisible = false
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationRepeat(p0: Animator?) {

            }

        })
    }

    private fun initScope() {
        lifecycleScope.launch(main) {
            launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    viewModel.check.collect {
                        with(binding.detailPart.addDatabase) {
                            if (it) {
                                load(R.drawable.outline_bookmark_24)
                            }
                            else load(R.drawable.ic_baseline_bookmark_border_24)
                        }
                    }
                }
            }
            launch(io) {
                viewModel.dao.collect { type ->
                    with(type) {
                        onInsert { viewModel.insertMovie(it) }
                        onDelete { viewModel.deleteMovie(it) }
                    }
                }
            }
            launch(default) {
                viewModel.getMovies.collect {
                    it.forEach { movie ->
                        if (movie.id == id.toInt()) {
                            viewModel.setCheck(true)
                            return@forEach
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