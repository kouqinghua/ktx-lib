package com.ktx.lib.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class SDKViewModel: ViewModel(), LifecycleObserver {

    val mLoadState by lazy {
        MutableLiveData<LoadState>()
    }

    val mError by lazy {
        MutableLiveData<String>()
    }
}