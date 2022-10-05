package com.connor.moviecat.utlis

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.paging.PagingSource
import coil.load
import com.connor.moviecat.App
import com.drake.logcat.LogCat
import com.google.android.material.snackbar.Snackbar
import kotlin.math.floor

inline fun <reified T : Any> fire(block: () -> PagingSource.LoadResult<Int, T>) = try {
    block()
} catch (e: Exception) {
    LogCat.e(e, "KtorTest")
    PagingSource.LoadResult.Error(e)
}

inline fun <reified T> startActivity(context: Context, block: Intent.() -> Unit) {
    val intent = Intent(context, T::class.java)
    intent.block()
    context.startActivity(intent)
}

fun ImageView.loadWithQuality(
    highQuality: String,
    lowQuality: String,
    placeholderRes: Int? = null,
    errorRes: Int? = null,
) {
    load(lowQuality) {
        placeholderRes?.let {
            placeholder(placeholderRes)
        }
        listener(onSuccess = { request, result ->
            load(highQuality) {
                placeholder(drawable) // If there was a way to not clear existing image before loading, this would not be required
                errorRes?.let { error(errorRes) }
            }
        })
    }
}

fun Double.toScore() = this.times(10).toInt()

fun Int.toHours() = floor((this / 60).toDouble()).toInt()

fun View.showSnackBar(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
}

fun String.showToast() {
    Toast.makeText(App.context, this, Toast.LENGTH_LONG).show()
}