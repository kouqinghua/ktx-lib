package com.xcher.lib.adapter

import com.xcher.lib.databinding.UiPageItemLayoutBinding
import com.xcher.lib.domain.Data

class PageAdapter(private val layoutId: Int) :
    PagingListAdapter<Data, UiPageItemLayoutBinding>(layoutId) {


    override fun invoke(binding: UiPageItemLayoutBinding, data: Data, position: Int) {
        binding.mPageItem = data
    }
}