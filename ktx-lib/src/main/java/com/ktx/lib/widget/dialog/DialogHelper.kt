package com.ktx.lib.widget.dialog

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import java.lang.ref.WeakReference

@Suppress("UNCHECKED_CAST")
class DialogHelper {

    private var mRootView: View? = null

    constructor(view: View?) {
        mRootView = view
    }

    constructor(context: Context?, layoutResId: Int) {
        mRootView = LayoutInflater.from(context).inflate(layoutResId, null)
    }

    fun getLayoutView(): View? {
        return mRootView
    }

}