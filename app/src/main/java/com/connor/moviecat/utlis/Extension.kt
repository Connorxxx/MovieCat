package com.connor.moviecat.utlis

import android.view.View
import android.widget.Toast
import com.connor.moviecat.App
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
}

fun String.showToast() {
    Toast.makeText(App.context, this, Toast.LENGTH_LONG).show()
}