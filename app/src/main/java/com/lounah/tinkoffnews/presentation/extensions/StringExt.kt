package com.lounah.tinkoffnews.presentation.extensions

import android.os.Build
import android.text.Html
import android.text.Spanned

fun String.fromHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this.trim(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this.trim())
    }
}