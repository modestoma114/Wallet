package me.robbin.architecture.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import me.robbin.architecture.data.BaseViewModel
import me.robbin.architecture.ext.getVmCls
import me.robbin.architecture.func.DataBindingConfig

/**
 * @PackageName: me.robbin.architecture.ui
 * @Name:        BaseActivity
 * @Description: Base Activity
 * @UpdateDate:  2021/2/28 18:59
 * @UpdateUser:  Robbin Ma
 * Created by Robbin Ma in 2021/2/28 18:59
 */

abstract class BaseActivity<VM: BaseViewModel, VDB: ViewDataBinding>: AppCompatActivity(), IBaseView {

    protected lateinit var mViewModel: VM

    private lateinit var mBinding: VDB

    protected abstract fun getDataBindingConfig(): DataBindingConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = createViewModel()
        mBinding = DataBindingUtil.setContentView(this, getDataBindingConfig().getLayout())
        mBinding.lifecycleOwner = this
        mBinding.setVariable(
            getDataBindingConfig().getVmVariableId(),
            getDataBindingConfig().getStateViewModel()
        )
        val bindingParams = getDataBindingConfig().getDataBindingParams()
        bindingParams.forEach { key, value ->
            mBinding.setVariable(key, value)
        }
        init(savedInstanceState)
    }

    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmCls(this))
    }

    protected fun init(savedInstanceState: Bundle?) {
        initView(savedInstanceState)
        initData()
        createObserver()
    }

    override fun initView(savedInstanceState: Bundle?) {}

    override fun initData() {}

    override fun createObserver() {}

}