package com.xcher.lib.datasource

import androidx.paging.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

abstract class DataPager<V : Any>(
    private val pageSize: Int = 20
) {

    abstract suspend fun invoke(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, V>

    fun getData(scope: CoroutineScope): Flow<PagingData<V>> {
        return Pager(PagingConfig(pageSize, initialLoadSize = pageSize)) {
            object : PagingSource<Int, V>() {
                override suspend fun load(params: LoadParams<Int>): LoadResult<Int, V> {
                    return invoke(params)
                }
            }
        }.flow.cachedIn(scope)
    }
}