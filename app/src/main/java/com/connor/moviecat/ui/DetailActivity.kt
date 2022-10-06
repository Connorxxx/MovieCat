package com.connor.moviecat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import coil.load
import com.connor.moviecat.App
import com.connor.moviecat.BaseActivity
import com.connor.moviecat.R
import com.connor.moviecat.databinding.ActivityDetailBinding
import com.connor.moviecat.model.net.Detail
import com.connor.moviecat.model.room.MovieDao
import com.connor.moviecat.model.room.MovieEntity
import com.connor.moviecat.utlis.ImageUtils
import com.connor.moviecat.utlis.Tools.openLink
import com.connor.moviecat.utlis.loadWithQuality
import com.connor.moviecat.viewmodel.DetailViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.math.floor

class DetailActivity : BaseActivity(R.layout.activity_detail) {

    private val TAG = "DetailActivity"

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }

    private val viewModel by inject<DetailViewModel>()

    private val type by lazy { intent.getStringExtra("media_type") ?: "movie" }
    private val id by lazy { intent.getStringExtra("movie_id") ?: "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = getColor(R.color.transparent)
        setActionBarAndHome(binding.toolbar)
        val posterPath = intent.getStringExtra("poster_path")
        binding.imgPoster.loadWithQuality(
            "${ImageUtils.IMAGE_API_URL}${posterPath}",
            "${ImageUtils.IMAGE_W_500}${posterPath}",
            R.drawable.placeholder,
            R.drawable.placeholder
        )
        binding.detailPart.homepage.setOnClickListener {
            openLink(binding.detailPart.model!!.homepage, this, binding.imgPoster)
        }
        binding.detailPart.addDatabase.setOnClickListener {
            if (!viewModel.inCheck) {
                with(binding.detailPart.model!!) {
                    val moves = MovieEntity(
                        id,
                        posterPath!!,
                        title,
                        releaseOrFirstAirDate,
                        voteAverage,
                        type
                    )
                    viewModel.insertMovie(moves)
                    viewModel.inCheck = true
                }
            } else {
                viewModel.deleteMovie(id.toInt())
                viewModel.inCheck = false
            }

        }
        lifecycleScope.launch {
            launch {
                viewModel.insert.collect {
                    binding.detailPart.addDatabase.load(R.drawable.outline_bookmark_24)
                }
            }
            launch {
                viewModel.delete.collect {
                    binding.detailPart.addDatabase.load(R.drawable.ic_baseline_bookmark_border_24)
                }
            }
            launch {
                viewModel.getMovies.collect {
                    it.forEach { movie ->
                        ensureActive()
                        if (movie.id == id.toInt()) {
                            binding.detailPart.addDatabase.load(R.drawable.outline_bookmark_24)
                            viewModel.inCheck = true
                            this.cancel()
                        }
                    }
                }
            }
            viewModel.detail(type, id).collect {
                binding.detailPart.model = it
            }
        }
    }
}