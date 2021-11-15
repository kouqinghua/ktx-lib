package com.xcher.lib.activity

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ktx.lib.base.BaseActivity
import com.xcher.lib.R
import com.xcher.lib.adapter.PageAdapter
import com.xcher.lib.databinding.ActivityPagingLayoutBinding
import com.xcher.lib.viewmodel.PagingViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AppPagingActivity : BaseActivity<PagingViewModel, ActivityPagingLayoutBinding>(R.layout.activity_paging_layout) {

    private val mPageAdapter by lazy {
        PageAdapter(R.layout.ui_page_item_layout)
    }

    override fun initViewModel(): PagingViewModel {
        return PagingViewModel()
    }

    override fun initView() {
        mBinding.mPagingList.run {
            layoutManager = LinearLayoutManager(this@AppPagingActivity)
            adapter = mPageAdapter
        }
    }

    override fun observer() {
    }

    override fun initData() {
        lifecycleScope.launchWhenCreated {
            mViewModel.data.collectLatest {
                mPageAdapter.submitData(it)
            }
        }
    }
}