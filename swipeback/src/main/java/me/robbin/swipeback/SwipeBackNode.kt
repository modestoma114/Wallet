package me.robbin.swipeback

import android.app.Activity
import me.robbin.swipeback.utils.ActivityTranslucentConverter


/**
 * @PackageName: me.robbin.swipeback
 * @Name:        SwipeBackNode
 * @Description: SwipeBackNode
 * @UpdateDate:  2021/3/1 下午4:19
 * @UpdateUser:  Robbin Ma
 * Create by Robbin Ma in 2021/3/1 下午4:19
 */

class SwipeBackNode(private val mActivity: Activity) {


    private val mTranslucentConverter: ActivityTranslucentConverter = ActivityTranslucentConverter(mActivity)
    private val mThemeTranslucent: Boolean = mTranslucentConverter.isThemeTranslucent()

    private var mLayout: SwipeBackLayout? = null

}