package com.connor.moviecat.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import androidx.paging.map
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.connor.moviecat.App.Companion.context
import com.connor.moviecat.common.BaseActivity
import com.connor.moviecat.R
import com.connor.moviecat.databinding.ActivitySearchBinding
import com.connor.moviecat.model.net.ApiPath
import com.connor.moviecat.model.net.MovieUiResult
import com.connor.moviecat.ui.adapter.FooterAdapter
import com.connor.moviecat.ui.adapter.SearchAdapter
import com.connor.moviecat.utlis.showSnackBar
import com.connor.moviecat.utlis.textChanges
import com.connor.moviecat.viewmodel.MainViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseActivity(R.layout.activity_search) {

    private lateinit var searchAdapter: SearchAdapter

    private val viewModel: MainViewModel by viewModel()
    private val default: CoroutineDispatcher by lazy { Dispatchers.Default }

    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val imm by lazy { context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setActionBarAndHome(binding.toolbar)
        initRV()
        initEditText()
        initScope()
    }

    @OptIn(FlowPreview::class)
    private fun initScope() {
        lifecycleScope.launch {
            launch {
                binding.etSearch.textChanges().debounce(700)
                    .filter { it.isNotEmpty() }
                    .collect {
                        viewModel.sendQuery(ApiPath.SEARCH_MULTI, it.toString())
                        binding.rv.scrollToPosition(0)
                    }
            }
            launch {
                viewModel.size.collect {
                    if (it == 0) {
                        binding.lottieError.visibility = View.VISIBLE
                    } else binding.lottieError.visibility = View.GONE
                }
            }
            viewModel.paging.collect {
                launch(default) {
                    searchAdapter.submitData(it)
                    searchAdapter.addLoadStateListener {
                        viewModel.setSize(searchAdapter.itemCount)
                    }
                }
            }
        }
    }

    private fun initRV() {
        with(binding.rv) {
            val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            layoutManager = manager
            searchAdapter = SearchAdapter {
                adapterOnClick(it)
            }
            adapter = searchAdapter.withLoadStateFooter(
                FooterAdapter { searchAdapter.retry() }
            )
            setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_MOVE -> imm.hideSoftInputFromWindow(windowToken, 0)
                    MotionEvent.ACTION_UP -> view.performClick()
                }
                super.onTouchEvent(motionEvent)
            }
        }
    }

    private fun initEditText() {
        binding.imgClean.setOnClickListener {
            binding.etSearch.setText("")
        }
        with(binding.etSearch) {
            requestFocus()
            imm.showSoftInput(this, 0)
            setOnEditorActionListener { textView, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH && textView.text.isNotBlank()) {
                    imm.hideSoftInputFromWindow(windowToken, 0)
                } else showSnackBar("Please Input")
                return@setOnEditorActionListener true
            }
        }
    }

    private fun adapterOnClick(result: MovieUiResult) {
        com.connor.moviecat.utlis.startActivity<DetailActivity>(this) {
            with(result) {
                putExtra("movie_id", id.toString())
                putExtra("media_type", mediaType)
                putExtra("poster_path", posterPath)
            }
        }
    }
}