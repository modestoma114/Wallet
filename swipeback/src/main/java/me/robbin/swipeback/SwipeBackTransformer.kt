package me.robbin.swipeback

import android.view.View
import androidx.annotation.FloatRange
import androidx.annotation.NonNull
import androidx.annotation.Nullable


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
        @NonNull currentView: View,
        @Nullable previousView: View?
    )

    fun transform(
        @NonNull currentView: View,
        @Nullable previousView: View?,
        @FloatRange(from = 0.0, to = 1.0) fraction: Float
    )

    fun restore(
        @NonNull currentView: View,
        @Nullable previousView: View?
    )

}