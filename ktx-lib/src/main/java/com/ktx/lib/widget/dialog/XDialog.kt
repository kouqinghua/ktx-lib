package com.ktx.lib.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.ktx.lib.R

/**
 * 该自定义Dialog需要配合ViewDataBinding使用
 */
class XDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {

    private var mManager: DialogManager = DialogManager(this, window)

    class Builder internal constructor(context: Context, themeResId: Int) {

        private val mParams: DialogManager.DialogParams = DialogManager.DialogParams(context, themeResId)

        constructor(context: Context) : this(context, R.style.dialog)

        fun setLayoutView(view: View): Builder {
            mParams.mView = view
            mParams.mLayId = 0
            return this
        }

        fun setLayoutView(layoutResId: Int): Builder {
            mParams.mView = null
            mParams.mLayId = layoutResId
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            mParams.mCancelable = cancelable
            return this
        }

        fun fullWidth(): Builder {
            mParams.mWidth = ViewGroup.LayoutParams.MATCH_PARENT
            return this
        }

        fun fromBottom(): Builder {
            mParams.mAnimation = R.style.dialog_from_bottom_anim
            mParams.mGravity = Gravity.BOTTOM
            return this
        }

        fun setWidthAndHeight(width: Int, height: Int): Builder {
            mParams.mWidth = width
            mParams.mHeight = height
            return this
        }

        /**
         * 默认动画 显示/隐藏
         */
        fun animation(): Builder {
            mParams.mAnimation = R.style.dialog_scale_anim
            return this
        }

        /**
         * 设置动画
         * style指定显示/隐藏
         */
        fun setAnimation(styleAnimation: Int): Builder {
            mParams.mAnimation = styleAnimation
            return this
        }

        fun create(): XDialog {
            val dialog = XDialog(mParams.mContext, mParams.mThemeResId)
            dialog.setCanceledOnTouchOutside(mParams.mCancelable)
            dialog.setCancelable(mParams.mCancelable)
            mParams.bind(dialog.mManager)
            return dialog
        }
    }
}