package com.xcher.lib

import com.ktx.lib.api.BaseResult
import com.xcher.lib.api.ResultData
import com.xcher.lib.domain.PagingData
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    companion object{
//        const val BASE_URL = "https://api.sunofbeach.net/shop/"
        const val BASE_URL = "https://www.wanandroid.com/"
    }

    @GET("onSell/{page}")
    suspend fun get_on_sell(@Path("page") page: Int): BaseResult<OnSellBean>


    @GET("wenda/list/{pageId}/json")
    suspend fun getData(@Path("pageId") page: Int): ResultData<PagingData>
}