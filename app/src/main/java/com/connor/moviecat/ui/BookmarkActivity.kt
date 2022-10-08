package com.connor.moviecat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.connor.moviecat.R
import com.connor.moviecat.databinding.ActivityBookmarkBinding

class BookmarkActivity : AppCompatActivity(R.layout.activity_bookmark) {

    private val binding by lazy { ActivityBookmarkBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}