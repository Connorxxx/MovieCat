package com.connor.moviecat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.connor.moviecat.R
import com.connor.moviecat.databinding.ActivityBookmarkBinding
import com.connor.moviecat.model.net.MovieUiResult
import com.connor.moviecat.model.room.MovieEntity
import com.connor.moviecat.ui.adapter.BookmarkAdapter
import com.connor.moviecat.viewmodel.BookmarkViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class BookmarkActivity : AppCompatActivity(R.layout.activity_bookmark) {

    private val binding by lazy { ActivityBookmarkBinding.inflate(layoutInflater) }

    private val viewModel by inject<BookmarkViewModel>()

    private lateinit var bookmarkAdapter: BookmarkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initScope()
        initRv()
    }

    private fun initRv() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvMovie.layoutManager = layoutManager
        bookmarkAdapter = BookmarkAdapter {
            adapterOnClick(it)
        }
        binding.rvMovie.adapter = bookmarkAdapter
    }

    private fun initScope() {
        lifecycleScope.launch {
            viewModel.getMovies.collect {
                bookmarkAdapter.submitList(it)
            }
        }
    }

    private fun adapterOnClick(result: MovieEntity) {
        com.connor.moviecat.utlis.startActivity<DetailActivity>(this) {
            with(result) {
                putExtra("movie_id", id.toString())
                putExtra("media_type", mediaType)
                putExtra("poster_path", poster)
            }
        }
    }
}