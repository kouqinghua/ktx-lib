package com.xcher.lib.datasource

import androidx.paging.PagingSource
import com.ktx.lib.base.RetrofitClient
import com.xcher.lib.Api
import com.xcher.lib.domain.Data

class DataSource : PagingSource<Int, Data>() {

    private val mApi by lazy {
        val mRetrofit = RetrofitClient.getRetrofit(Api.BASE_URL)
        mRetrofit.create(Api::class.java)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        return try {
            //页码未定义置为1
            val currentPage = params.key ?: 1
            println("currentPage---->" + currentPage)
            val resultData = mApi.getData(currentPage).getResult()
            //当前页码 小于 总页码 页面加1
            val nextPage = if (currentPage < resultData.pageCount ?: 0) {
                currentPage + 1
            } else {
                //没有更多数据
                null
            }

            LoadResult.Page(
                data = resultData.datas,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}