package com.xcher.lib

import com.ktx.lib.base.BaseBindAdapter
import com.xcher.lib.databinding.UiOnsellItemLayoutBinding

class OnSellAdapter(private val layoutId: Int) :
    BaseBindAdapter<OnSellBean.TbkDgOptimusMaterialResponse.ResultList.MapData, UiOnsellItemLayoutBinding>(layoutId) {


    override fun invoke(binding: UiOnsellItemLayoutBinding, data: OnSellBean.TbkDgOptimusMaterialResponse.ResultList.MapData, position: Int) {
        binding.onSellItem = data
    }

}