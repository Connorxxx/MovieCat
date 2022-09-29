package com.connor.moviecat.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import coil.load
import com.connor.moviecat.R
import com.connor.moviecat.databinding.FragmentMovieBinding
import com.connor.moviecat.model.net.ApiPath
import com.connor.moviecat.model.net.Movie
import com.connor.moviecat.model.net.MovieResult
import com.connor.moviecat.utlis.ImageUtils
import com.drake.brv.PageRefreshLayout.Companion.startIndex
import com.drake.brv.utils.*
import com.drake.engine.base.EngineFragment
import com.drake.logcat.LogCat
import com.drake.net.Get
import com.drake.net.utils.scope
import com.drake.net.utils.scopeNetLife
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MovieFragment : EngineFragment<FragmentMovieBinding>(R.layout.fragment_movie) {

    override fun initData() {

    }

    override fun initView() {
        //  val list = ArrayList<MovieResult>()
        initRV()

        scope {
            binding.rv.models = getData(1)
        }
//        scopeNetLife {
//            val json = Get<Movie>(ApiPath.MOVIE) {
//                param(ApiPath.API_KEY, ApiPath.API_KEY_VALUE)
//            }.await()
//            json.results.forEach {
//                list.add(it)
//            }
//
//            binding.rv.models = list
//        }
    }

    private fun initRV() {
        binding.rv.staggered(2).setup {
            addType<MovieResult>(R.layout.item_movie)
            onBind {
                findView<ImageView>(R.id.imgMovie).load("${ImageUtils.IMAGE_W_500}${getModel<MovieResult>().posterPath}")
            }
        }
        binding.page.onRefresh {
            scope {
                val data = Get<Movie>(ApiPath.MOVIE) {
                    param(ApiPath.API_KEY, ApiPath.API_KEY_VALUE)
                    param("page", index)
                }.await()
                addData(data.results) {
                    index < data.totalPages
                }
                binding.rv.addModels(data.results)
            }
        }.autoRefresh()
    }



    private suspend fun getData(page: Int) = coroutineScope {
        val json = Get<Movie>(ApiPath.MOVIE) {
            param(ApiPath.API_KEY, ApiPath.API_KEY_VALUE)
            param("page", startIndex)
        }.await()
        json.results
    }
}