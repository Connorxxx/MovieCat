package com.connor.moviecat.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.connor.moviecat.App
import com.connor.moviecat.App.Companion.context
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
    private val imm by lazy { context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setActionBarAndHome(binding.toolbar)
        initRV()
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

    private fun initRV() {
        with(binding.rv) {
            val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            layoutManager = manager
            searchAdapter = SearchAdapter(this@SearchActivity)
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
            postDelayed({
                requestFocus()
                imm.showSoftInput(this, 0)
            }, 200)
            addTextChangedListener {
                if (it.toString().isNotBlank()) binding.imgClean.visibility = View.VISIBLE
                else binding.imgClean.visibility = View.GONE
                lifecycleScope.launch {
                    delay(1000)
                    viewModel.getSearchPagingData(ApiPath.SEARCH_MULTI, it.toString())
                        .collect { result ->
                            searchAdapter.submitData(result)
                            binding.rv.scrollToPosition(0)
                        }
                }
            }
            setOnEditorActionListener { textView, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH && textView.text.isNotBlank()) {
                    lifecycleScope.launch {
                        viewModel.getSearchPagingData(
                            ApiPath.SEARCH_MULTI,
                            textView.text.toString()
                        ).collect {
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