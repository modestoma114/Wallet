package me.robbin.architecture.ui

import android.os.Bundle
import androidx.fragment.app.Fragment


/**
 * @PackageName: me.robbin.architecture.ui
 * @Name:        BaseFragment
 * @Description: Base Fragment
 * @UpdateDate:  2021/2/28 20:17
 * @UpdateUser:  Robbin Ma
 * Created by Robbin Ma in 2021/2/28 20:17
 */

class BaseFragment: Fragment(), IBaseView {

    override fun initView(savedInstanceState: Bundle?) {}

    override fun initData() {}

    override fun createObserver() {}

}