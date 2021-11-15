package com.xcher.lib

import androidx.paging.PagingSource
import com.ktx.lib.base.RetrofitClient
import com.ktx.lib.base.SDKViewModel
import kotlin.Exception

open class BaseViewModel : SDKViewModel() {

    private val mRetrofit by lazy {
        RetrofitClient.getRetrofit(Api.BASE_URL)
    }

    protected val mApi by lazy {
        mRetrofit.create(Api::class.java)
    }

    protected fun <T : Any> success(
        datas: List<T>,
        nextPage: Int,
    ): PagingSource.LoadResult.Page<Int, T> {
        return PagingSource.LoadResult.Page(datas, null, nextPage)
    }

    protected fun <T : Any> failed(e: Exception): PagingSource.LoadResult.Error<Int, T> {
        return PagingSource.LoadResult.Error(e)
    }

}