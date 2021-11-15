package com.xcher.lib

import android.annotation.SuppressLint
import com.ktx.lib.base.BaseActivity
import com.xcher.lib.databinding.ActivityOtherLayoutBinding

class AppOtherActivity : BaseActivity<OtherViewModel, ActivityOtherLayoutBinding>(R.layout.activity_other_layout) {


    override fun initView() {

    }

    override fun observer() {

    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        acceptValue("test") {
            mBinding.run {
                mTest.text = it?.value + "---->>"
            }
        }
    }

    override fun initViewModel(): OtherViewModel {
        return OtherViewModel()
    }
}