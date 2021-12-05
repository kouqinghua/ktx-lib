package com.ktx.lib.widget.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window

class DialogManager(dialog: XDialog?, window: Window?) {

    private var mDialog: XDialog? = dialog
    private var mWindow: Window? = window

    private var mHelper: DialogHelper? = null

    fun getDialog(): XDialog? {
        return mDialog
    }

    private fun getWindow(): Window? {
        return mWindow
    }

    private fun setViewHelper(helper: DialogHelper) {
        this.mHelper = helper
    }

    class DialogParams(var mContext: Context, var mThemeResId: Int) {
        var mCancelable = true
        var mView: View? = null
        var mLayId = 0

        var mWidth = ViewGroup.LayoutParams.WRAP_CONTENT
        var mHeight = ViewGroup.LayoutParams.WRAP_CONTENT
        var mGravity = Gravity.CENTER
        var mAnimation = 0

        /**
         * 绑定和设置参数
         */
        fun bind(mManager: DialogManager) {
            var viewHelper: DialogHelper? = null
            if (mLayId != 0) {
                viewHelper = DialogHelper(mContext, mLayId)
            }
            if (mView != null) {
                viewHelper = DialogHelper(mView)
            }
            if (viewHelper == null) {
                throw NullPointerException("请设置Dialog布局!")
            }
            mManager.setViewHelper(viewHelper)
            mManager.getDialog()!!.setContentView(viewHelper.getLayoutView()!!)
            val window = mManager.getWindow()
            window!!.decorView.setPadding(0, 0, 0, 0)
            window.setGravity(mGravity)
            val params = window.attributes
            params.width = mWidth
            params.height = mHeight
            window.attributes = params
            if (mAnimation != 0) {
                window.setWindowAnimations(mAnimation)
            }
        }
    }
}