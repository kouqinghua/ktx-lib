package com.xcher.lib.api


class ResultData<T>(
    val errorCode: Int,
    val errorMsg: String,
    val data: T,
) {

    fun getResult(): T {
        return data
    }
}