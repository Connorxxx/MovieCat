package com.connor.moviecat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.connor.moviecat.databinding.ActivityMainBinding
import com.connor.moviecat.ui.MovieFragment
import com.connor.moviecat.ui.TVShowFragment
import com.connor.moviecat.ui.adapter.TabPagerAdapter
import com.connor.moviecat.ui.adapter.TrendingAdapter
import com.connor.moviecat.viewmodel.MainViewModel
import com.drake.channel.sendTag
import com.drake.logcat.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TrendingAdapter
    private lateinit var tabPagerAdapter: TabPagerAdapter

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        adapter = TrendingAdapter()
        lifecycleScope.launch {
            viewModel.getPagingData().collect {
                adapter.submitData(it)
            }
        }
        initViewPager()
        initTab()
    }

    private fun initTab() {
        val titles = ArrayList<String>().apply {
            add("Movie")
            add("TV Show")
        }
        val fragments = ArrayList<Fragment>().apply {
            add(MovieFragment())
            add(TVShowFragment())
        }
        tabPagerAdapter = TabPagerAdapter(
            supportFragmentManager,
            lifecycle,
            fragments,
            titles
        )
        binding.pageTab.adapter = tabPagerAdapter
        binding.pageTab.offscreenPageLimit = 2
        TabLayoutMediator(binding.tab, binding.pageTab) { tab, position ->
            tab.text = titles[position]
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
                            sendTag("smoothScrollToPosition")
                        binding.appbar.setExpanded(true)
                    }
                    1 -> {
                        if (tab.position == 1)
                            sendTag("smoothScrollToPosition")
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
            adapter = this@MainActivity.adapter
        }
    }

    private fun loadTrending(page: Int) {
        lifecycleScope.launch {
            viewModel.getTrending(viewModel.page).collect {
                withContext(Dispatchers.Main) {
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun getCarouselPagerTransformer() = CompositePageTransformer().apply {
        addTransformer(MarginPageTransformer(resources.getDimensionPixelSize(R.dimen.dp_24)))
        addTransformer { page, position ->
            page.scaleY = 0.95f + (1 - abs(position)) * 0.05f
        }
    }
}