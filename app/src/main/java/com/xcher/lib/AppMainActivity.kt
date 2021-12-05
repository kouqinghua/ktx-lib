package com.xcher.lib

import android.os.Message
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ktx.lib.base.BaseActivity
import com.ktx.lib.base.BaseBindAdapter
import com.ktx.lib.dialogBinding
import com.ktx.lib.dialogBottom
import com.ktx.lib.dialogCenter
import com.ktx.lib.utils.UHandler
import com.ktx.lib.widget.Progress
import com.ktx.lib.widget.XHandler
import com.ktx.lib.widget.dialog.XDialog
import com.xcher.lib.activity.AppPagingActivity
import com.xcher.lib.databinding.ActivityMainLayoutBinding
import com.xcher.lib.databinding.UiOnsellItemLayoutBinding
import com.xcher.lib.databinding.UiTestDialogLayoutBinding

class AppMainActivity : BaseActivity<OnSellViewModel, ActivityMainLayoutBinding>(R.layout.activity_main_layout),
    XHandler.HandlerBack, BaseBindAdapter.OnItemClickListener {

    private var lists: List<OnSellBean.TbkDgOptimusMaterialResponse.ResultList.MapData>? = null

    private lateinit var bottom: XDialog

    private val mAdapter by lazy {
        OnSellAdapter(R.layout.ui_onsell_item_layout)
    }

    private lateinit var mDialog: XDialog

    override fun initView() {

        val dialogBinding = dialogBinding<UiTestDialogLayoutBinding>(R.layout.ui_test_dialog_layout)
        bottom = dialogBottom(dialogBinding.root)
        bottom.setOnDismissListener {
            alert("dismiss")
        }

        dialogBinding.sAliPay.setOnClickListener {
            alert("支付宝")
            bottom.dismiss()
        }

        mBinding.run {
            mPaging.setOnClickListener {
                bottom.show()
            }
        }
    }

    override fun observer() {
        mViewModel.apply {
            setLoadState(this@AppMainActivity, mLoadState)
            dataList.observe(this@AppMainActivity, {
                lists = it
                alert("请求成功")
                mAdapter.setData(it)
            })
        }
    }

    override fun initData() {
        mViewModel.invokeDada()
    }

    override fun initViewModel(): OnSellViewModel {
        return OnSellViewModel()
    }

    override fun handleMessage(msg: Message?) {
//        mDialog.show()
    }

    override fun onItemClick(position: Int) {
        bottom.show()
//        postValue("test", lists!![position].title)
//        go(AppOtherActivity::class.java)
    }

}