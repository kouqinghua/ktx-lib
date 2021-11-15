package com.ktx.lib.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ktx.lib.Constants
import com.ktx.lib.bus.BusData
import com.ktx.lib.bus.LiveDataBus
import com.ktx.lib.sdk.SDKFragment

abstract class BaseFragment<V : SDKViewModel, T : ViewDataBinding>(layoutId: Int) :
    SDKFragment<T>(layoutId) {

    protected lateinit var mViewModel: V

    override fun initialize() {
        mViewModel = initViewModel()
        lifecycle.addObserver(mViewModel)

        mViewModel.mError.observe(this) {
            alert(it)
        }

        initView()
        observer()
        initData()
    }

    abstract fun initViewModel(): V

    abstract fun initView()

    abstract fun observer()

    protected open fun initData() {}

    protected fun setLoadState(
        owner: LifecycleOwner,
        state: MutableLiveData<LoadState>,
        msg: String = "加载中..",
    ) {
        state.observe(owner) {
            setState(it, msg)
        }
    }

    protected fun postValue(key: String, value: String, data: BusData? = null) {
        LiveDataBus.get().getSticky(key)?.postValue(BusData(value, data))
    }

    protected fun acceptValue(key: String, observer: Observer<BusData?>) {
        LiveDataBus.get().getSticky(key)?.observe(this, observer)
    }

    protected fun token(): String? {
        return mSharedManager.getString(Constants.TOKEN)
    }
}