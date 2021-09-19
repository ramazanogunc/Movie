package com.ramo.movie.util

import android.view.View

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun textShorting(string: String, character: Int): String {
    return if (string.length > character)
        "${string.subSequence(0, character)}..."
    else
        string
}