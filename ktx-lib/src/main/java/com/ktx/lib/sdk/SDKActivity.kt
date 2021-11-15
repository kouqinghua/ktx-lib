package com.ktx.lib.sdk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleObserver
import com.gyf.barlibrary.ImmersionBar
import com.ktx.lib.Constants
import com.ktx.lib.R
import com.ktx.lib.base.LoadState
import com.ktx.lib.databinding.ActivityBaseLayoutBinding
import com.ktx.lib.databinding.UiLoadingLayoutBinding
import com.ktx.lib.manager.ActivityManager
import com.ktx.lib.manager.DialogManager
import com.ktx.lib.manager.SharedManager
import com.ktx.lib.manager.SlideBackManager
import com.ktx.lib.utils.StatusBar
import com.ktx.lib.utils.UHandler
import com.ktx.lib.utils.UPermission
import com.ktx.lib.widget.Progress

abstract class SDKActivity<T : ViewDataBinding>(private val layoutId: Int) : AppCompatActivity(), LifecycleObserver {

    protected val TAG: String by lazy {
        this::class.java.simpleName
    }

    private var REQUEST_CODE_PERMISSION = -10

    private lateinit var mBaseBinding: ActivityBaseLayoutBinding

    protected lateinit var mBinding: T
    private lateinit var mSuccessView: View
    private lateinit var mLoadingView: View

    private lateinit var mBaseToolbar: Toolbar


    private var mProgress: Progress? = null

    private val mActivityManager by lazy {
        ActivityManager.instance
    }

    protected val mDialogManager by lazy {
        DialogManager.instance
    }

    protected val mSharedManager by lazy {
        SharedManager.getInstance(this)
    }

    private var onRightClickListener: OnRightClickListener? = null

    val token by lazy {
        mSharedManager.getString(Constants.TOKEN)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBase()
        initialize()
        lifecycle.addObserver(this)
        mActivityManager.addActivity(this)
        SlideBackManager.instance.slideBack(this)
    }

    abstract fun initialize()

    private fun initBase() {
        mBaseBinding = loadBaseView()
        mSuccessView = loadSuccessView()
        mBaseBinding.mBaseContainer.addView(mSuccessView)

        mBaseToolbar = mBaseBinding.mTitleBarLayout.mBaseToolbar

        mBaseBinding.mTitleBarLayout.apply {
            mBaseBack.setOnClickListener {
                onBackPressed()
            }
        }

        StatusBar.init(this, mBaseToolbar)
    }

    private fun loadBaseView(): ActivityBaseLayoutBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_base_layout)
    }

    private fun loadSuccessView(): View {
        mBinding = DataBindingUtil.inflate(layoutInflater, layoutId, null, false)
        return mBinding.root
    }

    private fun loadLoadingView(): View {
        val binding = DataBindingUtil.inflate<UiLoadingLayoutBinding>(
            layoutInflater,
            R.layout.ui_loading_layout,
            null,
            false
        )
        return binding.root
    }

    protected fun setState(state: LoadState, msg: String) {
        when (state) {
            LoadState.LOADING -> {
                showProgress(this, msg, 0)
            }

            LoadState.SUCCESS -> {
                hideProgress(this)
            }

            LoadState.ERROR -> {
                hideProgress(this)
            }
        }
    }

    /**
     * 设置标题颜色
     */
    protected fun setToolBarColor(color: Int) {
        mBaseBinding.mTitleBarLayout.mBaseToolbar.setBackgroundColor(color)
    }

    protected fun hideTitleBar() {
        mBaseBinding.mTitleBarLayout.mBaseToolbar.visibility = GONE
    }

    /**
     * 设置支持toolbar, 避免状态栏与标题栏重合
     */
    protected fun supportToolBar(toolbar: Toolbar) {
        ImmersionBar.setTitleBar(this, toolbar)
    }

    protected fun setRightText(str: String) {
        mBaseBinding.mTitleBarLayout.mBaseRight.visibility = VISIBLE
        mBaseBinding.mTitleBarLayout.mBaseRightText.text = str
        mBaseBinding.mTitleBarLayout.mBaseRight.setOnClickListener {
            onRightClickListener?.onRightClick()
        }
    }

    /**
     * 设置StatusBarDar的两种模式 黑色字体与白色字体
     */
    protected fun setDarkBar(dark: Boolean) {
        ImmersionBar.with(this).statusBarColor(R.color.trans).statusBarDarkFont(dark).init()
        mBaseBinding.mTitleBarLayout.run {
            if (dark) {
                mBaseBackIcon.setBackgroundResource(R.drawable.icon_black_back)
            } else {
                mBaseBackIcon.setBackgroundResource(R.drawable.icon_white_back)
            }
        }
    }

    /**
     * 请求权限
     */
    open fun requestPermission(permissions: Array<String>, requestCode: Int) {
        this.REQUEST_CODE_PERMISSION = requestCode
        if (UPermission.checkPermissions(this, permissions)) {
            permissionSuccess(REQUEST_CODE_PERMISSION)
        } else {
            val needPermissions: List<String> = UPermission.getDeniedPermissions(this, permissions)
            ActivityCompat.requestPermissions(
                this,
                needPermissions.toTypedArray(),
                REQUEST_CODE_PERMISSION
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (UPermission.verifyPermissions(grantResults)) {
                permissionSuccess(REQUEST_CODE_PERMISSION)
            } else {
                permissionFail(REQUEST_CODE_PERMISSION)
            }
        }
    }


    protected open fun permissionSuccess(requestCode: Int) {

    }

    protected open fun permissionFail(requestCode: Int) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        ImmersionBar.with(this).destroy()
    }

    protected fun setTitleBarTitle(text: String) {
        mBaseBinding.mTitleBarLayout.mBaseTitleText.text = text
    }

    /**
     * 跳转页面
     */
    fun go(c: Class<*>) {
        val intent = Intent(this, c)
        startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out)
    }

    protected fun goTop(c: Class<*>) {
        val intent = Intent(this, c)
        startActivity(intent)
        overridePendingTransition(R.anim.from_bottom_in, R.anim.from_bottom_out)
    }

    protected fun goBottom() {
        finish()
        overridePendingTransition(0, R.anim.from_top_out)
    }

    protected fun alert(msg: String) {
        mBaseBinding.mBaseAlertText.text = msg
        mBaseBinding.mBaseAlertText.visibility = VISIBLE
        UHandler.postDelayed {
            mBaseBinding.mBaseAlertText.visibility = GONE
        }
    }

    /**
     * 显示模态进度框
     */
    private fun showProgress(activity: Activity, msg: String, tag: Int) {
        if (!activity.isFinishing) {
            if (mProgress == null) {
                mProgress = Progress(activity, msg, tag)
            }
            mProgress?.show(true)
            mProgress?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            mProgress?.setOnDismissListener {
                if (mProgress != null) {
                    mProgress?.dismiss()
                    mProgress = null
                }
            }
        }
    }

    /**
     * 隐藏模态进度框
     */
    private fun hideProgress(activity: Activity) {
        if (!activity.isFinishing) {
            if (mProgress != null) {
                mProgress?.dismiss()
                mProgress = null
            }
        }
    }

    protected fun hideBack() {
        mBaseBinding.mTitleBarLayout.run {
            this.mBaseBack.visibility = GONE
        }
    }

    protected fun <B : ViewDataBinding> createDialogBinding(resId: Int):  B {
        return DataBindingUtil.inflate(LayoutInflater.from(this),
            resId,
            null,
            false)
    }

    interface OnRightClickListener {
        fun onRightClick()
    }

    protected fun setOnRightClickListener(onRightClickListener: OnRightClickListener) {
        this.onRightClickListener = onRightClickListener
    }
}
