package com.connor.moviecat

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.connor.moviecat.contract.Event
import com.connor.moviecat.databinding.ActivityMainBinding
import com.connor.moviecat.model.net.ApiPath
import com.connor.moviecat.tag.Tag
import com.connor.moviecat.ui.MovieFragment
import com.connor.moviecat.ui.SearchActivity
import com.connor.moviecat.ui.TVShowFragment
import com.connor.moviecat.ui.adapter.TabPagerAdapter
import com.connor.moviecat.ui.adapter.TrendingAdapter
import com.connor.moviecat.utlis.Tools
import com.connor.moviecat.viewmodel.MainViewModel
import com.drake.channel.sendTag
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var trendingAdapter: TrendingAdapter
    private lateinit var tabPagerAdapter: TabPagerAdapter
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        trendingAdapter = TrendingAdapter(this)
        lifecycleScope.launch {
            viewModel.getPagingData(ApiPath.TRENDING_ALL_WEEK).collect {
                trendingAdapter.submitData(it)
            }
        }
        initViewPager()
        initTab()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                Tools.startActivity<SearchActivity>(this) {}
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initTab() {
        tabPagerAdapter = TabPagerAdapter(
            supportFragmentManager,
            lifecycle,
            viewModel.fragments,
            viewModel.titles
        )
        binding.pageTab.adapter = tabPagerAdapter
        binding.pageTab.offscreenPageLimit = 2
        TabLayoutMediator(binding.tab, binding.pageTab) { tab, position ->
            tab.text = viewModel.titles[position]
        }.attach()
        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        if (tab.position == 0)
                            viewModel.sendEvent(Event.Scroll)
                        binding.appbar.setExpanded(true)
                    }
                    1 -> {
                        if (tab.position == 1)
                            viewModel.sendEvent(Event.Scroll)
                        binding.appbar.setExpanded(true)
                    }
                }
            }

        })
    }

    private fun initViewPager() {
        with(binding.pagerImg) {
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS
            setPageTransformer(getCarouselPagerTransformer())
            adapter = trendingAdapter
        }
    }

    private fun getCarouselPagerTransformer() = CompositePageTransformer().apply {
        addTransformer(MarginPageTransformer(resources.getDimensionPixelSize(R.dimen.dp_24)))
        addTransformer { page, position ->
            page.scaleY = 0.95f + (1 - abs(position)) * 0.05f
        }
    }
}