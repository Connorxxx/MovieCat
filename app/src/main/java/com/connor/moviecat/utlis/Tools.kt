package com.connor.moviecat.utlis

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.google.android.material.snackbar.Snackbar

object Tools {

    inline fun <reified T> startActivity(context: Context, block: Intent.() -> Unit) {
        val intent = Intent(context, T::class.java)
        intent.block()
        context.startActivity(intent)
    }

    fun openLink(link: String, context: Context, view: View) {
        val sourceUri = link.toUri()
        if (sourceUri.toString().startsWith("http")) {
            val openURI = sourceUri.toString()
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, openURI.toUri())
        } else {
            Snackbar.make(view, "this $link is not a URL", Snackbar.LENGTH_LONG).show()
        }
    }
}