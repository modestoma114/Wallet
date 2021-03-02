package me.robbin.swipeback

import android.view.View
import androidx.annotation.FloatRange


/**
 * @PackageName: me.robbin.swipeback
 * @Name:        SwipeBackTransformer
 * @Description:
 * @UpdateDate:  2021/3/1 下午12:41
 * @UpdateUser:  Robbin Ma
 * Create by Robbin Ma in 2021/3/1 下午12:41
 */

interface SwipeBackTransformer {

    fun initialize(
        currentView: View,
        previousView: View?
    )

    fun transform(
        currentView: View,
        previousView: View?,
        @FloatRange(from = 0.0, to = 1.0) fraction: Float,
        direction: SwipeBackDirection
    )

    fun restore(
        currentView: View,
        previousView: View?
    )

}