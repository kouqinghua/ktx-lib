package com.ktx.lib.bus

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataBus {

    /**
     * 只有当前存活的owner才能收到消息, 即，尚未创建成功的owner将不会收到消息，
     * 即使它进行了监听
     */
    private val mAliveOwners: HashMap<String, AliveLiveData<BusData?>> = HashMap()

    /**
     * 全部owner都能收到消息，就算将要跳转的Activity并没创建成功的情况
     */
    private val mStickyOwners: HashMap<String, MutableLiveData<BusData?>> = HashMap()


    class SingleHolder {
        companion object {
            val DATA_BUS = LiveDataBus()
        }
    }

    companion object {
        fun get(): LiveDataBus {
            return SingleHolder.DATA_BUS
        }
    }

    /**
     * 获取消息通道, 仅支持当前存活的 lifeCycleOwner
     */
    fun getChannel(key: String): MutableLiveData<BusData?> {
        if (!mAliveOwners.containsKey(key)) {
            mAliveOwners[key] = AliveLiveData()
        }
        return mAliveOwners[key]!!
    }

    /**
     * 获取默认消息通道,  支持所有lifeCycleOwner，包括目前还没创建成功的
     */
    fun getSticky(key: String): MutableLiveData<BusData?>? {
        var mSticky: MutableLiveData<BusData?>? = null
        if (mStickyOwners.containsKey(key)) {
            mSticky = mStickyOwners[key]
            mStickyOwners.remove(key)
        }

        return mSticky
    }

    /**
     * 只有当前存活的lifeCycleOwner才会收到 消息, 重写它的observer的mLastVersion
     */
    class AliveLiveData<T> : MutableLiveData<T>() {

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
            hook(observer)
        }

        private fun hook(observer: Observer<in T>) {
            val classLiveData: Class<LiveData<*>> = LiveData::class.java
            val fieldObservers = classLiveData.getDeclaredField("mObservers")
            fieldObservers.isAccessible = true
            val mObservers = fieldObservers[this]
            val classObservers: Class<*> = mObservers.javaClass

            val methodGet = classObservers.getDeclaredMethod("get", Any::class.java)
            methodGet.isAccessible = true
            val objectWrapperEntry = methodGet.invoke(mObservers, observer)
            val objectWrapper =
                (objectWrapperEntry as Map.Entry<*, *>).value!!
            val classObserverWrapper: Class<*>? = objectWrapper.javaClass.superclass

            val fieldLastVersion =
                classObserverWrapper!!.getDeclaredField("mLastVersion")
            fieldLastVersion.isAccessible = true
            val fieldVersion = classLiveData.getDeclaredField("mVersion")
            fieldVersion.isAccessible = true
            val objectVersion = fieldVersion[this]
            fieldLastVersion[objectWrapper] = objectVersion
        }
    }
}