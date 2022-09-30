package com.connor.moviecat.ui

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.connor.moviecat.R
import com.connor.moviecat.databinding.ActivitySearchBinding
import com.connor.moviecat.model.net.ApiPath
import com.connor.moviecat.ui.adapter.FooterAdapter
import com.connor.moviecat.ui.adapter.MovieAdapter
import com.connor.moviecat.utlis.showSnackBar
import com.connor.moviecat.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var searchAdapter: MovieAdapter

    private val viewModel: MainViewModel by viewModel()

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        val layoutManager = GridLayoutManager(this, 2)
        binding.rv.layoutManager = layoutManager
        searchAdapter = MovieAdapter()
        binding.rv.adapter = searchAdapter.withLoadStateFooter(
            FooterAdapter{ searchAdapter.retry() }
        )
        initEditText()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initEditText() {
        with(binding.etSearch) {
            val imm by lazy { context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
            postDelayed({
                requestFocus()
                imm.showSoftInput(this, 0)
               // searchAdapter.notifyDataSetChanged()
            }, 200)
            setOnEditorActionListener { textView, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH && textView.text.isNotBlank()) {
                    lifecycleScope.launch {
                        viewModel.getPagingData(ApiPath.SEARCH_MULTI, textView.text.toString()).collect {
                            searchAdapter.submitData(it)
                        }
                    }
                    imm.hideSoftInputFromWindow(windowToken, 0)
                } else showSnackBar("Please Input")
                return@setOnEditorActionListener true
            }
        }
    }
}