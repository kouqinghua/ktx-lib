package com.ktx.lib

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ktx.lib.widget.dialog.XDialog

fun <B : ViewDataBinding> Context.dialogBinding(resId: Int): B {
    return DataBindingUtil.inflate(LayoutInflater.from(this),
        resId,
        null,
        false)
}

fun Context.dialogBottom(view: View): XDialog {
    return XDialog.Builder(this).apply {
        setLayoutView(view)
        fullWidth()
        fromBottom()
    }.create()
}

fun Context.dialogCenter(view: View): XDialog {
    return XDialog.Builder(this).apply {
        setLayoutView(view)
        fullWidth()
        animation()
    }.create()
}

fun Fragment.dialogBottom(view: View): XDialog {
    return XDialog.Builder(activity!!).apply {
        setLayoutView(view)
        fullWidth()
        fromBottom()
    }.create()
}

fun Fragment.dialogCenter(view: View): XDialog {
    return XDialog.Builder(activity!!).apply {
        setLayoutView(view)
        fullWidth()
        animation()
    }.create()
}