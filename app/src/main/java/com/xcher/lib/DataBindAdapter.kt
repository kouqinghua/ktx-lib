package com.xcher.lib

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("on_sell_item_pic")
fun on_sell_item_pic(view: ImageView, str: String?) {
    Glide.with(view.context).load("https:${str}").into(view)
}