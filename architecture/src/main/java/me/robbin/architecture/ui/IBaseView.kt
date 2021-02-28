package me.robbin.architecture.ui

import android.os.Bundle


/**
 * @PackageName: me.robbin.architecture.ui
 * @Name:        IBaseView
 * @Description: View Interface
 * @UpdateDate:  2021/2/28 7:45 下午
 * @UpdateUser:  Robbin Ma
 * Created by Robbin Ma in 2021/2/28 7:45 下午
 */

interface IBaseView {

    fun initView(savedInstanceState: Bundle?)

    fun initData()

    fun createObserver()

}