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
import com.connor.moviecat.utlis.ImageUtils
import com.connor.moviecat.utlis.loadWithQuality
import com.connor.moviecat.viewmodel.DetailViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.math.floor

class DetailActivity : BaseActivity(R.layout.activity_detail) {

    private val TAG = "DetailActivity"

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }

    private val viewModel by inject<DetailViewModel>()

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
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.detail(
                intent.getStringExtra("media_type")!!,
                intent.getStringExtra("movie_id")!!
            ).collect {
                binding.detailPart.model = it
                Log.d(TAG, "onCreate: $it")
            }
        }
    }
}