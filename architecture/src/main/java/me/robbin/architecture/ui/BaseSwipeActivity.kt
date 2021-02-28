package me.robbin.architecture.ui

import androidx.databinding.ViewDataBinding
import me.robbin.architecture.data.BaseViewModel


/**
 * @PackageName: me.robbin.architecture.ui
 * @Name:        BaseSwipeActivity
 * @Description: The Activity that can be swiped back
 * @UpdateDate:  2021/2/28 23:55
 * @UpdateUser:  Robbin Ma
 * Created by Robbin Ma in 2021/2/28 23:55
 */

abstract class BaseSwipeActivity<VM: BaseViewModel, VDB: ViewDataBinding>: BaseActivity<VM, VDB>() {

    protected fun isSupportSwipeBack(): Boolean {
        return true
    }

}