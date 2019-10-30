package com.grzegorziwanek.moviewatcher.utils

import android.content.Context
import android.widget.ImageView

class ImageLoader(private val context: Context) {

  fun loadImageFromUrl(url: String?, placeholderId: Int, receiver: ImageView) =
    GlideApp.with(context)
      .load(url)
      .placeholder(placeholderId)
      .into(receiver)
}