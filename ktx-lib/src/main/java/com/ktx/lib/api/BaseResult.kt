package com.ktx.lib.api

data class BaseResult<T>(
    val code: Int,
    val success: Boolean,
    val message: String,
    val data: T
) {
    companion object {
        const val CODE_SUCCESS = 10000
    }

    fun getResult(): T {
        if (code == CODE_SUCCESS) {
            return data
        } else {
            throw ApiException(code, message)
        }
    }
}