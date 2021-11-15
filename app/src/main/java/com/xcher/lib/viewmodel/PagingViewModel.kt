package com.xcher.lib.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import com.xcher.lib.BaseViewModel
import com.xcher.lib.datasource.DataPager
import com.xcher.lib.domain.Data

class PagingViewModel : BaseViewModel() {


//    fun getData() = Pager(PagingConfig(pageSize = 1)) {
//        DataSource()
//    }.flow.cachedIn(viewModelScope)

    private val pager = object : DataPager<Data>() {
        override suspend fun invoke(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, Data> {
            val page = params.key ?: return PagingSource.LoadResult.Page(emptyList(), null, null)
            return try {
                val data = mApi.getData(page).getResult()
                success(data.datas, page.plus(1))
            } catch (e: Exception) {
                failed(e)
            }
        }
    }

    val data = pager.getData(viewModelScope)
}

