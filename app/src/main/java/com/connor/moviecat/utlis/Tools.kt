package com.connor.moviecat.utlis

import android.content.Context
import android.content.Intent

object Tools {

    inline fun <reified T> startActivity(context: Context, block: Intent.() -> Unit) {
        val intent = Intent(context, T::class.java)
        intent.block()
        context.startActivity(intent)
    }
}