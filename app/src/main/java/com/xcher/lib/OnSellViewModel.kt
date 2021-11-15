package com.xcher.lib

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ktx.lib.base.LoadState
import kotlinx.coroutines.launch
import java.lang.Exception

class OnSellViewModel : BaseViewModel() {

    val dataList by lazy {
        MutableLiveData<List<OnSellBean.TbkDgOptimusMaterialResponse.ResultList.MapData>>()
    }

    fun invokeDada() {
        mLoadState.value = LoadState.LOADING
        viewModelScope.launch {
            try {
                val onSellList = mApi.get_on_sell(1).getResult()
                val dataLists = onSellList.tbk_dg_optimus_material_response.result_list.map_data
                if (dataLists.isEmpty()) {
                    mLoadState.value = LoadState.EMPTY
                } else {
                    mLoadState.value = LoadState.SUCCESS
                    dataList.postValue(onSellList.tbk_dg_optimus_material_response.result_list.map_data)
                }
            } catch (e: Exception) {
                mLoadState.value = LoadState.ERROR
            }
        }
    }
}