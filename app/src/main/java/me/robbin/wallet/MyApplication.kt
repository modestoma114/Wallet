package me.robbin.wallet

import android.app.Application
import me.robbin.swipeback.SwipeBack


/**
 * @PackageName: me.robbin.wallet
 * @Name:        MyApplication
 * @Description: TODO
 * @UpdateDate:  2021/3/2 下午5:45
 * @UpdateUser:  Robbin Ma
 * Create by Robbin Ma in 2021/3/2 下午5:45
 */

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        SwipeBack.init()
        SwipeBack.setSwipeBackForceEdge(true)
    }

}