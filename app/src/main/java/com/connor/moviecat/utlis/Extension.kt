package com.connor.moviecat.utlis

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.paging.PagingSource
import coil.load
import com.connor.moviecat.App
import com.drake.logcat.LogCat
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
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

fun EditText.textChanges() = callbackFlow {
    val listener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            trySend(p0)
            Log.d("textChanges", "onTextChanged: ")
        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }
    addTextChangedListener(listener)
    awaitClose {
        removeTextChangedListener(listener)
        Log.d("textChanges", "textChanges: close")
    }
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