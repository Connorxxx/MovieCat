package com.connor.moviecat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.connor.moviecat.R
import com.connor.moviecat.databinding.FragmentMovieBinding
import com.connor.moviecat.model.net.ApiPath
import com.drake.engine.base.EngineFragment
import com.drake.net.Get
import com.drake.net.utils.scopeNetLife

class MovieFragment : EngineFragment<FragmentMovieBinding>(R.layout.fragment_movie) {

    override fun initData() {

    }

    override fun initView() {
        scopeNetLife {
            binding.tvTest.text = Get<String>(ApiPath.MOVIE) {
                param(ApiPath.API_KEY, ApiPath.API_KEY_VALUE)
            }.await()
        }
    }


}