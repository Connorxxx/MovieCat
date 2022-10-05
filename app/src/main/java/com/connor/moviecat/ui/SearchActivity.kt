package com.connor.moviecat.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.connor.moviecat.App
import com.connor.moviecat.BaseActivity
import com.connor.moviecat.R
import com.connor.moviecat.contract.onScroll
import com.connor.moviecat.databinding.ActivityMainBinding
import com.connor.moviecat.databinding.ActivitySearchBinding
import com.connor.moviecat.model.net.ApiPath
import com.connor.moviecat.ui.adapter.FooterAdapter
import com.connor.moviecat.ui.adapter.MovieAdapter
import com.connor.moviecat.ui.adapter.SearchAdapter
import com.connor.moviecat.utlis.showSnackBar
import com.connor.moviecat.viewmodel.MainViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseActivity(R.layout.activity_search) {

    private lateinit var searchAdapter: SearchAdapter

    private val viewModel: MainViewModel by viewModel()

    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setActionBarAndHome(binding.toolbar)
        val layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.rv.layoutManager = layoutManager
        searchAdapter = SearchAdapter(this)
        binding.rv.adapter = searchAdapter.withLoadStateFooter(
            FooterAdapter{ searchAdapter.retry() }
        )
        initEditText()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect {
                    it.onScroll {
                        Log.d("MovieFragment", "onCreate: ")
                    }
                }
            }
        }
    }

    private fun initEditText() {
        with(binding.etSearch) {
            val imm by lazy { context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
            postDelayed({
                requestFocus()
                imm.showSoftInput(this, 0)
            }, 200)
            setOnEditorActionListener { textView, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH && textView.text.isNotBlank()) {
                    lifecycleScope.launch {
                        viewModel.getSearchPagingData(ApiPath.SEARCH_MULTI, textView.text.toString()).collect {
                            searchAdapter.submitData(it)
                            binding.rv.scrollToPosition(0)
                        }
                    }
                    imm.hideSoftInputFromWindow(windowToken, 0)
                } else showSnackBar("Please Input")
                return@setOnEditorActionListener true
            }
        }
    }
}